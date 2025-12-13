package com.bmc.jenkins.zadviser;

import static com.bmc.jenkins.zadviser.service.DataTransmitService.transmitData;
import static com.bmc.jenkins.zadviser.service.JenkinsDataService.getJenkinsData;

import com.bmc.jenkins.zadviser.exceptions.MissingConfigException;
import com.bmc.jenkins.zadviser.exceptions.MissingDataException;
import com.bmc.jenkins.zadviser.exceptions.ZAdviserResponseException;
import com.bmc.jenkins.zadviser.model.CombinedRunData;
import com.bmc.jenkins.zadviser.model.PluginConfiguration;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@Extension
public class PipelineAnalysisMain extends RunListener<Run<?, ?>> {
    private static final Logger LOGGER = Logger.getLogger(PipelineAnalysisMain.class.getName());

    @Override
    public void onCompleted(Run<?, ?> run, @NonNull TaskListener listener) {
        listener.getLogger()
                .println("[zAdviser Pipeline Analysis Plugin]: Build Completed: " + run.getFullDisplayName()
                        + "starting data transfer to zAdviser");

        try {
            PluginConfiguration config = PluginConfiguration.get();
            validateConfiguration(config);

            CombinedRunData jenkinsDataServiceResponse = getJenkinsData(
                    run,
                    config.getUsername(),
                    config.getToken().getPlainText(),
                    config.getTeamHash().getPlainText(),
                    listener);

            transmitData(config.getzAdviserURL(), jenkinsDataServiceResponse);
        } catch (ZAdviserResponseException e) {
            listener.getLogger()
                    .println("[zAdviser Pipeline Analysis Plugin]: zAdviser server responded with error: "
                            + e.getStatusCode() + " " + e.getResponseBody());
            listener.getLogger()
                    .println(
                            "[zAdviser Pipeline Analysis Plugin]: Please check the zAdviser server logs for more information");
        } catch (MissingDataException | MissingConfigException e) {
            listener.getLogger().println("[zAdviser Pipeline Analysis Plugin]: " + e.getMessage());
        } catch (Exception e) {
            listener.getLogger().println("[zAdviser Pipeline Analysis Plugin]: Error: " + e);
            LOGGER.log(Level.WARNING, "[zAdviser Pipeline Analysis Plugin] Unknown error stack trace: ", e);
        }
    }

    private void validateConfiguration(PluginConfiguration config)
            throws MissingConfigException {
        if (config == null) throw new MissingConfigException("Entire Configuration Object");

        String username = config.getUsername();
        String teamHash = config.getTeamHash().getPlainText();
        String jenkinsToken = config.getToken().getPlainText();
        String zAdviserURL = config.getzAdviserURL();

        if (username == null || username.isBlank()) throw new MissingConfigException("Jenkins Username");
        if (teamHash.isBlank()) throw new MissingConfigException("Team Hash");
        if (jenkinsToken.isBlank()) throw new MissingConfigException("Jenkins Auth Token");
        if (zAdviserURL == null || zAdviserURL.isBlank()) throw new MissingConfigException("zAdviser URL");
    }
}

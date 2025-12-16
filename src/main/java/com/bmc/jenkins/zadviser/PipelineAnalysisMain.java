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
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

@Extension
public class PipelineAnalysisMain extends RunListener<Run<?, ?>> {
    private static final Logger LOGGER = Logger.getLogger(PipelineAnalysisMain.class.getName());

    @Override
    public void onCompleted(Run<?, ?> run, @NonNull TaskListener listener) {
        if (!(run instanceof WorkflowRun)) {
            listener.getLogger()
                    .println(
                            "[zAdviser Pipeline Analysis Plugin]: Since this is not a pipeline job, no data will be transferred to zAdviser");

            return;
        }

        listener.getLogger()
                .println("[zAdviser Pipeline Analysis Plugin]: Build Completed: " + run.getFullDisplayName()
                        + "starting data transfer to zAdviser");
        try {
            PluginConfiguration config = PluginConfiguration.get();
            validateConfiguration(config);

            CombinedRunData jenkinsDataServiceResponse =
                    getJenkinsData(run, config.getTeamHash().getPlainText());

            transmitData(config.getzAdviserURL(), jenkinsDataServiceResponse);
            listener.getLogger().println("[zAdviser Pipeline Analysis Plugin]: Data transfer complete");
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

    private void validateConfiguration(PluginConfiguration config) throws MissingConfigException {
        if (config == null) throw new MissingConfigException("Entire Configuration Object");

        String teamHash = config.getTeamHash().getPlainText();
        String zAdviserURL = config.getzAdviserURL();

        if (teamHash.isBlank()) throw new MissingConfigException("Team Hash");
        if (zAdviserURL == null || zAdviserURL.isBlank()) throw new MissingConfigException("zAdviser URL");
    }
}

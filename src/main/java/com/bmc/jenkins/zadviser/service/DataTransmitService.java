package com.bmc.jenkins.zadviser.service;

import com.bmc.jenkins.zadviser.exceptions.ZAdviserResponseException;
import com.bmc.jenkins.zadviser.model.CombinedRunData;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class DataTransmitService {
    private static final HttpClient HTTP_CLIENT =
            HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

    public static void transmitData(String url, CombinedRunData jenkinsDataServiceResponse)
            throws ZAdviserResponseException {
        String payloadStr = getPayloadStr(jenkinsDataServiceResponse);

        transmitDataToZAdviser(url, payloadStr);
    }

    private static String getPayloadStr(CombinedRunData jenkinsDataServiceResponse) {
        JSONObject payload = new JSONObject(jenkinsDataServiceResponse);
        payload.put("buildData", new JSONObject(jenkinsDataServiceResponse));
        String testResult = jenkinsDataServiceResponse.getTestData();
        if (testResult != null) {
            payload.put("testData", new JSONObject(testResult));
        }

        return payload.toString();
    }

    private static void transmitDataToZAdviser(String url, String payload) throws ZAdviserResponseException {
        try {
            String zAdviserEndpoint = "jenkins";
            if (url.endsWith("/")) {
                url = url + zAdviserEndpoint;
            } else {
                url = url + "/" + zAdviserEndpoint;
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 && response.statusCode() != 201) {
                throw new ZAdviserResponseException(response.statusCode(), response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new ZAdviserResponseException(500, "zAdviser server unreachable");
        }
    }
}

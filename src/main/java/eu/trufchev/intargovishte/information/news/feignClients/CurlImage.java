package eu.trufchev.intargovishte.information.news.feignClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CurlImage {

    public String fetchJsonWithCurl(String url) {
        try {
            // Prepare the cURL command
            String[] command = {"curl", "-X", "GET", url};

            // Execute the command
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }

            // Wait for the process to complete
            process.waitFor();

            return jsonResponse.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

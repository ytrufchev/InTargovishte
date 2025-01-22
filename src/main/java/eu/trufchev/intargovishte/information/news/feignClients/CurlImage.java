package eu.trufchev.intargovishte.information.news.feignClients;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurlImage {

    public String fetchJsonWithCurl(String url) {
        try {
            // Create HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Build the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            // Send the request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if response is successful
            if (response.statusCode() == 200) {
                return response.body(); // Return the JSON response body
            } else {
                System.out.println("Failed with HTTP code: " + response.statusCode());
                return null; // Handle non-success status codes
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null in case of exception
        }
    }
}

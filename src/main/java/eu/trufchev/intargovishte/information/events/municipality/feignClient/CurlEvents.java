package eu.trufchev.intargovishte.information.events.municipality.feignClient;

import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurlEvents {
    public static void CurlEvent() {
        try {
            // Create HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Build the request using curl headers and URL
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://targovishte.bg/customSearchWCM/query"))
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate, br, zstd")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Connection", "keep-alive")
                    .header("Host", "targovishte.bg")
                    .header("Referer", "https://targovishte.bg/wps/portal/municipality-targovishte/actual/events")
                    .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"")
                    .header("Sec-Ch-Ua-Mobile", "?0")
                    .header("Sec-Ch-Ua-Platform", "\"macOS\"")
                    .header("Sec-Fetch-Dest", "empty")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Site", "same-origin")
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("context=targovishte.bg1005&libName=content&saId=98cf5967-2cf7-4bda-9bf5-f41a08cc073a"))
                    .build();

            // Send the request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

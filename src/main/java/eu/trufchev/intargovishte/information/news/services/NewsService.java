package eu.trufchev.intargovishte.information.news.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import eu.trufchev.intargovishte.information.news.entities.News;
import eu.trufchev.intargovishte.information.news.feignClients.NewsClient;
import eu.trufchev.intargovishte.information.news.feignClients.TargovishteBgClient;
import eu.trufchev.intargovishte.information.news.feignClients.WordPressFeignClient;
import eu.trufchev.intargovishte.information.news.repositories.NewsRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    NewsClient newsClient;
    @Autowired
    TargovishteBgClient targovishteBgClient;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private WordPressFeignClient wordPressFeignClient;

    public String fetchImageUrl(String imageUrl) {
        try {
            // Fetch media details from the WordPress API using Feign
            String jsonResponse = wordPressFeignClient.getFromUrl(imageUrl);

            // Parse the response to extract the image URL
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            JsonNode guidNode = rootNode.path("guid").path("rendered");

            if (!guidNode.isMissingNode()) {
                return guidNode.asText();  // Return the image URL
            } else {
                return "https://i.sstatic.net/y9DpT.jpg";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<News> getNewsUpdates() throws JsonProcessingException {
        String newsString = newsClient.getNews();
        String targovishtebgnews = targovishteBgClient.getNews();
        List<News> newsList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String combinedJsonString = "";

        try {
            // Parse JSON strings into ArrayNode
            ArrayNode newsArray = (ArrayNode) mapper.readTree(newsString);
            ArrayNode targovishteArray = (ArrayNode) mapper.readTree(targovishtebgnews);

            // Combine arrays
            ArrayNode combinedArray = mapper.createArrayNode();
            combinedArray.addAll(newsArray);
            combinedArray.addAll(targovishteArray);

            // Convert combined array back to JSON string
            combinedJsonString = mapper.writeValueAsString(combinedArray);
        } catch (Exception e) {
            e.printStackTrace();
            return newsList; // Return empty list on failure
        }

        JsonNode rootNode = mapper.readTree(combinedJsonString);
        for (JsonNode news : rootNode) {
            try {
                News newsEntry = new News();

                // Normalize ID field
                Long id = news.has("id") ? news.get("id").asLong() : null;

                // Normalize date field
                String date = news.has("date") ? news.get("date").asText() : null;

                // Normalize title field
                String title = news.has("title") && news.get("title").has("rendered")
                        ? news.get("title").get("rendered").asText()
                        : "Untitled";

                // Normalize content field
                String content = news.has("content") && news.get("content").has("rendered")
                        ? Jsoup.parse(news.get("content").get("rendered").asText()).text().replaceAll(";", "")
                        : "No content available";

                // Trim content if too long
                if (content.length() > 5000) {
                    content = content.substring(0, 5000);
                }

                // Normalize image field
                String image = news.has("yoast_head_json") && news.get("yoast_head_json").has("og_image")
                        && news.get("yoast_head_json").get("og_image").isArray()
                        ? news.get("yoast_head_json").get("og_image").get(0).get("url").asText()
                        : fetchImageUrl(news.get("_links").get("wp:featuredmedia").get(0).get("href").asText());

                // Normalize link field
                String link = news.has("link") ? news.get("link").asText() : null;

                // Skip if ID is null or already exists in the repository
                if (id == null || newsRepository.existsById(id)) {
                    continue;
                }

                // Populate news entry
                newsEntry.setId(id);
                newsEntry.setDate(date);
                newsEntry.setTitle(title);
                newsEntry.setContent(content);
                newsEntry.setImage(image);
                newsEntry.setLink(link);
                newsList.add(newsEntry);

                // Save the new news entry to the repository
                newsRepository.save(newsEntry);
            } catch (Exception e) {
                e.printStackTrace(); // Log and skip problematic news items
            }
        }
        return newsList;
    }


    public List<News> getLatestNews() {
        // Fetch all news sorted by date in descending order
        List<News> allNews = newsRepository.findLatestNews();
        // Limit to 10 items
        return allNews.stream().limit(10).collect(Collectors.toList());
    }

    public List<News> getAllNews() {
        // Fetch all news sorted by date in descending order
        List<News> allNews = newsRepository.findLatestNewsLimited();
        // Limit to 100 items
        return allNews.stream().limit(100).collect(Collectors.toList());
    }
}
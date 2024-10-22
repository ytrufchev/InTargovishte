package eu.trufchev.intargovishte.information.news.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trufchev.intargovishte.information.news.entities.News;
import eu.trufchev.intargovishte.information.news.feignClients.NewsClient;
import eu.trufchev.intargovishte.information.news.repositories.NewsRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    NewsClient newsClient;
    @Autowired
    ObjectMapper objectMapper;

    public List<News> getNewsUpdates() throws JsonProcessingException {
        // Fetch the raw news data from the external API
        String newsString = newsClient.getNews();

        List<News> newsList = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(newsString);
        // Iterate through the fetched news entries
        for (JsonNode news : rootNode) {
            Long id = news.get("id").longValue();

            // Check if this news entry already exists in the database using its ID
            if (!newsRepository.existsById(id)) {
                // If the news entry doesn't exist, process and add it to the list
                News newsEntry = new News();
                String date = news.get("date").asText();
                String title = news.get("title").get("rendered").asText();
                String content = Jsoup.parse(news.get("content").get("rendered").asText()).text();
                String image = news.get("yoast_head_json").get("og_image").get(0).get("url").asText();
                String link = news.get("_links").get("self").get(0).get("href").asText();

                // Set the values to the news entity
                newsEntry.setId(id);
                newsEntry.setDate(date);
                newsEntry.setTitle(title);
                newsEntry.setContent(content);
                newsEntry.setImage(image);
                newsEntry.setLink(link);

                // Add the news entry to the list that will be returned
                newsList.add(newsEntry);
            }
        }
        // Return the list of news entries that are new and not already in the database
        return newsList;
    }
}

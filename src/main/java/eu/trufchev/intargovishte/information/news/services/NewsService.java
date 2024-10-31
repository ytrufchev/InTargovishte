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
import java.util.stream.Collectors;

@Service
public class NewsService {
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    NewsClient newsClient;
    @Autowired
    ObjectMapper objectMapper;

    public List<News> getNewsUpdates() throws JsonProcessingException {
        String newsString = newsClient.getNews();
        List<News> newsList = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(newsString);
        for(JsonNode news : rootNode) {
            News newsEntry = new News();
            Long id = news.get("id").longValue();
            String date = news.get("date").asText();
            String title = news.get("title").get("rendered").asText();
            String content = Jsoup.parse(news.get("content").get("rendered").asText()).text().replaceAll(";", "");
            if (content.length() > 5000) {
                content = content.substring(0, 5000); // Limit to 15000 characters
            }
            String image = news.get("yoast_head_json").get("og_image").get(0).get("url").asText();
            String link = news.get("link").asText();
            if (!newsRepository.existsById(id)) {
                newsEntry.setId(id);
                newsEntry.setDate(date);
                newsEntry.setTitle(title);
                newsEntry.setContent(content);
                newsEntry.setImage(image);
                newsEntry.setLink(link);
                newsList.add(newsEntry);

                // Save the new news entry to the repository
                newsRepository.save(newsEntry);
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
}
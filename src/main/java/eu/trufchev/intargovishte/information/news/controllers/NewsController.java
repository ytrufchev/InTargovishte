package eu.trufchev.intargovishte.information.news.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.trufchev.intargovishte.information.news.entities.News;
import eu.trufchev.intargovishte.information.news.repositories.NewsRepository;
import eu.trufchev.intargovishte.information.news.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/information/news")
@RestController
public class NewsController {
    @Autowired
    NewsService newsService;
    @Autowired
    NewsRepository newsRepository;

    @Scheduled(cron = "0 0 */2 * * ?", zone = "GMT+3")
    public ResponseEntity<List<News>> cronUpdate() throws JsonProcessingException{
        return updateNews();
    }

    @GetMapping("/update")
    public ResponseEntity<List<News>> manualUpdate() throws JsonProcessingException{
        List<News> news = new ArrayList<>();
        newsService.getNewsUpdates().forEach(news::add);
        newsRepository.saveAll(news);
        return ResponseEntity.ok(news);
    }

    public ResponseEntity<List<News>> updateNews() throws JsonProcessingException {
        List<News> news = new ArrayList<>();
        newsService.getNewsUpdates().forEach(news::add);
        newsRepository.saveAll(news);
        return ResponseEntity.ok(news);
    }
    @GetMapping("/latest")
    public ResponseEntity<List<News>> getTheLatestNews() {
        List<News> latestNews = newsService.getLatestNews();
        return ResponseEntity.ok(latestNews);
    }

    @GetMapping("/all")
    public ResponseEntity<List<News>> getAllNews() {
        List<News> news = newsService.getAllNews();
        return ResponseEntity.ok(news);
    }
}
package eu.trufchev.intargovishte.information.news.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.trufchev.intargovishte.information.news.entities.News;
import eu.trufchev.intargovishte.information.news.repositories.NewsRepository;
import eu.trufchev.intargovishte.information.news.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("information/news")
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
        return updateNews();
    }

    public ResponseEntity<List<News>> updateNews() throws JsonProcessingException {
        List<News> news = new ArrayList<>();
        newsService.getNewsUpdates().forEach(news::add);
        newsRepository.deleteAll();
        newsRepository.saveAll(news);
        return ResponseEntity.ok(news);
    }
    @GetMapping("/all")
    public ResponseEntity<List<News>> getAllEnergyOutages(){
        List<News> news = new ArrayList<>();
        newsRepository.findAll().forEach(news::add);
        return ResponseEntity.ok(news);
    }
    @Scheduled(cron = "0 0 6 * * ?", zone = "GMT+3")
    public void updateNewsVoid() throws JsonProcessingException {
        List<News> news = new ArrayList<>();
        newsService.getNewsUpdates().forEach(news::add);
        newsRepository.deleteAll();
        newsRepository.saveAll(news);
    }
}
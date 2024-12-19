package eu.trufchev.intargovishte.information.events.puppetTheatre.services;

import eu.trufchev.intargovishte.information.events.puppetTheatre.PuppetTheaterDTO;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.information.events.puppetTheatre.feignClients.PuppetTheaterClient;
import eu.trufchev.intargovishte.information.events.puppetTheatre.repositories.PuppetTheaterLikeRepository;
import eu.trufchev.intargovishte.information.events.puppetTheatre.repositories.PuppetTheaterRepository;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PuppetTheaterService {

    @Autowired
    PuppetTheaterClient puppetTheaterClient;
    @Autowired
    PuppetTheaterRepository puppetTheaterRepository;
    @Autowired
    PuppetTheaterLikeRepository puppetTheaterLikeRepository;
    @Autowired
    UserRepository userRepository;

    public List<PuppetTheater> getTheaterEvents(){
        String html = puppetTheaterClient.getPuppetShows();
        Document doc = Jsoup.parse(html);
        Elements eventElements = doc.select(".fat-event-item");
        List<PuppetTheater> events = new ArrayList<>();
        for (Element eventElement : eventElements) {
            PuppetTheater event = new PuppetTheater();
            String imageUrl = eventElement.select(".fat-event-thumb img").attr("src");
            String eventMonth = eventElement.select(".fat-event-start .event-month").text().trim();
            String eventDay = eventElement.select(".fat-event-start .event-day").text().trim();
            String title = eventElement.select(".fat-event-title a").text().trim();
            String playTime = eventElement.select(".fat-event-meta-time span").text().trim();
            event.setImageUrl(imageUrl);
            event.setEventMonth(eventMonth);
            event.setEventDay(eventDay);
            event.setTitle(title);
            event.setPlayTime(playTime);
            events.add(event);
        }

        return events;
    }
    public List<PuppetTheaterDTO> PuppetToDTO(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (authentication != null) {
            currentUser = userRepository.findByUsernameOrEmail(authentication.getName(), authentication.getName())
                    .orElse(null);
        }

        Long today = System.currentTimeMillis();
        List<PuppetTheater> allEvents = new ArrayList<>();
        puppetTheaterRepository.findAll().forEach(allEvents::add);
        List<PuppetTheaterDTO> responseDTO = new ArrayList<>();
        for(PuppetTheater event: allEvents){
            PuppetTheaterDTO dto = new PuppetTheaterDTO();
            dto.setImageUrl(event.getImageUrl());
            dto.setEventMonth(event.getEventMonth());
            dto.setEventDay(event.getEventDay());
            dto.setTitle(event.getTitle());
            dto.setPlayTime(event.getPlayTime());
            dto.setLikesCount(event.getLikes() != null ? (long) event.getLikes().size() : 0L);
            // Check if current user has liked this event
            if (currentUser != null) {
                boolean isLikedByCurrentUser = puppetTheaterLikeRepository
                        .findByEventAndUser(event, currentUser)
                        .isPresent();
                dto.setLikedByCurrentUser(isLikedByCurrentUser);
            } else {
                dto.setLikedByCurrentUser(false);
            }
            responseDTO.add(dto);
        }
        return responseDTO;
    }
}

package eu.trufchev.intargovishte.information.events.puppetTheatre.services;

import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.information.events.puppetTheatre.feignClients.PuppetTheaterClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PuppetTheaterService {

    @Autowired
    PuppetTheaterClient puppetTheaterClient;
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
}

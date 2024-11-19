package eu.trufchev.intargovishte.information.events.appEvents.services;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.EventEntityRepository;
import eu.trufchev.intargovishte.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventAppService {
    @Autowired
    private EventEntityRepository eventEntityRepository;

    public List<EventEntity> getEvents() {
        List<EventEntity> events = new ArrayList<>();
        eventEntityRepository.findAll().forEach(events::add);
        return events;
    }

    public EventEntity addEvent(String title, String content, Long date, String location, String image, Long user, StatusENUMS status) {
        EventEntity event = new EventEntity();
        event.setTitle(title);
        event.setContent(content);
        event.setLocation(location);
        event.setImage(image);
        event.setUser(user);
        event.setStatus(status);

        // Set the event date directly from the incoming date
        event.setDate(date);

        // Save and return the event
        return eventEntityRepository.save(event);
    }
    public List<EventEntity> findNextTenApprovedEvents() {
        Long today = System.currentTimeMillis();
        List<EventEntity> events = new ArrayList<>();
        eventEntityRepository.findNextTenApprovedEvents(today, StatusENUMS.APPROVED).forEach(events::add);
        return events;
    }
    public List<EventEntity> findByStatus(StatusENUMS status) {
        List<EventEntity> events = eventEntityRepository.findByStatus(status); // Use a repository method to filter by status
        return events;
    }
}

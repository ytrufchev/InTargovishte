package eu.trufchev.intargovishte.information.events.appEvents.services;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.EventEntityRepository;
import eu.trufchev.intargovishte.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public EventEntity addEvent(String title, String content, Date date, String time, String location, String image, User user) {
        EventEntity event = new EventEntity();
        event.setTitle(title);
        event.setContent(content);
        event.setLocation(location);
        event.setImage(image);
        event.setUser(user);

        // Convert `Date` to `LocalDate`
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Parse `time` string to `LocalTime` (assuming `time` is in HH:mm format)
        LocalTime localTime = LocalTime.parse(time);

        // Combine `LocalDate` and `LocalTime` to `LocalDateTime`
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        // Convert `LocalDateTime` to `Timestamp`
        Timestamp timestamp = Timestamp.valueOf(localDateTime);

        // Set the combined timestamp as the event date
        event.setDate(timestamp.toInstant());

        // Save and return the event
        return eventEntityRepository.save(event);
    }
}

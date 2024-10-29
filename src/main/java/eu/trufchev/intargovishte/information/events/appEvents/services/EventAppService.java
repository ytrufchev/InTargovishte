package eu.trufchev.intargovishte.information.events.appEvents.services;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.EventEntityRepository;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventAppService {
    @Autowired
    private EventEntityRepository eventEntityRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<EventEntity>> getEvents() {
        List<EventEntity> appEvents = new ArrayList<>();
        eventEntityRepository.findAll().forEach(appEvents::add);
        return ResponseEntity.ok(appEvents);
    }

    public EventEntity addEvent(EventEntity eventEntity) {
        EventEntity event = new EventEntity();
        event.setTitle("");
        event.setContent("");
        event.setTitle(eventEntity.getTitle());
        event.setContent(eventEntity.getContent());
        event.setDate(eventEntity.getDate());
        event.setImage(eventEntity.getImage());

        // Find the user and set it if present
        Optional<User> user = userRepository.findById(eventEntity.getUser().getId());
        user.ifPresent(event::setUser);

        // Save the event and return it
        return eventEntityRepository.save(event);
    }
}

package eu.trufchev.intargovishte.information.events.appEvents;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.services.EventAppService;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/content/inapp/events")
public class EventEntityController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventAppService eventAppService;

    @PostMapping("/add")
    public ResponseEntity<?> addEvent(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Date date,
            @RequestParam String time,
            @RequestParam String location,
            @RequestParam String image,
            @RequestParam Long userId) {

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID is required.");
        }

        // Retrieve the user by ID
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Pass individual fields to the service to create the event
        EventEntity createdEvent = eventAppService.addEvent(title, content, date, time, location, image, user);
        return ResponseEntity.ok(createdEvent);
    }

    // GetMapping to retrieve all events
    @GetMapping
    public ResponseEntity<List<EventEntity>> getAllEvents() {
        List<EventEntity> events = eventAppService.getEvents();
        return ResponseEntity.ok(events);
    }
}

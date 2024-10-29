package eu.trufchev.intargovishte.information.events.appEvents;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.services.EventAppService;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/content/inapp/events")
public class EventEntityController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EventAppService eventAppService;

    @PostMapping("/add")
    public ResponseEntity<?> addEvent(@RequestBody EventEntity eventForPosting) {
        if (eventForPosting.getUser() == null || eventForPosting.getUser().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID is required.");
        }

        EventEntity createdEvent = eventAppService.addEvent(eventForPosting);
        if (createdEvent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return ResponseEntity.ok(createdEvent);
    }
}

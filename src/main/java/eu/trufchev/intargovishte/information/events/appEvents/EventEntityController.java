package eu.trufchev.intargovishte.information.events.appEvents;

import eu.trufchev.intargovishte.information.events.appEvents.dto.EventDTO;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.EventEntityRepository;
import eu.trufchev.intargovishte.information.events.appEvents.services.EventAppService;
import eu.trufchev.intargovishte.security.CustomUserDetailsService;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/content/inapp/events")
public class EventEntityController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventAppService eventAppService;
    @Autowired
    private EventEntityRepository eventEntityRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/add")
    public ResponseEntity<?> addEvent(@RequestBody EventDTO eventDTO) {
        if (eventDTO.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID is required.");
        }
        // Retrieve the user by ID
        User user = userRepository.findById(eventDTO.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Create the event using the DTO
        EventEntity createdEvent = eventAppService.addEvent(
                eventDTO.getTitle(),
                eventDTO.getContent(),
                eventDTO.getDate(), // Now just get the date from DTO
                eventDTO.getLocation(),
                eventDTO.getImage(),
                eventDTO.getUserId()
        );

        return ResponseEntity.ok(createdEvent);
    }

    // GetMapping to retrieve all events
    @GetMapping("/all")
    public ResponseEntity<List<EventEntity>> getAllEvents() {
        List<EventEntity> events = eventAppService.getEvents();
        return ResponseEntity.ok(events);
    }
    @GetMapping("/topten")
    public ResponseEntity<List<EventEntity>> getTopEvents() {
        List<EventEntity> events = eventAppService.getTopEvents();
        // Safely get up to 10 elements
        List<EventEntity> topTenEvents = events.size() > 10 ? events.subList(0, 10) : events;
        return ResponseEntity.ok(topTenEvents);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanUpOldEvents() {
        LocalDateTime yesterday = LocalDate.now().minusDays(1).atStartOfDay();
        String yesterdayTimestamp = yesterday.format(formatter);

        List<EventEntity> oldEvents = eventEntityRepository.findEventsBefore(yesterdayTimestamp);

        if (!oldEvents.isEmpty()) {
            eventEntityRepository.deleteAll(oldEvents);
            System.out.println("Deleted " + oldEvents.size() + " outdated events");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventEntity>> getEventsByUser(@PathVariable Long userId) {
        List<EventEntity> events = eventEntityRepository.findByUser(userId); // Ensure this method exists in your repository
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no events found
        }
        return ResponseEntity.ok(events); // 200 OK with the list of events
    }

    @DeleteMapping("/delete/{eventId}")
    public void deleteEvent(@PathVariable("eventId")Long eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Use userDetails.getUsername() to identify the user without casting
            String username = userDetails.getUsername();

            // Optional: Fetch custom User entity if needed
            User user = userRepository.findByUsername(username);

            Optional<EventEntity> eventForDeletion = eventEntityRepository.findById(eventId);
            if(eventForDeletion.isPresent()){
                EventEntity ev = eventForDeletion.get();
                if(ev.getUser().equals(user.getId())){
                    eventEntityRepository.delete(ev);
                }

            }
        } else {
            throw new AccessDeniedException("User not authenticated");
        }
    }




}

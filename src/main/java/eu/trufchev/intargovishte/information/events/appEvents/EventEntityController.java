package eu.trufchev.intargovishte.information.events.appEvents;

import eu.trufchev.intargovishte.information.events.appEvents.dto.EventDTO;
import eu.trufchev.intargovishte.information.events.appEvents.dto.ResponseEventDTO;
import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.events.appEvents.notifications.TelegramNotifier;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.AppEventLikeRepository;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.EventEntityRepository;
import eu.trufchev.intargovishte.information.events.appEvents.services.AppEventLikeService;
import eu.trufchev.intargovishte.information.events.appEvents.services.EventAppService;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private AppEventLikeRepository appEventLikeRepository;
    @Autowired
    private AppEventLikeService appEventLikeService;
    @Autowired
    TelegramNotifier telegramNotifier;

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

        List<AppEventLike> likes = new ArrayList<>();
        // Create the event using the DTO
        EventEntity createdEvent = eventAppService.addEvent(
                eventDTO.getTitle(),
                eventDTO.getContent(),
                eventDTO.getDate(), // Now just get the date from DTO
                eventDTO.getLocation(),
                eventDTO.getImage(),
                eventDTO.getUserId(),
                StatusENUMS.PENDING,
                likes

        );
        telegramNotifier.sendNotification("Ново събитие " + createdEvent.getTitle());
        return ResponseEntity.ok(createdEvent);
    }

    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @RequestBody Map<String, Long> requestBody,
            Authentication authentication
    ) {
        // Log entry into the method
        System.out.println("Called toggleLike");

        // Validate authentication
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No authentication information found"));
        }

        try {
            // Extract eventId from request body
            Long eventId = requestBody.get("eventId");
            if (eventId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Missing 'eventId' in request body"));
            }

            // Get authenticated user's username
            String username = authentication.getName();

            // Find user by username
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found: " + username));
            }

            // Retrieve the event
            EventEntity event = eventEntityRepository.findById(eventId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Event not found with ID: " + eventId));

            // Toggle the like
            boolean isLiked = appEventLikeService.toggleLike(event, user);
            // Return success response
            return ResponseEntity.ok(Map.of("liked", isLiked));
        } catch (Exception e) {
            System.err.println("Error in toggleLike method:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ResponseEventDTO>> getTopEvents() {
        List<ResponseEventDTO> events = eventAppService.findNextTenApprovedEvents();
        return ResponseEntity.ok(events);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanUpOldEvents() {
        Instant endOfYesterday = LocalDate.now().minusDays(1).atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
        String yesterdayTimestamp = String.valueOf(endOfYesterday.getEpochSecond());

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
    public ResponseEntity<Void> deleteEvent(@PathVariable("eventId") Long eventId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new AccessDeniedException("User not authenticated.");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        try {
            eventAppService.deleteEvent(eventId, username);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred during event deletion.", e);
        }
    }

    @PutMapping("/edit/{eventId}")
    public ResponseEntity<EventEntity> editEvent(
            @PathVariable("eventId") Long eventId,
            @RequestBody EventDTO eventDTO,
            Authentication authentication) {

        Optional<EventEntity> eventForEdit = eventEntityRepository.findById(eventId);

        if (eventForEdit.isPresent()) {
            EventEntity event = eventForEdit.get();

            // Get the authenticated user's ID
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String authenticatedUser = userDetails.getUsername(); // Assuming username stores the user ID
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(authenticatedUser));

            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // User not found
            }

            User user = optionalUser.get();
            // Check if the authenticated user is the publisher of the event
            if (!event.getUser().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update event fields
            event.setTitle(eventDTO.getTitle());
            event.setContent(eventDTO.getContent());
            event.setDate(eventDTO.getDate());
            event.setLocation(eventDTO.getLocation());
            event.setImage(eventDTO.getImage());
            event.setStatus(StatusENUMS.PENDING);

            eventEntityRepository.save(event);
            telegramNotifier.sendNotification("Редакция: " + event.getTitle());
            return ResponseEntity.ok(event);
        }

        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{eventId}")
    public ResponseEntity<EventEntity> getEventsById(@PathVariable Long eventId) {
        Optional<EventEntity> event = eventEntityRepository.findById(eventId); // Ensure this method exists in your repository
        if (event.isPresent()) {
            EventEntity ev = event.get();
            return ResponseEntity.ok(ev);
             // 204 No Content if no events found
        }
        return ResponseEntity.noContent().build(); // 200 OK with the list of events
    }
}

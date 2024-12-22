package eu.trufchev.intargovishte.information.events.puppetTheatre.controllers;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.puppetTheatre.PuppetTheaterDTO;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheaterLike;
import eu.trufchev.intargovishte.information.events.puppetTheatre.repositories.PuppetTheaterLikeRepository;
import eu.trufchev.intargovishte.information.events.puppetTheatre.repositories.PuppetTheaterRepository;
import eu.trufchev.intargovishte.information.events.puppetTheatre.services.PuppetTheaterLikeService;
import eu.trufchev.intargovishte.information.events.puppetTheatre.services.PuppetTheaterService;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/content/puppettheater")
public class PuppetTheaterController {

    @Autowired
    private PuppetTheaterService puppetTheaterService;
    @Autowired
    private PuppetTheaterRepository puppetTheaterRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PuppetTheaterLikeService puppetTheaterLikeService;
    @Autowired
    PuppetTheaterLike puppetTheaterLike;
    @Autowired
    PuppetTheaterLikeRepository puppetTheaterLikeRepository;


    @Scheduled(cron = "0 0 9 * * *")
    public List<PuppetTheater> cronUpdate() throws IOException{
        return updatePuppetTheaterEvents();
    }

    @GetMapping("/update")
    public List<PuppetTheater> manualUpdate() throws IOException{
        return updatePuppetTheaterEvents();
    }

    public List<PuppetTheater> updatePuppetTheaterEvents() throws IOException {
        // Get new events from service
        List<PuppetTheater> newEvents = puppetTheaterService.getTheaterEvents();

        // Get existing events from repository
        List<PuppetTheater> existingEvents = new ArrayList<>();
        puppetTheaterRepository.findAll().forEach(existingEvents::add);

        // Filter out events that already exist
        List<PuppetTheater> eventsToAdd = newEvents.stream()
                .filter(newEvent -> !existingEvents.stream()
                        .anyMatch(existingEvent -> areEventsEqual(newEvent, existingEvent)))
                .collect(Collectors.toList());

        // Save only new events
        if (!eventsToAdd.isEmpty()) {
            puppetTheaterRepository.saveAll(eventsToAdd);
        }
        List<PuppetTheater> eventsToReturn = new ArrayList<>();
        puppetTheaterRepository.findAll().forEach(eventsToReturn::add);
        return eventsToReturn;
    }

    // Helper method to compare events
    private boolean areEventsEqual(PuppetTheater event1, PuppetTheater event2) {
        // Implement comparison logic based on your event properties
        // For example:
        return event1.getTitle().equals(event2.getTitle()) &&
                event1.getEventDay().equals(event2.getEventDay());
        // Add more fields as needed for comparison
    }
    @GetMapping("/all")
    public List<PuppetTheaterDTO> allPuppetTheaterEvents(){
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        List<PuppetTheaterDTO> allEvents = puppetTheaterService.PuppetToDTO();
        // Create formatter for Bulgarian locale
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", new Locale("bg"));

        return allEvents.stream()
                .filter(event -> {
                    try {
                        // Parse the event date using Bulgarian locale
                        String dateStr = String.format("%s %s %d",
                                event.getEventMonth(),
                                event.getEventDay(),
                                currentYear);
                        LocalDate eventDate = LocalDate.parse(dateStr, formatter);

                        // If the event date is in the past for this year, assume it's for next year
                        if (eventDate.isBefore(currentDate)) {
                            eventDate = eventDate.plusYears(1);
                        }

                        // Return true if the event is today or in the future
                        return !eventDate.isBefore(currentDate);
                    } catch (Exception e) {
                        // Log the error if needed
                        // logger.error("Error parsing date for event: " + event.getId(), e);
                        return false;
                    }
                })
                .collect(Collectors.toList());
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
            PuppetTheater event = puppetTheaterRepository.findById(eventId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Event not found with ID: " + eventId));

            // Toggle the like
            boolean isLiked = puppetTheaterLikeService.toggleLike(event, user);
            // Return success response
            return ResponseEntity.ok(Map.of("liked", isLiked));
        } catch (Exception e) {
            System.err.println("Error in toggleLike method:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }
}

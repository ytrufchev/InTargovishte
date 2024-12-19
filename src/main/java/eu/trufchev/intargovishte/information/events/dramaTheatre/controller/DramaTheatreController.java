package eu.trufchev.intargovishte.information.events.dramaTheatre.controller;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Play;
import eu.trufchev.intargovishte.information.events.dramaTheatre.feignClient.CookieClient;
import eu.trufchev.intargovishte.information.events.dramaTheatre.repository.PlaysRepository;
import eu.trufchev.intargovishte.information.events.dramaTheatre.service.DramaLikeService;
import eu.trufchev.intargovishte.information.events.dramaTheatre.service.GetPlaysResponse;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RequestMapping("/content/plays")
@RestController
public class DramaTheatreController {
    @Autowired
    private PlaysRepository playsRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DramaLikeService dramaLikeService;

    @Autowired
    private CookieClient playClient;

    @Autowired
    GetPlaysResponse getPlaysResponse;

    @Scheduled(cron = "0 0 9 * * *")
    public  ResponseEntity<List<Play>> cronUpdate(){
        return upcomingPlays();
    }

    @GetMapping("/update")
    public ResponseEntity<List<Play>> manualUpdate(){
        return upcomingPlays();
    }

    public ResponseEntity<List<Play>> upcomingPlays() {
        List<Play> plays = getPlaysResponse.getPlaysForTargovishte();

        playsRepository.deleteAll();  // Deletes all plays in the DB
        playsRepository.saveAll(plays);  // Save new plays to the DB
        return ResponseEntity.ok(plays);
    }
    @GetMapping("/all")
    public List<Play> playsList() {
        return (List<Play>) playsRepository.findAll();
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
            Play event = playsRepository.findById(eventId.toString())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Event not found with ID: " + eventId));

            // Toggle the like
            boolean isLiked = dramaLikeService.toggleLike(event, user);
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
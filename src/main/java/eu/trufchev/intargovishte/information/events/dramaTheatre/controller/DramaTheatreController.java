package eu.trufchev.intargovishte.information.events.dramaTheatre.controller;

import eu.trufchev.intargovishte.information.events.dramaTheatre.dto.PlayDTO;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Play;
import eu.trufchev.intargovishte.information.events.dramaTheatre.feignClient.CookieClient;
import eu.trufchev.intargovishte.information.events.dramaTheatre.repository.PlaysLikeRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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

    @Autowired
    PlaysLikeRepository playsLikeRepository;

    @Scheduled(cron = "0 0 9 * * *")
    public  ResponseEntity<List<Play>> cronUpdate(){
        return upcomingPlays();
    }

    @GetMapping("/update")
    public ResponseEntity<List<Play>> manualUpdate(){
        return upcomingPlays();
    }

    public ResponseEntity<List<Play>> upcomingPlays() {
        List<Play> newPlays = getPlaysResponse.getPlaysForTargovishte();
        List<Play> existingPlays = new ArrayList<>();
                playsRepository.findAll().forEach(existingPlays::add);

        // Map existing plays by a unique identifier (e.g., title + date)
        Map<String, Play> existingPlaysMap = new HashMap<>();
        for (Play play : existingPlays) {
            existingPlaysMap.put(play.getTitle() + play.getStartDates(), play);
        }

        // Track updated list and found keys
        List<Play> finalPlayList = new ArrayList<>();
        Set<String> newKeys = new HashSet<>();

        for (Play newPlay : newPlays) {
            String key = newPlay.getTitle() + newPlay.getStartDates(); // unique key
            newKeys.add(key);

            if (existingPlaysMap.containsKey(key)) {
                Play existingPlay = existingPlaysMap.get(key);
                // Preserve likes
                newPlay.setId(existingPlay.getId());
                newPlay.setLikes(existingPlay.getLikes());
            }

            finalPlayList.add(newPlay);
        }

        // Delete plays not present in the new list
        for (Play existingPlay : existingPlays) {
            String key = existingPlay.getTitle() + existingPlay.getStartDates();
            if (!newKeys.contains(key)) {
                // Delete likes first if needed (depends on cascade config)
                dramaLikeService.deleteLikesForPlay(existingPlay);
                playsRepository.delete(existingPlay);
            }
        }

        playsRepository.saveAll(finalPlayList);

        return ResponseEntity.ok(finalPlayList);
    }

    @GetMapping("/all")
    public List<PlayDTO> playsList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        if (authentication != null) {
            currentUser = userRepository.findByUsernameOrEmail(authentication.getName(), authentication.getName())
                    .orElse(null);
        }
        List<PlayDTO> playDTOS = new ArrayList<>();
        List<Play> plays = new ArrayList<>();
        plays.addAll((Collection<? extends Play>) playsRepository.findAll());
        for(Play play: plays){
            PlayDTO playDTO = new PlayDTO();
            playDTO.setId(play.getId());
            playDTO.setTitle(play.getTitle());
            playDTO.setLength(play.getLength());
            playDTO.setMinAgeRestriction(play.getMinAgeRestriction());
            playDTO.setLargePhoto(play.getLargePhoto());
            playDTO.setPlaceName(play.getPlaceName());
            playDTO.setStartDates(play.getStartDates());
            playDTO.setLikesCount(play.getLikes() != null ? (long) play.getLikes().size() : 0L);
            // Check if current user has liked this event
            if (currentUser != null) {
                boolean isLikedByCurrentUser = playsLikeRepository
                        .findByEventAndUser(play, currentUser)
                        .isPresent();
                playDTO.setLikedByCurrentUser(isLikedByCurrentUser);
            } else {
                playDTO.setLikedByCurrentUser(false);
            }
            playDTOS.add(playDTO);
        }
        return playDTOS;
    }

    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @RequestBody Map<String, String> requestBody,
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
            String eventId = requestBody.get("eventId");
            if (eventId.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Missing or empty 'eventId' in request body"));
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
            Play event = playsRepository.findById(eventId)
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
package eu.trufchev.intargovishte.information.events.cinemagic.controllers;

import eu.trufchev.intargovishte.information.events.cinemagic.dto.MovieWithProjectionsDTO;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.MovieWithProjections;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Projections;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.ProjectionRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.services.MovieLikeService;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RequestMapping("content/movies")
@RestController
public class moviesWithProjectionController {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    ProjectionRepository projectionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MovieLikeService movieLikeService;

    @GetMapping("/all")
    public ResponseEntity<List<MovieWithProjections>> screenings(){
        List<Movie> movies = (List<Movie>) movieRepository.findAll();
        List<Projections> projections = (List<Projections>) projectionRepository.findAll();
        MovieWithProjectionsDTO movieWithProjectionsDTO = new MovieWithProjectionsDTO();
    List<MovieWithProjections> movieWithProjectionsList = movieWithProjectionsDTO.combineMovieWithProjections(movies, projections);
    return ResponseEntity.ok(movieWithProjectionsList);
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
            Movie event = movieRepository.findById(eventId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Event not found with ID: " + eventId));

            // Toggle the like
            boolean isLiked = movieLikeService.toggleLike(event, user);
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

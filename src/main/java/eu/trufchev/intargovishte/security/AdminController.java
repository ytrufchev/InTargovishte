package eu.trufchev.intargovishte.security;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.EventEntityRepository;
import eu.trufchev.intargovishte.information.fuelo.entities.GasStation;
import eu.trufchev.intargovishte.information.fuelo.entities.GasstationsList;
import eu.trufchev.intargovishte.information.fuelo.feignclient.FueloClient;
import eu.trufchev.intargovishte.information.fuelo.repository.GasStationRepository;
import eu.trufchev.intargovishte.information.fuelo.services.ParseGasStationToHtml;
import eu.trufchev.intargovishte.information.news.entities.News;
import eu.trufchev.intargovishte.information.news.repositories.NewsRepository;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/superadmin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private GasStationRepository gasStationRepository;
    @Autowired
    private ParseGasStationToHtml parseGasStationToHtml;
    @Autowired
    private FueloClient fueloClient;
    @Autowired
    private EventEntityRepository eventEntityRepository;
    @Autowired
    private EntityManager entityManager;

    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/deleteuser/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        Optional<User> userForDeletion = userRepository.findById(userId);
        if (userForDeletion.isPresent()) {
           User user = userForDeletion.get();
            entityManager.detach(user);
            userRepository.deleteById(user.getId());
            return "User with id " + userId + " deleted";
        } else {
            return "User with id " + userId + " not found";
        }
    }
    @DeleteMapping("/deletenews/{newsId}")
    public String deleteNewsEntry(@PathVariable Long newsId) {
        Optional<News> newsForDeletion = newsRepository.findById(newsId);
        if (newsForDeletion.isPresent()) {
            newsRepository.deleteById(newsId);
            return "News with id " + newsId + " deleted";
        } else {
            return "News with id " + newsId + " not found";
        }
    }

    @DeleteMapping("/deleteinappevent/{eventId}")
    public String deleteInAppEventEntry(@PathVariable Long eventId) {
        Optional<EventEntity> eventForDeletion = eventEntityRepository.findById(eventId);
        if (eventForDeletion.isPresent()) {
            eventEntityRepository.deleteById(eventId);
            return "Event with id " + eventId + " deleted";
        } else {
            return "Event with id " + eventId + " not found";
        }
    }

    @GetMapping("/fuel/update")
    public ResponseEntity<List<GasStation>> manualUpdate() {
        return updateGasStations();
    }

    public ResponseEntity<List<GasStation>> updateGasStations() {
        GasstationsList gasstationsLists = new GasstationsList();
        List<GasStation> gasStations = new ArrayList<>();

        // Iterate through all gas station IDs
        for (String gasStationId : gasstationsLists.getGasstations()) {
            try {
                // Fetch gas station details from the external source
                String htmlResponse = fueloClient.getGasstationDetails(gasStationId, "bg");
                // Parse the HTML response to create a GasStation object
                GasStation gasStation = parseGasStationToHtml.parseGasStationHtml(htmlResponse);
                gasStations.add(gasStation);
            } catch (Exception e) {
                // Log the error (you can use a logging framework)
                System.err.println("Error processing gas station ID " + gasStationId + ": " + e.getMessage());
            }
        }

        // Update the database
        gasStationRepository.deleteAll(); // Clear existing entries
        gasStationRepository.saveAll(gasStations); // Save the new list

        return ResponseEntity.ok(gasStations);
    }




}

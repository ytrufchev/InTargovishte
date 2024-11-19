package eu.trufchev.intargovishte.security;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.EventEntityRepository;
import eu.trufchev.intargovishte.information.fuelo.entities.GasStation;
import eu.trufchev.intargovishte.information.fuelo.entities.GasstationsList;
import eu.trufchev.intargovishte.information.fuelo.feignclient.FueloClient;
import eu.trufchev.intargovishte.information.fuelo.repository.GasStationRepository;
import eu.trufchev.intargovishte.information.fuelo.services.ParseGasStationToHtml;
import eu.trufchev.intargovishte.information.news.entities.News;
import eu.trufchev.intargovishte.information.news.repositories.NewsRepository;
import eu.trufchev.intargovishte.user.entity.Roles;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.RolesRepository;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import eu.trufchev.intargovishte.user.service.impl.UserServiceImpl;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private RolesRepository rolesRepository;
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/deleteuser/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        return userService.deleteUserById(userId);
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
    @GetMapping("/roles")
    public ResponseEntity<List<Roles>> getAllRoles(){
        List<Roles> roles = new ArrayList<>();
        rolesRepository.findAll().forEach(roles::add);
        return ResponseEntity.ok(roles);
    }
    @GetMapping("/events/pending")
    public ResponseEntity<List<EventEntity>> getPendingEvents() {
        List<EventEntity> events = eventEntityRepository.findByStatus(StatusENUMS.PENDING); // Use a repository method to filter by status
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no pending events found
        }
        return ResponseEntity.ok(events);
    }

    @PostMapping("/events/approve/{eventId}")
    public ResponseEntity<EventEntity> approveAnEvents(@PathVariable Long eventId) {
        Optional<EventEntity> event = eventEntityRepository.findById(eventId); // Use a repository method to filter by status
        if (event.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no pending events found
        }
        EventEntity approvedEvent = event.get();
        approvedEvent.setStatus(StatusENUMS.APPROVED);
        eventEntityRepository.save(approvedEvent);
        return ResponseEntity.ok(approvedEvent);
    }



}

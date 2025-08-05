package eu.trufchev.intargovishte.information.inAppInformation.controllers;

import eu.trufchev.intargovishte.information.events.appEvents.dto.EventDTO;
import eu.trufchev.intargovishte.information.events.appEvents.dto.ResponseEventDTO;
import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.events.appEvents.notifications.TelegramNotifier;
import eu.trufchev.intargovishte.information.inAppInformation.DTO.InfoDTO;
import eu.trufchev.intargovishte.information.inAppInformation.entities.Information;
import eu.trufchev.intargovishte.information.inAppInformation.services.InformationService;
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
import org.springframework.stereotype.Controller;
import eu.trufchev.intargovishte.information.inAppInformation.repositories.InformationRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/content/inapp/information")
public class InformationController {
    @Autowired
    InformationRepository informationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TelegramNotifier telegramNotifier;
    @Autowired
    InformationService informationService;

    @PostMapping("/add")
    public ResponseEntity<?> addInformation(@RequestBody InfoDTO infoDTO) {
        if (infoDTO.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID is required.");
        }
        // Retrieve the user by ID
        User user = userRepository.findById(infoDTO.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        // Create the information using the DTO
        Information createdInformation = informationService.addInformation(infoDTO);
        telegramNotifier.sendNotification("Нова информация " + createdInformation.getTitle());
        return ResponseEntity.ok(createdInformation);
    }
    @GetMapping("/approved")
    public ResponseEntity<List<InfoDTO>> getApprovedInfo() {
        List<InfoDTO> infoList = informationService.getApprovedInfo();
        return ResponseEntity.ok(infoList);
    }
    @DeleteMapping("/delete/{infoId}")
    public ResponseEntity<Void> deleteInfo(@PathVariable("infoId") Long infoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new AccessDeniedException("User not authenticated.");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        try {
            informationService.deleteInformation(infoId, username);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred during event deletion.", e);
        }
    }
    @PutMapping("/edit/{infoId}")
    public ResponseEntity<Information> editEvent(
            @PathVariable("infoId") Long eventId,
            @RequestBody InfoDTO infoDTO,
            Authentication authentication) {

        Optional<Information> infoForEdit = informationRepository.findById(eventId);

        if (infoForEdit.isPresent()) {
            Information info = infoForEdit.get();

            // Get the authenticated user's ID
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String authenticatedUser = userDetails.getUsername(); // Assuming username stores the user ID
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(authenticatedUser));

            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // User not found
            }

            User user = optionalUser.get();
            // Check if the authenticated user is the publisher of the event
            if (!info.getUserId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update event fields
            info.setTitle(infoDTO.getTitle());
            if(infoDTO.getDescription() != null) {
                info.setDescription(infoDTO.getDescription());
            }
            else {
                info.setDescription("");
            }
            info.setEndDate(infoDTO.getEndDate());
            if(infoDTO.getImage() != null) {
                info.setImage(infoDTO.getImage());
            }
            else{
                info.setImage("");
            }
            info.setStatus(StatusENUMS.PENDING);

            informationRepository.save(info);
            telegramNotifier.sendNotification("Редакция: " + info.getTitle());
            return ResponseEntity.ok(info);
        }

        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{infoId}")
    public ResponseEntity<Information> getInfoById(@PathVariable Long infoId) {
        Optional<Information> info = informationRepository.findById(infoId); // Ensure this method exists in your repository
        if (info.isPresent()) {
            Information inf = info.get();
            return ResponseEntity.ok(inf);
            // 204 No Content if no events found
        }
        return ResponseEntity.noContent().build(); // 200 OK with the list of events
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Information>> getInfoByUser(@PathVariable Long userId) {
        List<Information> info = informationRepository.findByUserId(userId); // Ensure this method exists in your repository
        if (info.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no events found
        }
        return ResponseEntity.ok(info); // 200 OK with the list of events
    }
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanUpOldInformation() {
        long endOfYesterdayEpochSeconds = LocalDate.now()
                .minusDays(1)
                .atTime(LocalTime.MAX)
                .toInstant(ZoneOffset.UTC)
                .getEpochSecond();

        List<Information> oldInformation = informationRepository.findInformationBeforeEndDate(endOfYesterdayEpochSeconds);

        if (!oldInformation.isEmpty()) {
            informationRepository.deleteAll(oldInformation);
            System.out.println("Deleted " + oldInformation.size() + " outdated events");
        }
    }
}

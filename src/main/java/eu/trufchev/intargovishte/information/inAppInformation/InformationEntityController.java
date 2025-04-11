package eu.trufchev.intargovishte.information.inAppInformation;

import eu.trufchev.intargovishte.information.events.appEvents.dto.EventDTO;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.events.appEvents.notifications.TelegramNotifier;
import eu.trufchev.intargovishte.information.inAppInformation.entity.Information;
import eu.trufchev.intargovishte.information.inAppInformation.notifications.InfoTelegramNotifier;
import eu.trufchev.intargovishte.information.inAppInformation.repositories.InformationEntityRepository;
import eu.trufchev.intargovishte.information.inAppInformation.services.InformationAppService;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/content/inapp/information")
public class InformationEntityController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InformationAppService informationAppService;
    @Autowired
    private InformationEntityRepository informationEntityRepository;
    @Autowired
    InfoTelegramNotifier infoTelegramNotifier;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/add")
    public ResponseEntity<?> addInfo(@RequestBody Information information) {
        if (information.getUser() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID is required.");
        }
        // Retrieve the user by ID
        User user = userRepository.findById(information.getUser()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        Information createdInformation = informationAppService.addInformation(information.getTitle(), information.getValidTo(), information.getDescription(), information.getUser(), StatusENUMS.PENDING);

        infoTelegramNotifier.sendNotification("Нова информация " + createdInformation.getTitle());
        return ResponseEntity.ok(createdInformation);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Information>> getTopInfos() {
        List<Information> infos = informationAppService.findNextTenApprovedInformation();
        return ResponseEntity.ok(infos);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanUpOldInfos() {
        Instant endOfYesterday = LocalDate.now().minusDays(1).atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
        String yesterdayTimestamp = String.valueOf(endOfYesterday.getEpochSecond());

        List<Information> oldInfos = informationEntityRepository.findInformationBefore(yesterdayTimestamp);

        if (!oldInfos.isEmpty()) {
            informationEntityRepository.deleteAll(oldInfos);
            System.out.println("Deleted " + oldInfos.size() + " outdated events");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Information>> getInfosByUser(@PathVariable Long userId) {
        List<Information> infos = informationEntityRepository.findByUser(userId); // Ensure this method exists in your repository
        if (infos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no events found
        }
        return ResponseEntity.ok(infos); // 200 OK with the list of events
    }

    @DeleteMapping("/delete/{infoId}")
    public void deleteInfo(@PathVariable("InfoId")Long infoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Use userDetails.getUsername() to identify the user without casting
            String username = userDetails.getUsername();

            // Optional: Fetch custom User entity if needed
            User user = userRepository.findByUsername(username);

            Optional<Information> infoForDeletion = informationEntityRepository.findById(infoId);
            if(infoForDeletion.isPresent()){
                Information ev = infoForDeletion.get();
                if(ev.getUser().equals(user.getId())){
                    informationEntityRepository.delete(ev);
                }

            }
        } else {
            throw new AccessDeniedException("User not authenticated");
        }
    }

    @PutMapping("/edit/{infoId}")
    public ResponseEntity<Information> editEvent(
            @PathVariable("infoId") Long infoId,
            @RequestBody Information information,
            Authentication authentication) {

        Optional<Information> infoForEdit = informationEntityRepository.findById(infoId);

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
            if (!info.getUser().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Update event fields
            info.setTitle(information.getTitle());
            info.setDescription(information.getDescription());
            info.setValidTo(information.getValidTo());
            info.setStatus(StatusENUMS.PENDING);

            informationEntityRepository.save(info);
            infoTelegramNotifier.sendNotification("Редакция: " + info.getTitle());
            return ResponseEntity.ok(info);
        }

        return ResponseEntity.notFound().build();
    }
    @GetMapping("/infoId}")
    public ResponseEntity<Information> getInfoById(@PathVariable Long infoId) {
        Optional<Information> info = informationEntityRepository.findById(infoId); // Ensure this method exists in your repository
        if (info.isPresent()) {
            Information ev = info.get();
            return ResponseEntity.ok(ev);
             // 204 No Content if no events found
        }
        return ResponseEntity.noContent().build(); // 200 OK with the list of events
    }
}

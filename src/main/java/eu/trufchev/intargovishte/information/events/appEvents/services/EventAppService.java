package eu.trufchev.intargovishte.information.events.appEvents.services;

import eu.trufchev.intargovishte.information.events.appEvents.dto.ResponseEventDTO;
import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.AppEventLikeRepository;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.EventEntityRepository;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Response;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventAppService {
    @Autowired
    private EventEntityRepository eventEntityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppEventLikeRepository appEventLikeRepository;

    public List<EventEntity> getEvents() {
        List<EventEntity> events = new ArrayList<>();
        eventEntityRepository.findAll().forEach(events::add);
        return events;
    }

    public EventEntity addEvent(String title, String content, Long date, String location, String image, Long user, StatusENUMS status, List<AppEventLike> likes) {
        EventEntity event = new EventEntity();
        event.setTitle(title);
        event.setContent(content);
        event.setLocation(location);
        event.setImage(image);
        event.setUser(user);
        event.setStatus(status);
        event.setLikes(likes);

        // Set the event date directly from the incoming date
        event.setDate(date);

        // Save and return the event
        return eventEntityRepository.save(event);
    }
    public List<ResponseEventDTO> findNextTenApprovedEvents() {
            // Get the current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = null;
            if (authentication != null) {
                currentUser = userRepository.findByUsernameOrEmail(authentication.getName(), authentication.getName())
                        .orElse(null);
            }

            Long today = System.currentTimeMillis();
            List<EventEntity> events = new ArrayList<>();
            eventEntityRepository.findNextTenApprovedEvents(today, StatusENUMS.APPROVED).forEach(events::add);

            List<ResponseEventDTO> eventDTO = new ArrayList<>();
            for(EventEntity event : events) {
                ResponseEventDTO responseEventDTO = new ResponseEventDTO();
                responseEventDTO.setId(event.getId());
                responseEventDTO.setTitle(event.getTitle());
                responseEventDTO.setContent(event.getContent());
                responseEventDTO.setImage(event.getImage());
                responseEventDTO.setDate(event.getDate());
                responseEventDTO.setLocation(event.getLocation());
                responseEventDTO.setUserId(event.getUser());

                // Count total likes
                responseEventDTO.setLikesCount(event.getLikes() != null ? (long) event.getLikes().size() : 0L);

                // Check if current user has liked this event
                if (currentUser != null) {
                    boolean isLikedByCurrentUser = appEventLikeRepository
                            .findByEventAndUser(event, currentUser)
                            .isPresent();
                    responseEventDTO.setLikedByCurrentUser(isLikedByCurrentUser);
                } else {
                    responseEventDTO.setLikedByCurrentUser(false);
                }

                eventDTO.add(responseEventDTO);
            }
            return eventDTO;
    }
    @Transactional
    public void deleteEvent(Long eventId, String requestingUsername) {
        User requestingUser = userRepository.findByUsername(requestingUsername);
        if (requestingUser == null) {
            throw new AccessDeniedException("Authenticated user not found in database.");
        }

        EventEntity eventForDeletion = eventEntityRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with ID: " + eventId));

        if (!eventForDeletion.getUser().equals(requestingUser.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this event.");
        }
        appEventLikeRepository.deleteByEvent(eventForDeletion);

        eventEntityRepository.delete(eventForDeletion);
    }
}

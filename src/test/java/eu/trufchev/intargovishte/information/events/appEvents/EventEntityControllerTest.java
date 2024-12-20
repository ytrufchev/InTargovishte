package eu.trufchev.intargovishte.information.events.appEvents;

import eu.trufchev.intargovishte.information.events.appEvents.dto.EventDTO;
import eu.trufchev.intargovishte.information.events.appEvents.dto.ResponseEventDTO;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.events.appEvents.notifications.TelegramNotifier;
import eu.trufchev.intargovishte.information.events.appEvents.services.EventAppService;
import eu.trufchev.intargovishte.information.events.appEvents.notifications.NotificationClient; // Import NotificationClient
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EventEntityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EventAppService eventAppService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationClient notificationClient; // Mock NotificationClient

    @Mock
    private TelegramNotifier notificationService;


    @InjectMocks
    private EventEntityController eventEntityController;

    private User user;
    private EventDTO eventDTO;
    private EventEntity eventEntity;
    private ResponseEventDTO responseEventDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventEntityController).build();

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        eventDTO = new EventDTO();
        eventDTO.setUserId(1L);
        eventDTO.setTitle("Event 1");
        eventDTO.setContent("Event content");
        eventDTO.setDate(Instant.now().toEpochMilli());
        eventDTO.setLocation("Targovishte");
        eventDTO.setImage("image_url");

        eventEntity = new EventEntity();
        eventEntity.setId(1L);
        eventEntity.setTitle("Event 1");
        eventEntity.setContent("Event content");
        eventEntity.setUser(1L);
        eventEntity.setDate(Instant.now().toEpochMilli());
        eventEntity.setLocation("Targovishte");
        eventEntity.setImage("image_url");
        eventEntity.setStatus(StatusENUMS.PENDING);

        responseEventDTO = new ResponseEventDTO();
        responseEventDTO.setUserId(1L);
        responseEventDTO.setTitle("Event 1");
        responseEventDTO.setContent("Event content");
        responseEventDTO.setDate(Instant.now().toEpochMilli());
        responseEventDTO.setLocation("Targovishte");
        responseEventDTO.setImage("image_url");
        responseEventDTO.setLikesCount(1L);
    }


    @Test
    void addEvent_ValidData_ReturnsCreatedEvent() throws Exception {
        // Mock the user repository
        when(userRepository.findById(eventDTO.getUserId())).thenReturn(Optional.of(user));
        // Mock the service to return a created event
        when(eventAppService.addEvent(anyString(), anyString(), anyLong(), anyString(), anyString(), anyLong(), any(), anyList())).thenReturn(eventEntity);
        // Mock the notification service to simulate success
        doNothing().when(notificationService).sendNotification(anyString());

        mockMvc.perform(post("/content/inapp/events/add")
                        .contentType("application/json")
                        .content("{\"userId\":1,\"title\":\"Event 1\",\"content\":\"Event content\",\"date\":1638900000000,\"location\":\"Targovishte\",\"image\":\"image_url\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Event 1"))
                .andExpect(jsonPath("$.content").value("Event content"));
    }


    @Test
    void addEvent_UserNotFound_ReturnsNotFound() throws Exception {
        // Mock the user repository to return empty (user not found)
        when(userRepository.findById(eventDTO.getUserId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/content/inapp/events/add")
                        .contentType("application/json")
                        .content("{\"userId\":1,\"title\":\"Event 1\",\"content\":\"Event content\",\"date\":1638900000000,\"location\":\"Targovishte\",\"image\":\"image_url\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found."));
    }

    @Test
    void getAllEvents_ReturnsApprovedEvents() throws Exception {
        // Mock the service to return a list of events
        when(eventAppService.findNextTenApprovedEvents()).thenReturn(List.of(responseEventDTO));

        mockMvc.perform(get("/content/inapp/events/approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Event 1"));
    }
}

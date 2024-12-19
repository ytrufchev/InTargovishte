package eu.trufchev.intargovishte.information.events.appEvents.services;

import eu.trufchev.intargovishte.information.events.appEvents.dto.ResponseEventDTO;
import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.EventEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class EventAppServiceTest {

    @Mock
    private EventEntityRepository eventEntityRepository;

    @InjectMocks
    private EventAppService eventAppService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEvents_ShouldReturnAllEvents() {
        // Arrange
        EventEntity event1 = new EventEntity();
        event1.setId(1L);
        event1.setTitle("Event 1");

        EventEntity event2 = new EventEntity();
        event2.setId(2L);
        event2.setTitle("Event 2");

        when(eventEntityRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        // Act
        List<EventEntity> events = eventAppService.getEvents();

        // Assert
        assertEquals(2, events.size());
        verify(eventEntityRepository, times(1)).findAll();
    }

    @Test
    void addEvent_ShouldSaveAndReturnEvent() {
        // Arrange
        List<AppEventLike> likes = new ArrayList<>();
        EventEntity event = new EventEntity();
        event.setId(1L);
        event.setTitle("New Event");
        event.setContent("This is a test event.");
        event.setDate(System.currentTimeMillis());
        event.setLocation("Targovishte");
        event.setImage("image.jpg");
        event.setUser(1L);
        event.setStatus(StatusENUMS.APPROVED);
        event.setLikes(likes);

        when(eventEntityRepository.save(any(EventEntity.class))).thenReturn(event);

        // Act
        EventEntity savedEvent = eventAppService.addEvent(
                "New Event",
                "This is a test event.",
                System.currentTimeMillis(),
                "Targovishte",
                "image.jpg",
                1L,
                StatusENUMS.APPROVED,
                likes
        );

        // Assert
        assertEquals("New Event", savedEvent.getTitle());
        verify(eventEntityRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void findNextTenApprovedEvents_ShouldReturnApprovedEvents() {
        // Arrange
        EventEntity event1 = new EventEntity();
        event1.setId(1L);
        event1.setTitle("Approved Event 1");
        event1.setStatus(StatusENUMS.APPROVED);

        EventEntity event2 = new EventEntity();
        event2.setId(2L);
        event2.setTitle("Approved Event 2");
        event2.setStatus(StatusENUMS.APPROVED);

        when(eventEntityRepository.findNextTenApprovedEvents(anyLong(), eq(StatusENUMS.APPROVED)))
                .thenReturn(Arrays.asList(event1, event2));

        // Act
        List<ResponseEventDTO> events = eventAppService.findNextTenApprovedEvents();

        // Assert
        assertEquals(2, events.size());
        verify(eventEntityRepository, times(1)).findNextTenApprovedEvents(anyLong(), eq(StatusENUMS.APPROVED));
    }

}

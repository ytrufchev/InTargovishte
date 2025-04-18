package eu.trufchev.intargovishte.security;

import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.AppEventLikeRepository;
import eu.trufchev.intargovishte.information.events.appEvents.repositories.EventEntityRepository;
import eu.trufchev.intargovishte.information.fuelo.entities.GasStation;
import eu.trufchev.intargovishte.information.fuelo.feignclient.FueloClient;
import eu.trufchev.intargovishte.information.fuelo.repository.GasStationRepository;
import eu.trufchev.intargovishte.information.fuelo.services.ParseGasStationToHtml;
import eu.trufchev.intargovishte.user.entity.Roles;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.RolesRepository;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import eu.trufchev.intargovishte.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GasStationRepository gasStationRepository;

    @Mock
    private ParseGasStationToHtml parseGasStationToHtml;

    @Mock
    private FueloClient fueloClient;

    @Mock
    private EventEntityRepository eventEntityRepository;

    @Mock
    private AppEventLikeRepository appEventLikeRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private UserServiceImpl userService;

    public AdminControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = List.of(new User());
        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = adminController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        when(userService.deleteUserById(userId)).thenReturn("User deleted");

        String response = adminController.deleteUser(userId);

        assertEquals("User deleted", response);
        verify(userService, times(1)).deleteUserById(userId);
    }

    @Test
    public void testDeleteInAppEventEntrySuccess() {
        Long eventId = 1L;

        // Mock EventEntity
        EventEntity mockEventEntity = new EventEntity();
        mockEventEntity.setId(eventId); // Set the eventId or any other required fields

        // Mock repository behavior
        when(eventEntityRepository.findById(eventId)).thenReturn(Optional.of(mockEventEntity));

        // Call the method under test
        String response = adminController.deleteInAppEventEntry(eventId);

        // Assertions
        assertEquals("Event with id 1 deleted", response);

        // Verify interactions
        verify(appEventLikeRepository, times(1)).deleteByEvent(mockEventEntity);
        verify(eventEntityRepository, times(1)).deleteById(eventId);
    }

    @Test
    public void testDeleteInAppEventEntryNotFound() {
        Long eventId = 1L;
        when(eventEntityRepository.findById(eventId)).thenReturn(Optional.empty());

        String response = adminController.deleteInAppEventEntry(eventId);

        assertEquals("Event with id 1 not found", response);
        verify(eventEntityRepository, never()).deleteById(eventId);
    }

    @Test
    public void testGetPendingEvents() {
        List<EventEntity> events = List.of(new EventEntity());
        when(eventEntityRepository.findByStatus(StatusENUMS.PENDING)).thenReturn(events);

        ResponseEntity<List<EventEntity>> response = adminController.getPendingEvents();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(events, response.getBody());
        verify(eventEntityRepository, times(1)).findByStatus(StatusENUMS.PENDING);
    }

    @Test
    public void testGetPendingEventsNoContent() {
        when(eventEntityRepository.findByStatus(StatusENUMS.PENDING)).thenReturn(new ArrayList<>());

        ResponseEntity<List<EventEntity>> response = adminController.getPendingEvents();

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(eventEntityRepository, times(1)).findByStatus(StatusENUMS.PENDING);
    }

    @Test
    public void testApproveAnEventSuccess() {
        Long eventId = 1L;
        EventEntity event = new EventEntity();
        when(eventEntityRepository.findById(eventId)).thenReturn(Optional.of(event));

        ResponseEntity<EventEntity> response = adminController.approveAnEvents(eventId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(StatusENUMS.APPROVED, event.getStatus());
        verify(eventEntityRepository, times(1)).save(event);
    }

    @Test
    public void testApproveAnEventNotFound() {
        Long eventId = 1L;
        when(eventEntityRepository.findById(eventId)).thenReturn(Optional.empty());

        ResponseEntity<EventEntity> response = adminController.approveAnEvents(eventId);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(eventEntityRepository, never()).save(any());
    }

    @Test
    public void testGetAllRoles() {
        List<Roles> roles = List.of(new Roles());
        when(rolesRepository.findAll()).thenReturn(roles);

        ResponseEntity<List<Roles>> response = adminController.getAllRoles();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(roles, response.getBody());
        verify(rolesRepository, times(1)).findAll();
    }
}

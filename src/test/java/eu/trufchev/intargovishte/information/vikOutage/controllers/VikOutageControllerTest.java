package eu.trufchev.intargovishte.information.vikOutage.controllers;

import eu.trufchev.intargovishte.information.vikOutage.entities.VikOutage;
import eu.trufchev.intargovishte.information.vikOutage.repositories.VikOutageRepository;
import eu.trufchev.intargovishte.information.vikOutage.services.VikOutageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VikOutageControllerTest {

    @InjectMocks
    private VikOutageController vikOutageController;

    @Mock
    private VikOutageService vikOutageService;

    @Mock
    private VikOutageRepository vikOutageRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTargovishteDetails() {
        // Arrange
        VikOutage vikOutage1 = new VikOutage();
        vikOutage1.setDescription("Outage 1");
        VikOutage vikOutage2 = new VikOutage();
        vikOutage2.setDescription("Outage 2");

        List<VikOutage> mockOutages = Arrays.asList(vikOutage1, vikOutage2);
        when(vikOutageService.fetchAndParseVikOutage()).thenReturn(mockOutages);

        // Act
        ResponseEntity<List<VikOutage>> response = vikOutageController.getTargovishteDetails();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(vikOutageRepository, times(1)).deleteAll();
        verify(vikOutageRepository, times(1)).saveAll(mockOutages);
    }

    @Test
    void testManualUpdate() {
        // Arrange
        VikOutage vikOutage = new VikOutage();
        vikOutage.setDescription("Manual Outage");
        when(vikOutageService.fetchAndParseVikOutage()).thenReturn(List.of(vikOutage));

        // Act
        ResponseEntity<List<VikOutage>> response = vikOutageController.manualUpdate();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Manual Outage", response.getBody().get(0).getDescription());
        verify(vikOutageRepository, times(1)).deleteAll();
        verify(vikOutageRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testGetOutages() {
        // Arrange
        VikOutage vikOutage1 = new VikOutage();
        vikOutage1.setDescription("Saved Outage 1");
        VikOutage vikOutage2 = new VikOutage();
        vikOutage2.setDescription("Saved Outage 2");

        when(vikOutageRepository.findAll()).thenReturn(Arrays.asList(vikOutage1, vikOutage2));

        // Act
        ResponseEntity<List<VikOutage>> response = vikOutageController.getOutages();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Saved Outage 1", response.getBody().get(0).getDescription());
        assertEquals("Saved Outage 2", response.getBody().get(1).getDescription());
        verify(vikOutageRepository, times(1)).findAll();
    }
}

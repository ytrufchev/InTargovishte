package eu.trufchev.intargovishte.information.energyOutage.controllers;

import eu.trufchev.intargovishte.information.energyOutage.entities.EnergyOutage;
import eu.trufchev.intargovishte.information.energyOutage.repositories.EnergyOutageRepository;
import eu.trufchev.intargovishte.information.energyOutage.services.EnergyOutageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnergyOutageControllerTest {

    @Mock
    private EnergyOutageService energyOutageService;

    @Mock
    private EnergyOutageRepository energyOutageRepository;

    @InjectMocks
    private EnergyOutageController energyOutageController;

    private List<EnergyOutage> mockEnergyOutages;

    @BeforeEach
    void setUp() {
        // Prepare mock data
        mockEnergyOutages = new ArrayList<>();
        EnergyOutage outage1 = new EnergyOutage();
        outage1.setId(1L);
        mockEnergyOutages.add(outage1);

        EnergyOutage outage2 = new EnergyOutage();
        outage2.setId(2L);
        mockEnergyOutages.add(outage2);
    }

    @Test
    void testGetAllEnergyOutages() {
        // Arrange
        when(energyOutageRepository.findAll()).thenReturn(mockEnergyOutages);

        // Act
        ResponseEntity<List<EnergyOutage>> response = energyOutageController.getAllEnergyOutages();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(energyOutageRepository, times(1)).findAll();
    }

    @Test
    void testUpdateEnergyOutages() {
        // Arrange
        String mockOutagesString = "mock outages data";
        when(energyOutageService.fetchInterruptions()).thenReturn(mockOutagesString);
        when(energyOutageService.updateEnergyOutages(mockOutagesString)).thenReturn(mockEnergyOutages);

        // Act
        ResponseEntity<List<EnergyOutage>> response = energyOutageController.updateEnergyOutages();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(2, response.getBody().size());

        // Verify interactions
        verify(energyOutageService, times(1)).fetchInterruptions();
        verify(energyOutageService, times(1)).updateEnergyOutages(mockOutagesString);
    }

    @Test
    void testManualUpdateEnergyOutages() {
        // Arrange
        String mockOutagesString = "manual update outages data";
        when(energyOutageService.fetchInterruptions()).thenReturn(mockOutagesString);
        when(energyOutageService.updateEnergyOutages(mockOutagesString)).thenReturn(mockEnergyOutages);

        // Act
        ResponseEntity<List<EnergyOutage>> response = energyOutageController.manualUpdateEnergyOutages();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(2, response.getBody().size());

        // Verify interactions
        verify(energyOutageService, times(1)).fetchInterruptions();
        verify(energyOutageService, times(1)).updateEnergyOutages(mockOutagesString);
    }

    @Test
    void testUpdateEnergyOutages_EmptyResult() {
        // Arrange
        String mockOutagesString = "no outages data";
        when(energyOutageService.fetchInterruptions()).thenReturn(mockOutagesString);
        when(energyOutageService.updateEnergyOutages(mockOutagesString)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<EnergyOutage>> response = energyOutageController.updateEnergyOutages();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}
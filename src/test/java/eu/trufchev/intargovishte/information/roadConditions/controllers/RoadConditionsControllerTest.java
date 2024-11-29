package eu.trufchev.intargovishte.information.roadConditions.controllers;

import eu.trufchev.intargovishte.information.roadConditions.entities.RoadConditions;
import eu.trufchev.intargovishte.information.roadConditions.feignClients.RoadConditionsClient;
import eu.trufchev.intargovishte.information.roadConditions.repositories.RoadConditionsRepository;
import eu.trufchev.intargovishte.information.roadConditions.services.RoadConditionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.*;

public class RoadConditionsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoadConditionsClient roadConditionsClient;
    @Mock
    private RoadConditionsService roadConditionsService;
    @Mock
    private RoadConditionsRepository roadConditionsRepository;

    @InjectMocks
    private RoadConditionsController roadConditionsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roadConditionsController).build();
    }

    @Test
    void testManualUpdate() throws Exception {
        // Given
        RoadConditions roadConditions = new RoadConditions(1L, "2024-11-29", "Clear road");
        List<RoadConditions> roadConditionsList = List.of(roadConditions);
        when(roadConditionsService.getRoadConditions()).thenReturn(roadConditionsList);

        // When & Then
        mockMvc.perform(get("/information/roadconditions/update"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].endDate").value("2024-11-29"))
                .andExpect(jsonPath("$[0].description").value("Clear road"));

        verify(roadConditionsService, times(1)).getRoadConditions();
        verify(roadConditionsRepository, times(1)).deleteAll();
        verify(roadConditionsRepository, times(1)).saveAll(roadConditionsList);
    }

    @Test
    void testCronUpdate() throws Exception {
        // Given
        RoadConditions roadConditions = new RoadConditions(1L, "2024-11-29", "Clear road");
        List<RoadConditions> roadConditionsList = List.of(roadConditions);
        when(roadConditionsService.getRoadConditions()).thenReturn(roadConditionsList);

        // When & Then
        mockMvc.perform(get("/information/roadconditions/update"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].endDate").value("2024-11-29"))
                .andExpect(jsonPath("$[0].description").value("Clear road"));

        verify(roadConditionsService, times(1)).getRoadConditions();
        verify(roadConditionsRepository, times(1)).deleteAll();
        verify(roadConditionsRepository, times(1)).saveAll(roadConditionsList);
    }

    @Test
    void testAllRoadCondition() throws Exception {
        // Given
        RoadConditions roadConditions = new RoadConditions(1L, "2024-11-29", "Clear road");
        List<RoadConditions> roadConditionsList = List.of(roadConditions);
        when(roadConditionsRepository.findAll()).thenReturn(roadConditionsList);

        // When & Then
        mockMvc.perform(get("/information/roadconditions/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].endDate").value("2024-11-29"))
                .andExpect(jsonPath("$[0].description").value("Clear road"));

        verify(roadConditionsRepository, times(1)).findAll();
    }

    @Test
    void testUpdateRoadConditions() {
        // Given
        RoadConditions roadConditions = new RoadConditions(1L, "2024-11-29", "Clear road");
        List<RoadConditions> roadConditionsList = List.of(roadConditions);
        when(roadConditionsService.getRoadConditions()).thenReturn(roadConditionsList);

        // When
        ResponseEntity<List<RoadConditions>> response = roadConditionsController.updateRoadConditions();

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(roadConditionsList, response.getBody());
        verify(roadConditionsService, times(1)).getRoadConditions();
        verify(roadConditionsRepository, times(1)).deleteAll();
        verify(roadConditionsRepository, times(1)).saveAll(roadConditionsList);
    }
}

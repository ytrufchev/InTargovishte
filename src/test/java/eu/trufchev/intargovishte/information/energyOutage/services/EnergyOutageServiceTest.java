package eu.trufchev.intargovishte.information.energyOutage.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trufchev.intargovishte.information.energyOutage.entities.EnergyOutage;
import eu.trufchev.intargovishte.information.energyOutage.feignClient.EnergoProClient;
import eu.trufchev.intargovishte.information.energyOutage.repositories.EnergyOutageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EnergyOutageServiceTest {

    @Mock
    private EnergyOutageRepository energyOutageRepository;

    @Mock
    private EnergoProClient energoProClient;

    @InjectMocks
    private EnergyOutageService energyOutageService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        energyOutageService = new EnergyOutageService(energyOutageRepository, objectMapper, energoProClient);
    }

    @Test
    @DisplayName("Should update energy outages and save them to the repository")
    void testUpdateEnergyOutages() {
        String jsonEvent = """
                [
                  {
                    "area_locations_for_next_48_hours": [
                      {
                        "location_period": "<b>01-12-2024</b>",
                        "location_text": "<p>Power outage in area A</p>"
                      },
                      {
                        "location_period": "<b>02-12-2024</b>",
                        "location_text": "<p>Power outage in area B</p>"
                      }
                    ]
                  }
                ]
                """;

        // Mock repository methods
        doNothing().when(energyOutageRepository).deleteAll();
        when(energyOutageRepository.saveAll(anyList())).thenReturn(null);

        // Call the service method
        List<EnergyOutage> energyOutages = energyOutageService.updateEnergyOutages(jsonEvent);

        // Verify interactions and assert results
        verify(energyOutageRepository).deleteAll();
        verify(energyOutageRepository).saveAll(anyList());
        assertThat(energyOutages).hasSize(2);
        assertThat(energyOutages.get(0).getLocation_period()).isEqualTo("01-12-2024");
        assertThat(energyOutages.get(0).getLocation_text()).isEqualTo("Power outage in area A");
        assertThat(energyOutages.get(1).getLocation_period()).isEqualTo("02-12-2024");
        assertThat(energyOutages.get(1).getLocation_text()).isEqualTo("Power outage in area B");
    }

    @Test
    @DisplayName("Should throw runtime exception when JSON parsing fails")
    void testUpdateEnergyOutagesWithInvalidJson() {
        String invalidJson = """
                [
                  {
                    "area_locations_for_next_48_hours": [
                      { "location_period": "01-12-2024", "location_text":
                """;

        // Call the service method and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                energyOutageService.updateEnergyOutages(invalidJson));
        assertThat(exception.getMessage()).contains("Error parsing or saving the energy outage information");

        // Verify no repository interaction
        verify(energyOutageRepository, never()).deleteAll();
        verify(energyOutageRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Should fetch interruptions using Feign client")
    void testFetchInterruptions() {
        String expectedResponse = "{\"data\":\"mocked interruption response\"}";

        // Mock Feign client response
        when(energoProClient.getInterruptions(
                eq("get_interruptions"),
                eq(8),
                eq("for_next_48_hours"),
                eq(0),
                anyString(),
                anyString(),
                eq("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36"),
                eq("application/json, text/javascript, */*; q=0.01"),
                eq("XMLHttpRequest"),
                eq("https://www.energo-pro.bg/bg/planirani-prekysvanija")
        )).thenReturn(expectedResponse);

        // Call the service method
        String response = energyOutageService.fetchInterruptions();

        // Verify interactions and assert results
        verify(energoProClient).getInterruptions(
                eq("get_interruptions"),
                eq(8),
                eq("for_next_48_hours"),
                eq(0),
                anyString(),
                anyString(),
                eq("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36"),
                eq("application/json, text/javascript, */*; q=0.01"),
                eq("XMLHttpRequest"),
                eq("https://www.energo-pro.bg/bg/planirani-prekysvanija")
        );
        assertThat(response).isEqualTo(expectedResponse);
    }
}

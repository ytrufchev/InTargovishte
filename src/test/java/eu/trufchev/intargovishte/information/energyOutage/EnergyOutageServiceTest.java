package eu.trufchev.intargovishte.information.energyOutage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trufchev.intargovishte.information.energyOutage.entities.EnergyOutage;
import eu.trufchev.intargovishte.information.energyOutage.repositories.EnergyOutageRepository;
import eu.trufchev.intargovishte.information.energyOutage.services.EnergyOutageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class EnergyOutageServiceTest {

    @Mock
    private EnergyOutageRepository energyOutageRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EnergyOutageService energyOutageService;

    private String sampleJson;

    @BeforeEach
    void setUp() {
        sampleJson = """
        [
            {
                "area_locations_for_next_48_hours": [
                    {
                        "location_period": "<p>2024-10-10</p>",
                        "location_text": "<p>Power outage description</p>"
                    }
                ]
            }
        ]
        """;
    }

    @Test
    void shouldParseAndSaveEnergyOutagesSuccessfully() throws Exception {
        // Arrange
        JsonNode mockJsonNode = new ObjectMapper().readTree(sampleJson);
        when(objectMapper.readTree(anyString())).thenReturn(mockJsonNode);

        // Act
        List<EnergyOutage> energyOutages = energyOutageService.updateEnergyOutages(sampleJson);

        // Assert
        assertThat(energyOutages).hasSize(1);
        assertThat(energyOutages.get(0).getLocation_text()).isEqualTo("Power outage description");
        assertThat(energyOutages.get(0).getLocation_period()).isEqualTo("2024-10-10");

        verify(energyOutageRepository, times(1)).deleteAll();
        verify(energyOutageRepository, times(1)).saveAll(energyOutages);
    }

    @Test
    void shouldThrowRuntimeExceptionOnInvalidJson() throws Exception {
        // Arrange
        String invalidJson = "invalid_json";
        when(objectMapper.readTree(anyString())).thenThrow(new RuntimeException("Invalid JSON"));

        // Act & Assert
        assertThatThrownBy(() -> energyOutageService.updateEnergyOutages(invalidJson))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error parsing or saving the municipality event");
    }
}
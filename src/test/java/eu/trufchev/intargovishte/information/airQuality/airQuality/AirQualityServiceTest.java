package eu.trufchev.intargovishte.information.airQuality.airQuality;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trufchev.intargovishte.information.airQuality.entities.*;
import eu.trufchev.intargovishte.information.airQuality.feignClients.AirQualityClient;
import eu.trufchev.intargovishte.information.airQuality.services.AirQualityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AirQualityServiceTest {

    @Mock
    private AirQualityClient airQualityClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AirQualityService airQualityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAirQuality() throws JsonProcessingException {
        // Arrange
        String mockResponse = "{\"coord\":{\"lat\":43.15,\"lon\":26.34},\"list\":[{\"dt\":1633046400,\"main\":{\"aqi\":2},\"components\":{\"co\":201.94,\"no\":0.02,\"no2\":1.24,\"o3\":68.67,\"so2\":0.63,\"pm2_5\":4.81,\"pm10\":5.99,\"nh3\":0.71}}]}";
        when(airQualityClient.getAirQuality(anyString(), anyString(), anyString())).thenReturn(mockResponse);
        when(objectMapper.readTree(mockResponse)).thenReturn(new ObjectMapper().readTree(mockResponse));

        // Act
        AirQuality result = airQualityService.getAirQuality();

        // Assert
        assertNotNull(result);
        assertEquals(43.15, result.getCoord().getLat());
        assertEquals(26.34, result.getCoord().getLon());
        assertEquals("2", result.getList().getMain().getAqi());
        assertEquals(1633046400L, result.getList().getDt());
        assertEquals(201.94, result.getList().getComponents().getCo());
        assertEquals(0.02, result.getList().getComponents().getNo());
        assertEquals(1.24, result.getList().getComponents().getNo2());
        assertEquals(68.67, result.getList().getComponents().getO3());
        assertEquals(0.63, result.getList().getComponents().getSo2());
        assertEquals(4.81, result.getList().getComponents().getPm2_5());
        assertEquals(5.99, result.getList().getComponents().getPm10());
        assertEquals(0.71, result.getList().getComponents().getNh3());

        verify(airQualityClient).getAirQuality("43.15", "26.34", "540aef93c732bdc81e74cbfa5ea31132");
        verify(objectMapper).readTree(mockResponse);
    }

    @Test
    void testGetAirQualityClientError() {
        // Arrange
        when(airQualityClient.getAirQuality(anyString(), anyString(), anyString())).thenThrow(new RuntimeException("API Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> airQualityService.getAirQuality());
    }

    @Test
    void testGetAirQualityJsonProcessingError() throws JsonProcessingException {
        // Arrange
        String mockResponse = "Invalid JSON";
        when(airQualityClient.getAirQuality(anyString(), anyString(), anyString())).thenReturn(mockResponse);
        when(objectMapper.readTree(mockResponse)).thenThrow(new JsonProcessingException("JSON parsing error") {});

        // Act & Assert
        assertThrows(JsonProcessingException.class, () -> airQualityService.getAirQuality());
    }
}
package eu.trufchev.intargovishte.information.airQuality.airQuality;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.trufchev.intargovishte.InTargovishteApplication;
import eu.trufchev.intargovishte.information.airQuality.entities.AirQuality;
import eu.trufchev.intargovishte.information.airQuality.feignClients.AirQualityClient;
import eu.trufchev.intargovishte.information.airQuality.services.AirQualityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class AirQualityServiceIntegrationTest {

    @Autowired
    private AirQualityService airQualityService;

    @MockBean
    private AirQualityClient airQualityClient;

    @Test
    void testGetAirQualityIntegration() throws JsonProcessingException {
        // Arrange
        String mockResponse = "{\"coord\":{\"lat\":43.15,\"lon\":26.34},\"list\":[{\"dt\":1633046400,\"main\":{\"aqi\":2},\"components\":{\"co\":201.94,\"no\":0.02,\"no2\":1.24,\"o3\":68.67,\"so2\":0.63,\"pm2_5\":4.81,\"pm10\":5.99,\"nh3\":0.71}}]}";
        when(airQualityClient.getAirQuality(anyString(), anyString(), anyString())).thenReturn(mockResponse);

        // Act
        AirQuality result = airQualityService.getAirQuality();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getCoord());
        assertNotNull(result.getList());
        assertNotNull(result.getList().getMain());
        assertNotNull(result.getList().getComponents());

        // Validate coordinate values
        assertEquals(43.15, result.getCoord().getLat());
        assertEquals(26.34, result.getCoord().getLon());

        // Validate main values
        assertEquals("2", result.getList().getMain().getAqi());

        // Validate components values
        assertEquals(1633046400L, result.getList().getDt());
        assertEquals(201.94, result.getList().getComponents().getCo());
        assertEquals(0.02, result.getList().getComponents().getNo());
        assertEquals(1.24, result.getList().getComponents().getNo2());
        assertEquals(68.67, result.getList().getComponents().getO3());
        assertEquals(0.63, result.getList().getComponents().getSo2());
        assertEquals(4.81, result.getList().getComponents().getPm2_5());
        assertEquals(5.99, result.getList().getComponents().getPm10());
        assertEquals(0.71, result.getList().getComponents().getNh3());
    }

    @Test
    void testGetAirQualityIntegrationError() {
        // Arrange
        when(airQualityClient.getAirQuality(anyString(), anyString(), anyString())).thenThrow(new RuntimeException("API Error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> airQualityService.getAirQuality());
        assertEquals("API Error", exception.getMessage());
    }
}

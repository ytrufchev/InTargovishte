package eu.trufchev.intargovishte.information.energyOutage.feignClient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        EnergoProClientTest.FeignClientTestConfiguration.class
})
public class EnergoProClientTest {

    // Configuration to enable Feign clients for testing
    @Configuration
    @EnableFeignClients(basePackages = "eu.trufchev.intargovishte.information.energyOutage.feignClient")
    public static class FeignClientTestConfiguration {
    }

    private final EnergoProClient energoProClient = mock(EnergoProClient.class);

    private static final String MOCK_RESPONSE = "Sample Interruption Data";
    private static final String METHOD = "getInterruptions";
    private static final int REGION_ID = 1;
    private static final String TYPE = "planned";
    private static final int OFFSET = 0;
    private static final String FROM_DATE = "2024-01-01";
    private static final String TO_DATE = "2024-01-31";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String ACCEPT = "application/json";
    private static final String X_REQUESTED_WITH = "XMLHttpRequest";
    private static final String REFERER = "https://www.energo-pro.bg/";

    @Test
    void testGetInterruptions_SuccessfulCall() {
        // Arrange
        when(energoProClient.getInterruptions(
                eq(METHOD),
                eq(REGION_ID),
                eq(TYPE),
                eq(OFFSET),
                eq(FROM_DATE),
                eq(TO_DATE),
                eq(USER_AGENT),
                eq(ACCEPT),
                eq(X_REQUESTED_WITH),
                eq(REFERER)
        )).thenReturn(MOCK_RESPONSE);

        // Act
        String response = energoProClient.getInterruptions(
                METHOD,
                REGION_ID,
                TYPE,
                OFFSET,
                FROM_DATE,
                TO_DATE,
                USER_AGENT,
                ACCEPT,
                X_REQUESTED_WITH,
                REFERER
        );

        // Assert
        assertNotNull(response);
        assertEquals(MOCK_RESPONSE, response);
    }

    @Test
    void testGetInterruptions_AllParametersProvided() {
        // Arrange
        String differentMethod = "differentMethod";
        int differentRegionId = 2;
        String differentType = "unplanned";
        int differentOffset = 10;
        String differentFromDate = "2024-02-01";
        String differentToDate = "2024-02-29";

        // Stub the method
        when(energoProClient.getInterruptions(
                eq(differentMethod),
                eq(differentRegionId),
                eq(differentType),
                eq(differentOffset),
                eq(differentFromDate),
                eq(differentToDate),
                eq(USER_AGENT),
                eq(ACCEPT),
                eq(X_REQUESTED_WITH),
                eq(REFERER)
        )).thenReturn(MOCK_RESPONSE);

        // Act
        String response = energoProClient.getInterruptions(
                differentMethod,
                differentRegionId,
                differentType,
                differentOffset,
                differentFromDate,
                differentToDate,
                USER_AGENT,
                ACCEPT,
                X_REQUESTED_WITH,
                REFERER
        );

        // Assert
        assertNotNull(response);
    }

    @Test
    void testGetInterruptions_HeaderValidation() {
        // Arrange
        when(energoProClient.getInterruptions(
                anyString(),
                anyInt(),
                anyString(),
                anyInt(),
                anyString(),
                anyString(),
                eq(USER_AGENT),
                eq(ACCEPT),
                eq(X_REQUESTED_WITH),
                eq(REFERER)
        )).thenReturn(MOCK_RESPONSE);

        // Act
        String response = energoProClient.getInterruptions(
                METHOD,
                REGION_ID,
                TYPE,
                OFFSET,
                FROM_DATE,
                TO_DATE,
                USER_AGENT,
                ACCEPT,
                X_REQUESTED_WITH,
                REFERER
        );

        // Assert
        assertNotNull(response);
    }
}
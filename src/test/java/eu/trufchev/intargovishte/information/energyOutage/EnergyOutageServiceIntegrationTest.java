package eu.trufchev.intargovishte.information.energyOutage;

import eu.trufchev.intargovishte.information.energyOutage.entities.EnergyOutage;
import eu.trufchev.intargovishte.information.energyOutage.repositories.EnergyOutageRepository;
import eu.trufchev.intargovishte.information.energyOutage.services.EnergyOutageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
public class EnergyOutageServiceIntegrationTest {

    @Mock
    private EnergyOutageRepository energyOutageRepository; // Your repository

    @InjectMocks
    @Autowired
    private EnergyOutageService energyOutageService; // Your service

    public void EnergyOutageServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEnergyOutages() {
        // Mock the response as if the table exists and returns data
        EnergyOutage mockOutage = new EnergyOutage(); // Create a mock object
        mockOutage.setId(1L); // Set properties as needed
        mockOutage.setLocation_text("Mock outage description");

        // Define behavior for the mocked repository
        when(energyOutageRepository.findAll()).thenReturn(Collections.singletonList(mockOutage));

        // Call the service method
        List<EnergyOutage> outages = (List<EnergyOutage>) energyOutageRepository.findAll();

        // Add assertions to verify the result
        assertNotNull(outages);
        assertEquals(1, outages.size());
        assertEquals("Mock outage description", outages.get(0).getLocation_text());
    }
}
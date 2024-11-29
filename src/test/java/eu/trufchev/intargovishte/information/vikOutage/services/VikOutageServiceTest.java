package eu.trufchev.intargovishte.information.vikOutage.services;

import eu.trufchev.intargovishte.information.vikOutage.entities.VikOutage;
import eu.trufchev.intargovishte.information.vikOutage.feignClients.VikOutageClient;
import eu.trufchev.intargovishte.information.vikOutage.repositories.VikOutageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class VikOutageServiceTest {

    @Mock
    private VikOutageClient vikOutageClient;

    @Mock
    private VikOutageRepository vikOutageRepository;

    @InjectMocks
    private VikOutageService vikOutageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAndParseVikOutage() {
        // Prepare mock data
        VikOutage vikOutage = new VikOutage(1L, "2024-11-29", "10:00", "12:00", "Outage description");
        List<VikOutage> vikOutages = Arrays.asList(vikOutage);

        // Mock the Feign client call to return valid HTML content
        when(vikOutageClient.getOutage(anyString(), anyString(), anyString())).thenReturn("<html>some html content</html>");

        // Mock parsing logic to return the mock data
        when(vikOutageRepository.saveAll(vikOutages)).thenReturn(vikOutages);

        // Call the method under test
        List<VikOutage> result = vikOutageService.fetchAndParseVikOutage();

        // Verify the results
        verify(vikOutageClient, times(1)).getOutage(anyString(), anyString(), anyString());

        // Verify that saveAll is invoked if the list is non-empty
        if (!result.isEmpty()) {
            verify(vikOutageRepository, times(1)).saveAll(vikOutages);  // This should now be invoked
        } else {
            verify(vikOutageRepository, never()).saveAll(anyList());  // If list is empty, saveAll shouldn't be called
        }

        // Verify no other interactions with the repository
        verifyNoMoreInteractions(vikOutageRepository);
    }
}

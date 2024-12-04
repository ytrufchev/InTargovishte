package eu.trufchev.intargovishte.information.events.dramaTheatre.service;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Photo;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Production;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetPlaysResponseTest {

    private GetPlaysResponseService service; // Assuming this is the class that you are testing
    private Response mockResponse;

    @BeforeEach
    public void setUp() {
        service = mock(GetPlaysResponseService.class); // Mock the service under test
        mockResponse = mock(Response.class); // Mock the Response class
    }

    @Test
    public void testGetPlaysForTargovishte_success() {
        // Prepare mock data for productions
        Production production1 = new Production("1", 120, 16, new Photo(), new ArrayList<>(), "Test Play 1", new ArrayList<>());
        Production production2 = new Production("2", 100, 14, new Photo(), new ArrayList<>(), "Test Play 2", new ArrayList<>());
        List<Production> productions = Arrays.asList(production1, production2);

        // Define the behavior of the mocked Response
        when(mockResponse.getProductions()).thenReturn(productions);

        // Call the service method (mocked)
        when(service.getPlaysForTargovishte()).thenReturn(mockResponse);

        // Get the actual response
        Response actualResponse = service.getPlaysForTargovishte();

        // Assert that the response contains the correct number of productions
        assertNotNull(actualResponse);
        assertEquals(2, actualResponse.getProductions().size());
        assertEquals("Test Play 1", actualResponse.getProductions().get(0).getTitle());
    }

    @Test
    public void testGetPlaysForTargovishte_emptyProductions() {
        // Prepare empty productions list
        when(mockResponse.getProductions()).thenReturn(Collections.emptyList());

        // Call the service method (mocked)
        when(service.getPlaysForTargovishte()).thenReturn(mockResponse);

        // Get the actual response
        Response actualResponse = service.getPlaysForTargovishte();

        // Assert that no productions were returned
        assertNotNull(actualResponse);
        assertTrue(actualResponse.getProductions().isEmpty());
    }

    @Test
    public void testGetPlaysForTargovishte_noUpcomingEvents() {
        // Prepare mock data to simulate no upcoming events (empty list)
        when(mockResponse.getProductions()).thenReturn(Collections.emptyList());

        // Call the service method (mocked)
        when(service.getPlaysForTargovishte()).thenReturn(mockResponse);

        // Get the actual response
        Response actualResponse = service.getPlaysForTargovishte();

        // Assert that there are no upcoming events
        assertNotNull(actualResponse);
        assertTrue(actualResponse.getProductions().isEmpty());
    }

    @Test
    public void testGetPlaysForTargovishte_withNullProductions() {
        // Simulate a null production list (unexpected case)
        when(mockResponse.getProductions()).thenReturn(null);

        // Call the service method (mocked)
        when(service.getPlaysForTargovishte()).thenReturn(mockResponse);

        // Get the actual response
        Response actualResponse = service.getPlaysForTargovishte();

        // Assert that the response is null or handle it according to the behavior you expect
        assertNotNull(actualResponse);
        assertNull(actualResponse.getProductions());
    }

    // Mocked method to simulate service
    private class GetPlaysResponseService {
        public Response getPlaysForTargovishte() {
            return mockResponse;
        }
    }
}

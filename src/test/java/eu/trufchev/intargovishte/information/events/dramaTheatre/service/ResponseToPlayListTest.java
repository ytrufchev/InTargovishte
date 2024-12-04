package eu.trufchev.intargovishte.information.events.dramaTheatre.service;

import static org.mockito.Mockito.*;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.*;
import eu.trufchev.intargovishte.information.events.dramaTheatre.feignClient.CookieClient;
import eu.trufchev.intargovishte.information.events.dramaTheatre.feignClient.PlayClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseToPlayListTest {



    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        GetPlaysResponse getPlaysResponse;
        CookieClient cookieClient;
        PlayClient playClient;
        // Initialize the class under test
        getPlaysResponse = new GetPlaysResponse();

        // Create mocks for the dependencies
        cookieClient = mock(CookieClient.class);
        playClient = mock(PlayClient.class);

        // Use reflection to set the private fields
        Field cookieClientField = GetPlaysResponse.class.getDeclaredField("cookieClient");
        cookieClientField.setAccessible(true);
        cookieClientField.set(getPlaysResponse, cookieClient);

        Field playClientField = GetPlaysResponse.class.getDeclaredField("playClient");
        playClientField.setAccessible(true);
        playClientField.set(getPlaysResponse, playClient);
    }

    @Test
    void testPlaysResponseToPlays() {
        // Create mock data for productions
        Production production1 = new Production("prod1", 120, 12, null, null, "Production 1", null);
        Production production2 = new Production("prod2", 90, 10, null, null, "Production 2", null);
        Host host = new Host("trg", "ref");
        ProductionEvent productionEvent = new ProductionEvent("2");
        // Create events and associate them with productions
        Event event1 = new Event("2024-12-04T20:00", host, "event1", false, null, productionEvent, null, 0);
        Event event2 = new Event("2024-12-05T20:00", host, "event2", false, null, productionEvent, null, 0);

        // List of productions and events
        List<Production> productions = Arrays.asList(production1, production2);
        List<Event> events = Arrays.asList(event1, event2);

        // Create an instance of the service (ResponseToPlayList)
        ResponseToPlayList responseToPlayList = new ResponseToPlayList();

        // Call the method to test
        List<Play> plays = responseToPlayList.playsResponseToPlays(productions, events);

        // Assert the plays list size
        assertNotNull(plays);
        assertEquals(2, plays.size(), "Expected two plays in the list");

        // Check the details of the first play
        Play play1 = plays.get(0);
        assertEquals("prod1", play1.getId());
        assertEquals("Production 1", play1.getTitle());

        // Check the details of the second play
        Play play2 = plays.get(1);
        assertEquals("prod2", play2.getId());
        assertEquals("Production 2", play2.getTitle());
    }
}

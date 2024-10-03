package eu.trufchev.intargovishte.information.fuelo;

import eu.trufchev.intargovishte.information.fuelo.entities.GasStation;
import eu.trufchev.intargovishte.information.fuelo.entities.GasstationsList;
import eu.trufchev.intargovishte.information.fuelo.feignclient.FueloClient;
import eu.trufchev.intargovishte.information.fuelo.repository.GasStationRepository;
import eu.trufchev.intargovishte.information.fuelo.services.ParseGasStationToHtml;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FueloControllerTest {

    @Mock
    private FueloClient fueloClient;

    @Mock
    private ParseGasStationToHtml parseGasStationToHtml;

    @Mock
    private GasStationRepository gasStationRepository;

    @InjectMocks
    private FueloController fueloController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize mocks
    }

    @Test
    public void testupdateGasStations() {
        // Mock the response from fueloClient and parseGasStationToHtml
        GasstationsList gasstationsList = new GasstationsList();
        GasStation gasStation = new GasStation();

        when(fueloClient.getGasstationDetails(anyString(), eq("bg"))).thenReturn("gas station details");
        when(parseGasStationToHtml.parseGasStationHtml(anyString())).thenReturn(gasStation);

        // Execute the controller method
        ResponseEntity<List<GasStation>> response = fueloController.updateGasStations();

        // Verify that gas stations were saved
        verify(gasStationRepository).deleteAll();
        verify(gasStationRepository).saveAll(anyList());

        // Check the response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(18, response.getBody().size());
    }

    @Test
    public void testAllGasStations() {
        // Mock the repository response
        List<GasStation> gasStations = new ArrayList<>();
        gasStations.add(new GasStation());
        when(gasStationRepository.findAll()).thenReturn(gasStations);

        // Execute the controller method
        ResponseEntity<List<GasStation>> response = fueloController.allGasStations();

        // Verify that the repository was called
        verify(gasStationRepository).findAll();

        // Check the response
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }
}

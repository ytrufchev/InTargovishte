package eu.trufchev.intargovishte.information.guestInformation;

import eu.trufchev.intargovishte.information.energyOutage.entities.EnergyOutage;
import eu.trufchev.intargovishte.information.energyOutage.repositories.EnergyOutageRepository;
import eu.trufchev.intargovishte.information.events.appEvents.dto.ResponseEventDTO;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.services.EventAppService;
import eu.trufchev.intargovishte.information.events.cinemagic.dto.MovieWithProjectionsDTO;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Movie;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.MovieWithProjections;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Projections;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.MovieRepository;
import eu.trufchev.intargovishte.information.events.cinemagic.repositories.ProjectionRepository;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Play;
import eu.trufchev.intargovishte.information.events.dramaTheatre.repository.PlaysRepository;
import eu.trufchev.intargovishte.information.events.municipality.entities.MunicipalityEvent;
import eu.trufchev.intargovishte.information.events.municipality.repository.MunicipalityEventRepository;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.information.events.puppetTheatre.repositories.PuppetTheaterRepository;
import eu.trufchev.intargovishte.information.fuelo.entities.GasStation;
import eu.trufchev.intargovishte.information.fuelo.repository.GasStationRepository;
import eu.trufchev.intargovishte.information.roadConditions.entities.RoadConditions;
import eu.trufchev.intargovishte.information.roadConditions.repositories.RoadConditionsRepository;
import eu.trufchev.intargovishte.information.vikOutage.entities.VikOutage;
import eu.trufchev.intargovishte.information.vikOutage.repositories.VikOutageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GuestControllerTest {

    @InjectMocks
    private GuestController guestController;

    @Mock
    private EnergyOutageRepository energyOutageRepository;

    @Mock
    private VikOutageRepository vikOutageRepository;

    @Mock
    private RoadConditionsRepository roadConditionsRepository;

    @Mock
    private GasStationRepository gasStationRepository;

    @Mock
    private EventAppService eventAppService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ProjectionRepository projectionRepository;

    @Mock
    private PlaysRepository playsRepository;

    @Mock
    private MunicipalityEventRepository municipalityEventRepository;

    @Mock
    private PuppetTheaterRepository puppetTheaterRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEnergyOutages() {
        List<EnergyOutage> mockOutages = Arrays.asList(new EnergyOutage(), new EnergyOutage());
        when(energyOutageRepository.findAll()).thenReturn(mockOutages);

        ResponseEntity<List<EnergyOutage>> response = guestController.getAllEnergyOutages();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(energyOutageRepository, times(1)).findAll();
    }

    @Test
    void testGetOutages() {
        List<VikOutage> mockOutages = Collections.singletonList(new VikOutage());
        when(vikOutageRepository.findAll()).thenReturn(mockOutages);

        ResponseEntity<List<VikOutage>> response = guestController.getOutages();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(vikOutageRepository, times(1)).findAll();
    }

    @Test
    void testAllRoadConditions() {
        List<RoadConditions> mockConditions = Collections.singletonList(new RoadConditions());
        when(roadConditionsRepository.findAll()).thenReturn(mockConditions);

        ResponseEntity<List<RoadConditions>> response = guestController.allRoadCondition();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(roadConditionsRepository, times(1)).findAll();
    }

    @Test
    void testGetAllFuelStations() {
        List<GasStation> mockStations = Arrays.asList(new GasStation(), new GasStation());
        when(gasStationRepository.findAll()).thenReturn(mockStations);

        ResponseEntity<List<GasStation>> response = guestController.getAllFuelStations();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(gasStationRepository, times(1)).findAll();
    }

    @Test
    void testGetTopEvents() {
        List<ResponseEventDTO> mockEvents = Arrays.asList(new ResponseEventDTO(), new ResponseEventDTO());
        when(eventAppService.findNextTenApprovedEvents()).thenReturn(mockEvents);

        ResponseEntity<List<ResponseEventDTO>> response = guestController.getTopEvents();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(eventAppService, times(1)).findNextTenApprovedEvents();
    }
}

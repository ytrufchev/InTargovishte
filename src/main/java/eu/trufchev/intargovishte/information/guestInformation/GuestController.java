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
import eu.trufchev.intargovishte.information.events.dramaTheatre.dto.PlayDTO;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Play;
import eu.trufchev.intargovishte.information.events.dramaTheatre.repository.PlaysRepository;
import eu.trufchev.intargovishte.information.events.municipality.entities.MunicipalityEvent;
import eu.trufchev.intargovishte.information.events.municipality.repository.MunicipalityEventRepository;
import eu.trufchev.intargovishte.information.events.puppetTheatre.PuppetTheaterDTO;
import eu.trufchev.intargovishte.information.events.puppetTheatre.entities.PuppetTheater;
import eu.trufchev.intargovishte.information.events.puppetTheatre.repositories.PuppetTheaterRepository;
import eu.trufchev.intargovishte.information.events.puppetTheatre.services.PuppetTheaterService;
import eu.trufchev.intargovishte.information.fuelo.entities.GasStation;
import eu.trufchev.intargovishte.information.fuelo.repository.GasStationRepository;
import eu.trufchev.intargovishte.information.roadConditions.entities.RoadConditions;
import eu.trufchev.intargovishte.information.roadConditions.repositories.RoadConditionsRepository;
import eu.trufchev.intargovishte.information.vikOutage.entities.VikOutage;
import eu.trufchev.intargovishte.information.vikOutage.repositories.VikOutageRepository;
import eu.trufchev.intargovishte.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/guest")
public class GuestController {
    @Autowired
    EnergyOutageRepository energyOutageRepository;
    @Autowired
    VikOutageRepository vikOutageRepository;
    @Autowired
    RoadConditionsRepository roadConditionsRepository;
    @Autowired
    GasStationRepository gasStationRepository;
    @Autowired
    EventAppService eventAppService;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    PlaysRepository playsRepository;
    @Autowired
    MunicipalityEventRepository municipalityEventRepository;
    @Autowired
    PuppetTheaterRepository puppetTheaterRepository;
    @Autowired
    PuppetTheaterService puppetTheaterService;
    @Autowired
    ProjectionRepository projectionRepository;

    @GetMapping("/energy")
    public ResponseEntity<List<EnergyOutage>> getAllEnergyOutages(){
        List<EnergyOutage> energyOutages = new ArrayList<>();
        energyOutageRepository.findAll().forEach(energyOutages::add);
        return ResponseEntity.ok(energyOutages);
    }

    @GetMapping("/vik")
    public ResponseEntity<List<VikOutage>> getOutages() {
        List<VikOutage> vikOutages = new ArrayList<>();
        vikOutageRepository.findAll().forEach(vikOutages::add);
        return ResponseEntity.ok(vikOutages);
    }

    @GetMapping("/road")
    public ResponseEntity<List<RoadConditions>> allRoadCondition(){
        List<RoadConditions> roadConditions = new ArrayList<>();
        roadConditionsRepository.findAll().forEach(roadConditions::add);

        // Return the list of gas stations
        return ResponseEntity.ok(roadConditions);
    }

    @GetMapping("/fuel")
    public ResponseEntity<List<GasStation>> getAllFuelStations() {
        List<GasStation> stations =  new ArrayList<>();
        gasStationRepository.findAll().forEach(stations::add);
        return ResponseEntity.ok(stations);
    }
    @GetMapping("/toptenevents")
    public ResponseEntity<List<ResponseEventDTO>> getTopEvents() {
        List<ResponseEventDTO> events = eventAppService.findNextTenApprovedEvents();
        // Safely get up to 10 elements
        List<ResponseEventDTO> topTenEvents = events.size() > 10 ? events.subList(0, 10) : events;
        return ResponseEntity.ok(topTenEvents);
    }
    @GetMapping("/listmovies")
    public ResponseEntity<List<MovieWithProjections>> screenings(){
        List<Movie> movies = (List<Movie>) movieRepository.findAll();
        List<Projections> projections = (List<Projections>) projectionRepository.findAll();
        MovieWithProjectionsDTO movieWithProjectionsDTO = new MovieWithProjectionsDTO();
        List<MovieWithProjections> movieWithProjectionsList = movieWithProjectionsDTO.combineMovieWithProjections(movies, projections, null);
        return ResponseEntity.ok(movieWithProjectionsList);
    }

    @GetMapping("/allplays")
    public List<PlayDTO> playsList() {
        List<PlayDTO> playDTOS = new ArrayList<>();
        List<Play> plays = new ArrayList<>();
        plays.addAll((Collection<? extends Play>) playsRepository.findAll());
        for(Play play: plays){
            PlayDTO playDTO = new PlayDTO();
            playDTO.setId(play.getId());
            playDTO.setTitle(play.getTitle());
            playDTO.setLength(play.getLength());
            playDTO.setMinAgeRestriction(play.getMinAgeRestriction());
            playDTO.setLargePhoto(play.getLargePhoto());
            playDTO.setPlaceName(play.getPlaceName());
            playDTO.setStartDates(play.getStartDates());
            playDTO.setLikesCount(play.getLikes() != null ? (long) play.getLikes().size() : 0L);
            playDTO.setLikedByCurrentUser(false);
            playDTOS.add(playDTO);
        }
        return playDTOS;
    }

    @GetMapping("/allmunicipality")
    public ResponseEntity<List<MunicipalityEvent>> getAllMunicipalityEvents(){
        List<MunicipalityEvent> municipalityEvents = new ArrayList<>();
        municipalityEventRepository.findAll().forEach(municipalityEvents::add);
        List<MunicipalityEvent> futureEvents = municipalityEvents.stream()
                .filter(event -> {
                    // Convert the string timestamp to a long
                    long eventTimestamp = Long.parseLong(event.getDate());

                    // Convert the timestamp to LocalDate
                    LocalDate eventDate = Instant.ofEpochMilli(eventTimestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    // Return true if the event date is today or in the future
                    return !eventDate.isBefore(LocalDate.now());
                })
                .collect(Collectors.toList());
        Collections.reverse(futureEvents);
        return ResponseEntity.ok(futureEvents);
    }

    @GetMapping("/allpuppet")
    public List<PuppetTheaterDTO> allPuppetTheaterEvents(){
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        List<PuppetTheaterDTO> allEvents = puppetTheaterService.PuppetToDTO();
        // Create formatter for Bulgarian locale
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", new Locale("bg"));

        return allEvents.stream()
                .filter(event -> {
                    try {
                        // Parse the event date using Bulgarian locale
                        String dateStr = String.format("%s %s %d",
                                event.getEventMonth(),
                                event.getEventDay(),
                                currentYear);
                        LocalDate eventDate = LocalDate.parse(dateStr, formatter);

                        // If the event date is in the past for this year, assume it's for next year
                        if (eventDate.isBefore(currentDate)) {
                            eventDate = eventDate.plusYears(1);
                        }

                        // Return true if the event is today or in the future
                        return !eventDate.isBefore(currentDate);
                    } catch (Exception e) {
                        // Log the error if needed
                        // logger.error("Error parsing date for event: " + event.getId(), e);
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}

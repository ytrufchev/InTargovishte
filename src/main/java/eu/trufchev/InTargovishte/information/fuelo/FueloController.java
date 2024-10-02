package eu.trufchev.InTargovishte.information.fuelo;

import eu.trufchev.InTargovishte.information.fuelo.entities.GasStation;
import eu.trufchev.InTargovishte.information.fuelo.entities.GasstationsList;
import eu.trufchev.InTargovishte.information.fuelo.feignclient.FueloClient;
import eu.trufchev.InTargovishte.information.fuelo.repository.GasStationRepository;
import eu.trufchev.InTargovishte.information.fuelo.services.ParseGasStationToHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/fuel")
@RestController
public class FueloController {
    @Autowired
    FueloClient fueloClient;
    @Autowired
    ParseGasStationToHtml parseGasStationToHtml;
    @Autowired
    GasStationRepository gasStationRepository;

    @GetMapping("/update")
    public ResponseEntity<List<GasStation>> upcomingPlays() {
        GasstationsList gasstationsLists = new GasstationsList();
        List<GasStation> gasStations = new ArrayList<>();
        for(int i = 0; i < gasstationsLists.getGasstations().size(); i++){
            gasStations.add(parseGasStationToHtml.parseGasStationHtml(fueloClient.getGasstationDetails(gasstationsLists.getGasstations().get(i), "bg")));
        }
        gasStationRepository.deleteAll();
        gasStationRepository.saveAll(gasStations);
        return ResponseEntity.ok(gasStations);
    }
    @GetMapping("/all")
    public ResponseEntity<List<GasStation>> allGasStations(){
        List<GasStation> gasStations = new ArrayList<>();
        gasStationRepository.findAll().forEach(gasStations::add);

        // Return the list of gas stations
        return ResponseEntity.ok(gasStations);
    }

}
package eu.trufchev.intargovishte.information.fuelo;

import eu.trufchev.intargovishte.information.fuelo.entities.GasStation;
import eu.trufchev.intargovishte.information.fuelo.entities.GasstationsList;
import eu.trufchev.intargovishte.information.fuelo.feignclient.FueloClient;
import eu.trufchev.intargovishte.information.fuelo.repository.GasStationRepository;
import eu.trufchev.intargovishte.information.fuelo.services.ParseGasStationToHtml;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("content/fuel")
@RestController
public class FueloController {
    FueloClient fueloClient;
    ParseGasStationToHtml parseGasStationToHtml;
    GasStationRepository gasStationRepository;

    public FueloController(FueloClient fueloClient, ParseGasStationToHtml parseGasStationToHtml, GasStationRepository gasStationRepository) {
        this.fueloClient = fueloClient;
        this.parseGasStationToHtml = parseGasStationToHtml;
        this.gasStationRepository = gasStationRepository;
    }

    @GetMapping("/update")
    public ResponseEntity<List<GasStation>> updateGasStations() {
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
package eu.trufchev.intargovishte.information.fuelo;

import eu.trufchev.intargovishte.information.fuelo.entities.FuelPrice;
import eu.trufchev.intargovishte.information.fuelo.entities.GasStation;
import eu.trufchev.intargovishte.information.fuelo.entities.GasstationsList;
import eu.trufchev.intargovishte.information.fuelo.feignclient.FueloClient;
import eu.trufchev.intargovishte.information.fuelo.repository.GasStationRepository;
import eu.trufchev.intargovishte.information.fuelo.services.ParseGasStationToHtml;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("information/fuel")
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

    @Scheduled(cron = "0 0 9 * * *")
    public ResponseEntity<List<GasStation>> cronUpdate(){
        return updateGasStations();
    }

    @GetMapping("/update")
    public ResponseEntity<List<GasStation>> manualUpdate(){
        return updateGasStations();
    }

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
    @PutMapping("/{id}/prices")
    public ResponseEntity<GasStation> updateGasStationPrices(@PathVariable Long id, @RequestBody List<FuelPrice> updatedPrices) {
        GasStation gasStation = gasStationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gas Station not found"));

        for (FuelPrice priceUpdate : updatedPrices) {
            gasStation.setFuelPrices(priceUpdate.getFuelType(), priceUpdate.getPrice());
        }

        gasStation.setLastUpdated(System.currentTimeMillis());
        gasStationRepository.save(gasStation);

        return ResponseEntity.ok(gasStation);
    }

}
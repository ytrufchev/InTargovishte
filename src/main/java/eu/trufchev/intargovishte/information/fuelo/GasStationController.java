package eu.trufchev.intargovishte.information.fuelo;

import eu.trufchev.intargovishte.information.fuelo.entities.FuelPrice;
import eu.trufchev.intargovishte.information.fuelo.entities.GasStation;
import eu.trufchev.intargovishte.information.fuelo.repository.GasStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/information/fuel")
@RequiredArgsConstructor
public class GasStationController {

    private final GasStationRepository gasStationRepository;

    @GetMapping("/all")
    public ResponseEntity<List<GasStation>> getAllFuelStations() {
        List<GasStation> stations =  new ArrayList<>();
        gasStationRepository.findAll().forEach(stations::add);
        return ResponseEntity.ok(stations);
    }

    @PutMapping("/{id}/prices")
    public ResponseEntity<?> updateFuelPrices(@PathVariable Long id, @RequestBody Map<String, Double> updatedPrices) {
        GasStation station = gasStationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fuel station not found"));

        List<FuelPrice> updatedFuelPrices = station.getFuelPrices();
        for (FuelPrice fuelPrice : updatedFuelPrices) {
            if (updatedPrices.containsKey(fuelPrice.getFuelType())) {
                fuelPrice.setPrice(updatedPrices.get(fuelPrice.getFuelType()).toString());
            }
        }

        gasStationRepository.save(station);

        return ResponseEntity.ok().build();
    }
}

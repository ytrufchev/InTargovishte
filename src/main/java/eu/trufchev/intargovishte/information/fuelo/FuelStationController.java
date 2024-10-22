package eu.trufchev.intargovishte.information.fuelo;

import eu.trufchev.intargovishte.information.fuelo.entities.FuelPrice;
import eu.trufchev.intargovishte.information.fuelo.entities.FuelStation;
import eu.trufchev.intargovishte.information.fuelo.repository.FuelStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/information/fuel")
@RequiredArgsConstructor
public class FuelStationController {

    private final FuelStationRepository fuelStationRepository;

    @GetMapping("/all")
    public ResponseEntity<List<FuelStation>> getAllFuelStations() {
        List<FuelStation> stations = fuelStationRepository.findAll();
        return ResponseEntity.ok(stations);
    }

    @PutMapping("/{id}/prices")
    public ResponseEntity<?> updateFuelPrices(@PathVariable Long id, @RequestBody Map<String, Double> updatedPrices) {
        FuelStation station = fuelStationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fuel station not found"));

        List<FuelPrice> updatedFuelPrices = station.getFuelPrices();
        for (FuelPrice fuelPrice : updatedFuelPrices) {
            if (updatedPrices.containsKey(fuelPrice.getFuelType())) {
                fuelPrice.setPrice(updatedPrices.get(fuelPrice.getFuelType()));
            }
        }

        fuelStationRepository.save(station);

        return ResponseEntity.ok().build();
    }
}

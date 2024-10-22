package eu.trufchev.intargovishte.information.fuelo.entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GasStationTest {

    @Test
    void testSetAndGetId() {
        GasStation gasStation = new GasStation();
        gasStation.setId(123L);

        assertEquals(123, gasStation.getId());
    }

    @Test
    void testSetAndGetName() {
        GasStation gasStation = new GasStation();
        gasStation.setName("Fuel Station A");

        assertEquals("Fuel Station A", gasStation.getName());
    }

    @Test
    void testSetAndGetAddress() {
        GasStation gasStation = new GasStation();
        gasStation.setAddress("123 Main Street");

        assertEquals("123 Main Street", gasStation.getAddress());
    }

    @Test
    void testSetAndGetFuelPrices() {
        GasStation gasStation = new GasStation();
        List<FuelPrice> fuelPrices = new ArrayList<>();
        FuelPrice fuelPrice = new FuelPrice("Diesel", "2.40");
        fuelPrices.add(fuelPrice);
        gasStation.setFuelPrices(fuelPrices);

        assertNotNull(gasStation.getFuelPrices());
        assertEquals(1, gasStation.getFuelPrices().size());
        assertEquals("2.40", gasStation.getFuelPrices().get(0).getPrice());
    }
}

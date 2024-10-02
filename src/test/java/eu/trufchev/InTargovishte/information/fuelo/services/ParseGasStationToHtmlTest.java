package eu.trufchev.InTargovishte.information.fuelo.services;

import eu.trufchev.InTargovishte.information.fuelo.entities.FuelPrice;
import eu.trufchev.InTargovishte.information.fuelo.entities.GasStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParseGasStationToHtmlTest {
    private ParseGasStationToHtml parseGasStationToHtml;

    @BeforeEach
    public void setUp() {
        parseGasStationToHtml = new ParseGasStationToHtml();
    }

    @Test
    public void testParseGasStationHtml() {
        // Sample HTML input (with some encoded characters)
        String html = "<h4>Station Name</h4>"
                + "<h5>123 Main St</h5>"
                + "<img alt=\"Petrol\" title=\"Price: 2,50\" />"
                + "<img alt=\"Diesel\" title=\"Price: 2,40\" />";

        // Call the method to be tested
        GasStation gasStation = parseGasStationToHtml.parseGasStationHtml(html);

        // Verify gas station name and address
        assertNotNull(gasStation);
        assertEquals("Station Name", gasStation.getName());
        assertEquals("123 Main St", gasStation.getAddress());

        // Verify fuel prices
        List<FuelPrice> fuelPrices = gasStation.getFuelPrices();
        assertNotNull(fuelPrices);
        assertEquals(2, fuelPrices.size());

        // Verify the first fuel price (Petrol)
        FuelPrice petrol = fuelPrices.get(0);
        assertEquals("Petrol", petrol.getFuelType());
        assertEquals("2.50", petrol.getPrice());

        // Verify the second fuel price (Diesel)
        FuelPrice diesel = fuelPrices.get(1);
        assertEquals("Diesel", diesel.getFuelType());
        assertEquals("2.40", diesel.getPrice());
    }

    @Test
    public void testParseGasStationHtml_EmptyHtml() {
        // Test with empty HTML
        String html = "";

        // Call the method
        GasStation gasStation = parseGasStationToHtml.parseGasStationHtml(html);

        // Assert that a GasStation object is still returned with no data
        assertNotNull(gasStation);
        assertEquals("", gasStation.getName());
        assertEquals("", gasStation.getAddress());
        assertEquals(0, gasStation.getFuelPrices().size());
    }

}
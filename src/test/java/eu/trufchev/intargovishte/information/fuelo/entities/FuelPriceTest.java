package eu.trufchev.intargovishte.information.fuelo.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuelPriceTest {
    @Test
    public void testSettersAndGettersForType(){
        FuelPrice fuelPrice = new FuelPrice();
        fuelPrice.setFuelType("diesel");

        assertEquals("diesel", fuelPrice.getFuelType());
    }
    @Test
    public void testSettersAndGettersForPrice(){
        FuelPrice fuelPrice = new FuelPrice();
        fuelPrice.setFuelType("2.40");

        assertEquals("2.40", fuelPrice.getFuelType());
    }

}
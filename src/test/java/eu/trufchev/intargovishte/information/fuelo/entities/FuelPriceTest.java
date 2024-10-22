package eu.trufchev.intargovishte.information.fuelo.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuelPriceTest {
    @Test
     void testSettersAndGettersForType(){
        FuelPrice fuelPrice = new FuelPrice();
        fuelPrice.setFuelType("diesel");

        assertEquals("diesel", fuelPrice.getFuelType());
    }
    @Test
     void testSettersAndGettersForPrice(){
        FuelPrice fuelPrice = new FuelPrice();
        fuelPrice.setFuelType("diesel");

        assertEquals("diesel", fuelPrice.getFuelType());
    }

}
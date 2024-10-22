package eu.trufchev.intargovishte.information.fuelo.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GasstationsListTest {

    @Test
    void getGasstations() {
        GasstationsList gasstationsList = new GasstationsList();
        String omv = gasstationsList.getGasstations().get(8);

        assertEquals("577", omv);
    }
}
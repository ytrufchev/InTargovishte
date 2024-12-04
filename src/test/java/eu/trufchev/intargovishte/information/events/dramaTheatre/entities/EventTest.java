package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class EventTest {

    @Test
    public void testEventConstructorAndGetters() {
        Host host = new Host();  // Assuming Host is another entity, create an instance.
        Owner owner = new Owner();  // Assuming Owner is another entity, create an instance.
        ProductionEvent production = new ProductionEvent();  // Assuming ProductionEvent is another entity, create an instance.
        Stats stats = new Stats();  // Assuming Stats is another entity, create an instance.

        Event event = new Event("2024-12-04", host, "123", true, owner, production, stats, 1);

        // Verifying that getters work correctly
        assertThat(event.getDateStart()).isEqualTo("2024-12-04");
        assertThat(event.getHost()).isEqualTo(host);
        assertThat(event.getId()).isEqualTo("123");
        assertThat(event.isCohost()).isTrue();
        assertThat(event.getOwner()).isEqualTo(owner);
        assertThat(event.getProduction()).isEqualTo(production);
        assertThat(event.getStats()).isEqualTo(stats);
        assertThat(event.getType()).isEqualTo(1);
    }

    @Test
    public void testSetters() {
        Event event = new Event();

        Host host = new Host();  // Assuming Host is another entity, create an instance.
        event.setHost(host);
        assertThat(event.getHost()).isEqualTo(host);

        event.setDateStart("2025-01-01");
        assertThat(event.getDateStart()).isEqualTo("2025-01-01");

        event.setId("456");
        assertThat(event.getId()).isEqualTo("456");

        event.setCohost(false);
        assertThat(event.isCohost()).isFalse();

        Owner owner = new Owner();  // Assuming Owner is another entity, create an instance.
        event.setOwner(owner);
        assertThat(event.getOwner()).isEqualTo(owner);

        ProductionEvent production = new ProductionEvent();  // Assuming ProductionEvent is another entity, create an instance.
        event.setProduction(production);
        assertThat(event.getProduction()).isEqualTo(production);

        Stats stats = new Stats();  // Assuming Stats is another entity, create an instance.
        event.setStats(stats);
        assertThat(event.getStats()).isEqualTo(stats);

        event.setType(2);
        assertThat(event.getType()).isEqualTo(2);
    }

    @Test
    public void testToString() {
        Host host = new Host();
        Owner owner = new Owner();
        ProductionEvent production = new ProductionEvent();
        Stats stats = new Stats();

        Event event = new Event("2024-12-04", host, "123", true, owner, production, stats, 1);

        String expectedToString = "Event(dateStart=2024-12-04, host=" + host + ", id=123, isCohost=true, " +
                "owner=" + owner + ", production=" + production + ", stats=" + stats + ", type=1)";

        assertThat(event.toString()).isEqualTo(expectedToString);
    }
}

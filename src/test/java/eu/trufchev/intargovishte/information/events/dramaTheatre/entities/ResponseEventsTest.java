package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseEventsTest {

    @Test
    public void testEventGettersAndSetters() {
        // Arrange
        Host host = new Host("Main Hall", "HostRef123");
        Owner owner = new Owner("OwnerRef456");
        ProductionEvent production = new ProductionEvent("ProdEvent789");
        Stats stats = new Stats(300, 100, 50, 500, 200);

        Event event = new Event(
                "2024-12-04T10:00:00", // dateStart
                host,                  // host
                "Event123",            // id
                true,                  // isCohost
                owner,                 // owner
                production,            // production
                stats,                 // stats
                2                      // type
        );

        // Act & Assert
        assertThat(event.getDateStart()).isEqualTo("2024-12-04T10:00:00");
        assertThat(event.getHost()).isEqualTo(host);
        assertThat(event.getId()).isEqualTo("Event123");
        assertThat(event.isCohost()).isTrue();
        assertThat(event.getOwner()).isEqualTo(owner);
        assertThat(event.getProduction()).isEqualTo(production);
        assertThat(event.getStats()).isEqualTo(stats);
        assertThat(event.getType()).isEqualTo(2);
    }

    @Test
    public void testEventToString() {
        // Arrange
        Host host = new Host("Main Hall", "HostRef123");
        Owner owner = new Owner("OwnerRef456");
        ProductionEvent production = new ProductionEvent("ProdEvent789");
        Stats stats = new Stats(300, 100, 50, 500, 200);

        Event event = new Event(
                "2024-12-04T10:00:00", // dateStart
                host,                  // host
                "Event123",            // id
                true,                  // isCohost
                owner,                 // owner
                production,            // production
                stats,                 // stats
                2                      // type
        );

        // Act
        String result = event.toString();

        // Assert
        assertThat(result).contains("2024-12-04T10:00:00");
        assertThat(result).contains("Main Hall");
        assertThat(result).contains("HostRef123");
        assertThat(result).contains("Event123");
        assertThat(result).contains("true");
        assertThat(result).contains("OwnerRef456");
        assertThat(result).contains("ProdEvent789");
        assertThat(result).contains("300");
        assertThat(result).contains("100");
        assertThat(result).contains("50");
        assertThat(result).contains("500");
        assertThat(result).contains("200");
        assertThat(result).contains("2");
    }
}

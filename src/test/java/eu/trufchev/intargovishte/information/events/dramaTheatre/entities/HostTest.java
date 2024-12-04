package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Host;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HostTest {

    @Test
    public void testNoArgsConstructor() {
        // Arrange & Act
        Host host = new Host();

        // Assert
        assertThat(host).isNotNull();
        assertThat(host.getLocationStr()).isNull();
        assertThat(host.getRef()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        String locationStr = "Drama Theatre";
        String ref = "ref123";

        // Act
        Host host = new Host(locationStr, ref);

        // Assert
        assertThat(host.getLocationStr()).isEqualTo(locationStr);
        assertThat(host.getRef()).isEqualTo(ref);
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Host host = new Host();
        String locationStr = "Comedy Club";
        String ref = "ref456";

        // Act
        host.setLocationStr(locationStr);
        host.setRef(ref);

        // Assert
        assertThat(host.getLocationStr()).isEqualTo(locationStr);
        assertThat(host.getRef()).isEqualTo(ref);
    }

    @Test
    public void testToString() {
        // Arrange
        Host host = new Host("Drama Theatre", "ref789");

        // Act
        String result = host.toString();

        // Assert
        assertThat(result).isEqualTo("Host(locationStr=Drama Theatre, ref=ref789)");
    }
}

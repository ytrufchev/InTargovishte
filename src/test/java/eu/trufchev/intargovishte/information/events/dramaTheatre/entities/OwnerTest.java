package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Owner;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerTest {

    @Test
    public void testNoArgsConstructor() {
        // Arrange & Act
        Owner owner = new Owner();

        // Assert
        assertThat(owner).isNotNull();
        assertThat(owner.getRef()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        String ref = "owner-ref-123";

        // Act
        Owner owner = new Owner(ref);

        // Assert
        assertThat(owner.getRef()).isEqualTo(ref);
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Owner owner = new Owner();
        String ref = "owner-ref-456";

        // Act
        owner.setRef(ref);

        // Assert
        assertThat(owner.getRef()).isEqualTo(ref);
    }

    @Test
    public void testToString() {
        // Arrange
        String ref = "owner-ref-789";
        Owner owner = new Owner(ref);

        // Act
        String result = owner.toString();

        // Assert
        assertThat(result).isEqualTo("Owner(ref=owner-ref-789)");
    }
}

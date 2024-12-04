package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.ProductionEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductionEventTest {

    @Test
    public void testNoArgsConstructor() {
        // Arrange & Act
        ProductionEvent productionEvent = new ProductionEvent();

        // Assert
        assertThat(productionEvent).isNotNull();
        assertThat(productionEvent.getId()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        String id = "event123";

        // Act
        ProductionEvent productionEvent = new ProductionEvent(id);

        // Assert
        assertThat(productionEvent.getId()).isEqualTo(id);
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        ProductionEvent productionEvent = new ProductionEvent();

        // Act
        productionEvent.setId("event456");

        // Assert
        assertThat(productionEvent.getId()).isEqualTo("event456");
    }

    @Test
    public void testToString() {
        // Arrange
        ProductionEvent productionEvent = new ProductionEvent("event789");

        // Act
        String result = productionEvent.toString();

        // Assert
        assertThat(result).isEqualTo("ProductionEvent(id=event789)");
    }
}

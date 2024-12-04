package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StatsTest {

    @Test
    public void testStatsGettersAndSetters() {
        // Arrange
        Stats stats = new Stats();

        // Act
        stats.setCapacity(300);
        stats.setReserved(100);
        stats.setSelled(50);
        stats.setTotalAmount(500);
        stats.setValidated(200);

        // Assert
        assertThat(stats.getCapacity()).isEqualTo(300);
        assertThat(stats.getReserved()).isEqualTo(100);
        assertThat(stats.getSelled()).isEqualTo(50);
        assertThat(stats.getTotalAmount()).isEqualTo(500);
        assertThat(stats.getValidated()).isEqualTo(200);
    }

    @Test
    public void testStatsConstructorAndToString() {
        // Arrange
        Stats stats = new Stats(300, 100, 50, 500, 200);

        // Act & Assert
        assertThat(stats.getCapacity()).isEqualTo(300);
        assertThat(stats.getReserved()).isEqualTo(100);
        assertThat(stats.getSelled()).isEqualTo(50);
        assertThat(stats.getTotalAmount()).isEqualTo(500);
        assertThat(stats.getValidated()).isEqualTo(200);

        // Testing the toString method
        String statsString = stats.toString();
        assertThat(statsString).contains("300");
        assertThat(statsString).contains("100");
        assertThat(statsString).contains("50");
        assertThat(statsString).contains("500");
        assertThat(statsString).contains("200");
    }

    @Test
    public void testStatsEquality() {
        // Arrange
        Stats stats1 = new Stats(300, 100, 50, 500, 200);
        Stats stats2 = new Stats(300, 100, 50, 500, 200);
        Stats stats3 = new Stats(400, 150, 60, 600, 250);

        // Act & Assert
        assertThat(stats1.toString()).isEqualTo(stats2.toString()); // Same values
        assertThat(stats1.toString()).isNotEqualTo(stats3.toString()); // Different values
    }
}

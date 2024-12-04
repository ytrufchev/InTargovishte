package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Play;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayTest {

    @Test
    public void testNoArgsConstructor() {
        // Arrange & Act
        Play play = new Play();

        // Assert
        assertThat(play).isNotNull();
        assertThat(play.getId()).isNull();
        assertThat(play.getTitle()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        List<String> startDatesList = List.of("2024-12-01", "2024-12-02");
        String startDatesString = String.join(",", startDatesList);

        // Act
        Play play = new Play("1", 120, 12, "Hamlet", "large.jpg", "medium.jpg", "small.jpg", "Main Theatre", startDatesString);

        // Assert
        assertThat(play.getId()).isEqualTo("1");
        assertThat(play.getLength()).isEqualTo(120);
        assertThat(play.getMinAgeRestriction()).isEqualTo(12);
        assertThat(play.getTitle()).isEqualTo("Hamlet");
        assertThat(play.getLargePhoto()).isEqualTo("large.jpg");
        assertThat(play.getMediumPhoto()).isEqualTo("medium.jpg");
        assertThat(play.getSmallPhoto()).isEqualTo("small.jpg");
        assertThat(play.getPlaceName()).isEqualTo("Main Theatre");
        assertThat(play.getStartDates()).containsExactlyElementsOf(startDatesList);
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Play play = new Play();

        // Act
        play.setId("2");
        play.setLength(90);
        play.setMinAgeRestriction(18);
        play.setTitle("Macbeth");
        play.setLargePhoto("large2.jpg");
        play.setMediumPhoto("medium2.jpg");
        play.setSmallPhoto("small2.jpg");
        play.setPlaceName("Grand Theatre");
        play.setStartDates(List.of("2024-12-05", "2024-12-06"));

        // Assert
        assertThat(play.getId()).isEqualTo("2");
        assertThat(play.getLength()).isEqualTo(90);
        assertThat(play.getMinAgeRestriction()).isEqualTo(18);
        assertThat(play.getTitle()).isEqualTo("Macbeth");
        assertThat(play.getLargePhoto()).isEqualTo("large2.jpg");
        assertThat(play.getMediumPhoto()).isEqualTo("medium2.jpg");
        assertThat(play.getSmallPhoto()).isEqualTo("small2.jpg");
        assertThat(play.getPlaceName()).isEqualTo("Grand Theatre");
        assertThat(play.getStartDates()).containsExactly("2024-12-05", "2024-12-06");
    }

    @Test
    public void testSetStartDates() {
        // Arrange
        Play play = new Play();
        List<String> startDatesList = List.of("2024-11-20", "2024-11-21");

        // Act
        play.setStartDates(startDatesList);

        // Assert
        assertThat(play.getStartDates()).containsExactlyElementsOf(startDatesList);
    }

    @Test
    public void testGetStartDates() {
        // Arrange
        Play play = new Play();
        play.setStartDates(List.of("2024-11-25", "2024-11-26"));

        // Act
        List<String> startDates = play.getStartDates();

        // Assert
        assertThat(startDates).containsExactly("2024-11-25", "2024-11-26");
    }

    @Test
    public void testToString() {
        // Arrange
        Play play = new Play("3", 150, 16, "Othello", "large3.jpg", "medium3.jpg", "small3.jpg", "City Theatre", "2024-11-30");

        // Act
        String result = play.toString();

        // Assert
        assertThat(result).isEqualTo(
                "Play(id=3, length=150, minAgeRestriction=16, title=Othello, largePhoto=large3.jpg, mediumPhoto=medium3.jpg, smallPhoto=small3.jpg, placeName=City Theatre, startDates=[2024-11-30])"
        );
    }
}

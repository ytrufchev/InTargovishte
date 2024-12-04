package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.GetPlaysRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GetPlaysRequestTest {

    @Test
    public void testNoArgsConstructor() {
        // Arrange & Act
        GetPlaysRequest request = new GetPlaysRequest();

        // Assert
        assertThat(request).isNotNull();
        assertThat(request.getObj()).isNull();
        assertThat(request.getSkip()).isEqualTo(0);
        assertThat(request.getLimit()).isEqualTo(0);
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        String obj = "Drama";
        int skip = 5;
        int limit = 10;

        // Act
        GetPlaysRequest request = new GetPlaysRequest(obj, skip, limit);

        // Assert
        assertThat(request.getObj()).isEqualTo(obj);
        assertThat(request.getSkip()).isEqualTo(skip);
        assertThat(request.getLimit()).isEqualTo(limit);
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        GetPlaysRequest request = new GetPlaysRequest();
        String obj = "Comedy";
        int skip = 2;
        int limit = 20;

        // Act
        request.setObj(obj);
        request.setSkip(skip);
        request.setLimit(limit);

        // Assert
        assertThat(request.getObj()).isEqualTo(obj);
        assertThat(request.getSkip()).isEqualTo(skip);
        assertThat(request.getLimit()).isEqualTo(limit);
    }

    @Test
    public void testToString() {
        // Arrange
        GetPlaysRequest request = new GetPlaysRequest("Tragedy", 0, 50);

        // Act
        String result = request.toString();

        // Assert
        assertThat(result).isEqualTo("GetPlaysRequest(obj=Tragedy, skip=0, limit=50)");
    }
}

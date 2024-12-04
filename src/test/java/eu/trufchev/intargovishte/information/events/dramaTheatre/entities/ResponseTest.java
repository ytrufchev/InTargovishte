package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Response;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Production;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseTest {

    @Test
    public void testNoArgsConstructor() {
        // Arrange & Act
        Response response = new Response();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isNull();
        assertThat(response.getProductions()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        String status = "success";
        List<Production> productions = List.of(
                new Production("prod1", 120, 12, null, null, "Title 1", null),
                new Production("prod2", 150, 16, null, null, "Title 2", null)
        );

        // Act
        Response response = new Response(status, productions);

        // Assert
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getProductions()).isEqualTo(productions);
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Response response = new Response();
        String status = "failure";
        List<Production> productions = List.of(
                new Production("prod3", 90, 10, null, null, "Title 3", null)
        );

        // Act
        response.setStatus(status);
        response.setProductions(productions);

        // Assert
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getProductions()).isEqualTo(productions);
    }

    @Test
    public void testToString() {
        // Arrange
        Response response = new Response(
                "success",
                List.of(new Production("prod1", 120, 12, null, null, "Title 1", null))
        );

        // Act
        String result = response.toString();

        // Assert
        assertThat(result).contains("status=success");
        assertThat(result).contains("Productions=[Production(id=prod1, length=120, minAgeRestriction=12, photos=null, tagRefs=null, title=Title 1, upcomingEvents=null)]");
    }
}

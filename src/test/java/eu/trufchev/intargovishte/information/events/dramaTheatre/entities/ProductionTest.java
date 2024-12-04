package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Photo;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.TagRef;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.UpcomingEvent;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Production;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductionTest {

    @Test
    public void testNoArgsConstructor() {
        // Arrange & Act
        Production production = new Production();

        // Assert
        assertThat(production).isNotNull();
        assertThat(production.getId()).isNull();
        assertThat(production.getLength()).isEqualTo(0);
        assertThat(production.getMinAgeRestriction()).isEqualTo(0);
        assertThat(production.getPhotos()).isNull();
        assertThat(production.getTagRefs()).isNull();
        assertThat(production.getTitle()).isNull();
        assertThat(production.getUpcomingEvents()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Arrange
        String id = "123";
        int length = 120;
        int minAgeRestriction = 18;
        Poster poster = new Poster("large", "medium", "small");
        Photo photo = new Photo(poster);
        TagRef tagRef = new TagRef("tag1");
        String type = "Point";
        List<Double> coordinates = Arrays.asList(42.0, 25.0);
        GeoPoint geoPoint = new GeoPoint(type, coordinates);
        Location location = new Location("dt trg","targovishte", "bg", "bulgaria", 43.12, 25.11, "dt", "7700", "GMT+3");
        UpcomingEvent upcomingEvent = new UpcomingEvent("event1", geoPoint, location);
        List<TagRef> tagRefs = Arrays.asList(tagRef);
        List<UpcomingEvent> upcomingEvents = Arrays.asList(upcomingEvent);
        String title = "Play Title";

        // Act
        Production production = new Production(id, length, minAgeRestriction, photo, tagRefs, title, upcomingEvents);

        // Assert
        assertThat(production.getId()).isEqualTo(id);
        assertThat(production.getLength()).isEqualTo(length);
        assertThat(production.getMinAgeRestriction()).isEqualTo(minAgeRestriction);
        assertThat(production.getPhotos()).isEqualTo(photo);
        assertThat(production.getTagRefs()).isEqualTo(tagRefs);
        assertThat(production.getTitle()).isEqualTo(title);
        assertThat(production.getUpcomingEvents()).isEqualTo(upcomingEvents);
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Production production = new Production();

        // Act
        production.setId("456");
        production.setLength(100);
        production.setMinAgeRestriction(16);
        Poster poster = new Poster("large2.jpg", "medium2.jpg", "small2.jpg");
        production.setPhotos(new Photo(poster));
        production.setTagRefs(Arrays.asList(new TagRef("tag2")));
        production.setTitle("New Play Title");
        Location location = new Location("dt", "targovishte", "bg", "bulgaria", 43.12, 25.16, "dt trg", "7700", "GMT+#");
        String type = "Point";
        List<Double> coordinates = Arrays.asList(42.0, 25.0);
        GeoPoint geoPoint = new GeoPoint(type, coordinates);
        production.setUpcomingEvents(Arrays.asList(new UpcomingEvent("event2", geoPoint, location)));

        // Assert
        assertThat(production.getId()).isEqualTo("456");
        assertThat(production.getLength()).isEqualTo(100);
        assertThat(production.getMinAgeRestriction()).isEqualTo(16);
        assertThat(production.getPhotos().getPoster().getLarge()).isEqualTo("large2.jpg");
        assertThat(production.getTagRefs().get(0).getSomeField()).isEqualTo("tag2");
        assertThat(production.getTitle()).isEqualTo("New Play Title");
        assertThat(production.getUpcomingEvents().get(0).getEventId()).isEqualTo("event2");
    }

    @Test
    public void testToString() {
        // Arrange
        TagRef tagRef = new TagRef("tag1");
        Location location = new Location("dt", "targovishte", "bg", "bulgaria", 43.12, 25.16, "dt trg", "7700", "GMT+#");
        String type = "Point";
        List<Double> coordinates = Arrays.asList(42.0, 25.0);
        GeoPoint geoPoint = new GeoPoint(type, coordinates);
        UpcomingEvent upcomingEvent = new UpcomingEvent("event1", geoPoint, location);
        Poster poster = new Poster("large.jpg", "medium.jps", "small.jpg");
        Photo photo = new Photo(poster);
        List<TagRef> tagRefs = Arrays.asList(tagRef);
        List<UpcomingEvent> upcomingEvents = Arrays.asList(upcomingEvent);
        Production production = new Production("123", 120, 18, photo, tagRefs, "Play Title", upcomingEvents);

        // Act
        String result = production.toString();

        // Assert
        assertThat(result).isEqualTo("Production(id=123, length=120, minAgeRestriction=18, photos=Photo(poster=Poster(large=large.jpg, medium=medium.jps, small=small.jpg)), tagRefs=[TagRef(someField=tag1)], title=Play Title, upcomingEvents=[UpcomingEvent(EventId=event1, geoPoint=GeoPoint(type=Point, coordinates=[42.0, 25.0]), location=Location(address=dt, cityName=targovishte, countryCode=bg, countryName=bulgaria, lat=43.12, lng=25.16, placeName=dt trg, postCode=7700, timezone=GMT+#))])");
    }
}

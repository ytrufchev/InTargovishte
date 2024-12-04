package eu.trufchev.intargovishte.information.events.dramaTheatre.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TagRefTest {

    @Test
    public void testTagRefGettersAndSetters() {
        // Arrange
        TagRef tagRef = new TagRef();

        // Act
        tagRef.setSomeField("ExampleValue");

        // Assert
        assertThat(tagRef.getSomeField()).isEqualTo("ExampleValue");
    }

    @Test
    public void testTagRefConstructorAndToString() {
        // Arrange
        TagRef tagRef = new TagRef("ExampleValue");

        // Act & Assert
        assertThat(tagRef.getSomeField()).isEqualTo("ExampleValue");

        // Testing the toString method
        String tagRefString = tagRef.toString();
        assertThat(tagRefString).contains("ExampleValue");
    }

    @Test
    public void testTagRefEquality() {
        // Arrange
        TagRef tagRef1 = new TagRef("ExampleValue");
        TagRef tagRef2 = new TagRef("ExampleValue");
        TagRef tagRef3 = new TagRef("DifferentValue");

        // Act & Assert
        assertThat(tagRef1.toString()).isEqualTo(tagRef2.toString()); // Same values
        assertThat(tagRef1.toString()).isNotEqualTo(tagRef3.toString()); // Different values
    }
}

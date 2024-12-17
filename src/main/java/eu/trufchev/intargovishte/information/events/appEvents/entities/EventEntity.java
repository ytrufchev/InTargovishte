package eu.trufchev.intargovishte.information.events.appEvents.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content", length=25000)
    private String content;
    @Column(name = "publisher")
    private Long user;
    @Column(name = "image", length = 500000)
    private String image;
    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Long date;
    @Column(name = "location")
    private String location;
    @Column(name = "status")
    private StatusENUMS status;
    @ElementCollection
    @JsonIgnore // Prevent recursion during serialization
    private List<AppEventLike> likes;
//    @Column(name = "comments")
//    @ManyToOne
//    private List<Comment> comments;
}

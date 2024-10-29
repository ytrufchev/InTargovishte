package eu.trufchev.intargovishte.information.events.appEvents.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import eu.trufchev.intargovishte.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

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
    @Column(name = "content")
    private String content;
    @Column(name = "publisher")
    private User user;
    @Column(name = "image")
    private String image;
    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Instant date;
//    @Column(name = "likes")
//    @ManyToOne
//    private List<Likes> likes;
//    @Column(name = "comments")
//    @ManyToOne
//    private List<Comment> comments;
}

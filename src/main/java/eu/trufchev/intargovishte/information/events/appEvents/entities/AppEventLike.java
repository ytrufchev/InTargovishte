package eu.trufchev.intargovishte.information.events.appEvents.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class AppEventLike {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ElementCollection
    @Column(name = "event_id")
    private Long event;

    @ElementCollection
    @Column(name = "user_id", nullable = false)
    private Long user;

    @Column(name = "liked_at", nullable = false)
    private Instant likedAt;
}

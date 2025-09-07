package eu.trufchev.intargovishte.social.publication.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import eu.trufchev.intargovishte.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "publication_id", nullable = false)
    @JsonBackReference
    private Publication publication;
}

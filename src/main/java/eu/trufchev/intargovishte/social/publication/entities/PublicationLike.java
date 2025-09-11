package eu.trufchev.intargovishte.social.publication.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import eu.trufchev.intargovishte.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PublicationLike {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "publication_id", nullable = false)
    @JsonBackReference
    private Publication publication;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

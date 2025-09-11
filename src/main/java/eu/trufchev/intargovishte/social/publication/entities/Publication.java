package eu.trufchev.intargovishte.social.publication.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "owner")
    private long owner; // the user id of the person that created the publication
    @Column(name = "createdAt")
    private Instant createdAt;
    @Column(name = "image", length = 500000)
    private String image;
    @Column(name = "text", length = 140)
    private String text;
    @Column(name = "likescount")
    private int likescount;
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PublicationLike> likes;
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments;
}

package eu.trufchev.intargovishte.information.news.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class News {
    @Id
    private Long id;
    @Column(name = "date")
    private String date;
    @Column(name = "title")
    private String title;
    @Column(name = "content", length = 5000)
    private String content;
    @Column(name = "link")
    private String link;
    @Column(name = "image")
    private String image;
}

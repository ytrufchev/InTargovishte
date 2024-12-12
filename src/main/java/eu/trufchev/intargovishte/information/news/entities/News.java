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
    @Column(columnDefinition = "TEXT", name = "content", length = 20000)
    private String content;
    @Column(name = "link", length = 1024)
    private String link;
    @Column(name = "image")
    private String image;
}

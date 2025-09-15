package eu.trufchev.intargovishte.affiliate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignEntity {

    @Id
    private Integer id; // Profitshare campaign ID

    private String name;
    private String commissionType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String url;

    @Column(length = 500)
    private String referralLink; // Generated once, reused later

    private boolean active;
}

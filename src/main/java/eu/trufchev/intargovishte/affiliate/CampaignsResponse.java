package eu.trufchev.intargovishte.affiliate;

import jakarta.persistence.Entity;
import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CampaignsResponse {
    CampaignsResult result;
}
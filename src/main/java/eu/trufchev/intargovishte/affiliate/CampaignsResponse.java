package eu.trufchev.intargovishte.affiliate;

import jakarta.persistence.Entity;
import lombok.*;

@Value
@Builder
@Getter
@Setter
public class CampaignsResponse {
    CampaignsResult result;
}
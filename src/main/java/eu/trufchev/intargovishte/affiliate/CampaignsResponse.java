package eu.trufchev.intargovishte.affiliate;

import jakarta.persistence.Entity;
import lombok.*;

@Value
@Builder
public class CampaignsResponse {
    CampaignsResult result;
}
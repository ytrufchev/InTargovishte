package eu.trufchev.intargovishte.affiliate;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.List;

@Value
@Builder
@Getter
@Setter
public class CampaignsResult {
    Paginator paginator;
    List<Campaign> campaigns;
}
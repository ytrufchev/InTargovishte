package eu.trufchev.intargovishte.affiliate;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.Map;
@Value
@Builder
public class Campaign {
    int id;
    String name;
    String commissionType;
    String startDate;
    String endDate;
    String url;
    Map<String, Banner> banners;
}


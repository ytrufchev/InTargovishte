package eu.trufchev.intargovishte.affiliate;

import jakarta.persistence.Entity;
import lombok.*;

@Value
@Builder
public class Banner {
    int width;
    int height;
    String src;
}
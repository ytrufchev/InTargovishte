package eu.trufchev.intargovishte.affiliate;

import jakarta.persistence.Entity;
import lombok.*;

@Value
@Builder
@Setter
@Getter
public class Paginator {
    int itemsPerPage;
    int currentPage;
    int totalPages;
}
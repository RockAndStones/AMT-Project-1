package ch.heigvd.amt.stoneoverflow.application.pagination;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaginationDTO {
    int limit;
    int itemRepoSize;
    int totalPages;
    int currentPage;
    int startItem;
    int lastItem;
    int startPage;
    int lastPage;
}

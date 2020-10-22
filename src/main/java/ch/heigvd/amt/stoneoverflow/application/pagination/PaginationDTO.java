package ch.heigvd.amt.stoneoverflow.application.pagination;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaginationDTO {
    int limit;
    int allQuestions;
    int totalPages;
    int currentPage;
    int startQuestion;
    int lastQuestion;
}

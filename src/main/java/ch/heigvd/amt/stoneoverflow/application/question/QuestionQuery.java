package ch.heigvd.amt.stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class QuestionQuery {
    @Builder.Default
    private QuestionType type = QuestionType.UNCLASSIFIED;

    @Builder.Default
    private QuestionQuerySortBy sortBy = QuestionQuerySortBy.DATE;

    @Builder.Default
    private boolean sortDescending = true;

    @Builder.Default
    private String searchCondition = "";
}

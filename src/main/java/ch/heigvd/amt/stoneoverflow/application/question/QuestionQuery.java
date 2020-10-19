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
    private boolean byDate = false;

    @Builder.Default
    private boolean byNbResponse = false;

    @Builder.Default
    private boolean byNbVotes = false;

    @Builder.Default
    private boolean byNbViews = false;

    @Builder.Default
    private String searchCondition = "";
}

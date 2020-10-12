package ch.heigvd.amt.StoneOverflow.application.Question;

import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class QuestionQuery {
    @Builder.Default
    private QuestionType type = QuestionType.SQL;

    @Builder.Default
    private boolean byDate = true;

    @Builder.Default
    private boolean byNbResponse = false;

    @Builder.Default
    private boolean byNbVotes = false;

    @Builder.Default
    private boolean byNbViews = false;
}

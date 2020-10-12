package ch.heigvd.amt.StoneOverflow.application.Question;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class QuestionQuery {
    @Builder.Default
    private boolean sqlSearch = false;

    @Builder.Default
    private boolean byDate = false;

    @Builder.Default
    private boolean byNbResponse = false;

    @Builder.Default
    private boolean byNbVotes = false;

    @Builder.Default
    private boolean byNbVues = false;
}

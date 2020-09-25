package ch.heigvd.amt.StoneOverflow.application.Question;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class QuestionQuery {
    @Builder.Default
    private boolean sqlSearch = true;
}

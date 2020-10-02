package ch.heigvd.amt.stoneoverflow.application.question;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class QuestionQuery {
    @Builder.Default
    private boolean sqlSearch = false;
}

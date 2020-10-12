package ch.heigvd.amt.StoneOverflow.application.answer;

import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
@EqualsAndHashCode
public class AddAnswerCommand {

    @Builder.Default
    private QuestionId answerTo = null;

    @Builder.Default
    private String description = "No content";

    @Builder.Default
    private String creator = "Anonymous";

    @Builder.Default
    private int nbVotes = 0;

    @Builder.Default
    private Date date = null;

}

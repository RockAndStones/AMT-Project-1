package ch.heigvd.amt.StoneOverflow.application.Question;

import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionType;
import ch.heigvd.amt.StoneOverflow.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
@EqualsAndHashCode
public class AddQuestionCommand {
    @Builder.Default
    private String title = "My default question";

    @Builder.Default
    private String description = "No content";

    @Builder.Default
    private UserId creatorId = new UserId();

    @Builder.Default
    private String creator = "Anonymous";

    @Builder.Default
    private int nbVotes = 0;

    @Builder.Default
    private int nbViews = 0;

    @Builder.Default
    private Date date = null;

    @Builder.Default
    private QuestionType type = QuestionType.UNCLASSIFIED;
}

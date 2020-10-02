package ch.heigvd.amt.stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class AddQuestionCommand {
    @Builder.Default
    private String title = "My default question";

    @Builder.Default
    private String description = "No content";

    @Builder.Default
    private String creator = "Anonymous";

    @Builder.Default
    private int nbVotes = 0;

    @Builder.Default
    private QuestionType type = QuestionType.UNCLASSIFIED;
}

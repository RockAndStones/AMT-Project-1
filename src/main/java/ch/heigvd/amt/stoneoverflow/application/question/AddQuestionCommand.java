package ch.heigvd.amt.stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

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
    private AtomicInteger nbViews = new AtomicInteger(0);

    @Builder.Default
    private Date date = new Date(System.currentTimeMillis());

    @Builder.Default
    private QuestionType type = QuestionType.UNCLASSIFIED;
}

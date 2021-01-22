package ch.heigvd.amt.stoneoverflow.domain.question;

import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

public class QuestionTest {

    @Test
    public void deepCloneCreatesNewObject() {
        Question question = Question.builder()
                .title("My test Question")
                .description("my question description")
                .creator("test")
                .build();
        Question question2 = question.deepClone();

        assertEquals(question, question2);
        assertNotSame(question, question2);
    }

    @Test
    public void questionIdShouldBeAutomaticallyGenerated() {
        Question question = Question.builder()
                .title("My test Question")
                .description("my question description")
                .creator("test")
                .build();

        assertNotNull(question.getId());
    }

    @Test
    public void questionShouldBeCategorized() {
        Question question = Question.builder()
                .title("My test Question")
                .description("my question description")
                .creator("test")
                .build();

        question.categorizeAs(QuestionType.C);

        assertEquals(question.getQuestionType(), QuestionType.C);
    }

    @Test
    public void questionShouldAddView() {
        Question question = Question.builder()
                .title("My test Question")
                .description("my question description")
                .creator("test")
                .build();

        question.addView();
        question.addView();

        assertEquals(question.getNbViews().get(), 2);
    }
}

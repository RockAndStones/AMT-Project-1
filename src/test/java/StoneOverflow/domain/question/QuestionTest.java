package StoneOverflow.domain.question;

import ch.heigvd.amt.StoneOverflow.domain.Question.Question;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}

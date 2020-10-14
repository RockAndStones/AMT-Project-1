package stoneoverflow.domain.answer;

import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnswerTest {
    @Test
    public void deepCloneCreatesNewObject() {
        Answer answer = Answer.builder()
                .answerTo(new QuestionId())
                .description("My test Answer")
                .creatorId(new UserId())
                .creator("test")
                .nbVotes(17).build();
        Answer answer2 = answer.deepClone();

        assertEquals(answer, answer2);
        assertNotSame(answer, answer2);
    }

    @Test
    public void answerIdShouldBeAutomaticallyGenerated() {
        Answer answer = Answer.builder()
                .answerTo(new QuestionId())
                .description("My test Answer")
                .creatorId(new UserId())
                .creator("test")
                .nbVotes(17).build();

        assertNotNull(answer.getId());
    }
}

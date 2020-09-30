package StoneOverflow.domain.question;

import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionIdTest {
    @Test
    public void questionIdShouldGenerateUUID() {
        QuestionId questionId = new QuestionId();
        assertNotNull(questionId.asString());
    }

    @Test
    public void questionIdShouldReturnException() {
        Throwable exception = assertThrows(NullPointerException.class, () -> new QuestionId((String) null));
    }

    @Test
    public void questionIdShouldInitFromExistingUUID() {
        String validUUID = "05f7601b-43b4-4814-b9a0-fd2b07a70b18";
        UUID uuid = UUID.fromString(validUUID);

        QuestionId questionId = new QuestionId(validUUID);
        QuestionId questionId2 = new QuestionId(uuid);

        assertEquals(questionId.asString(), validUUID);
        assertEquals(questionId2.asString(), validUUID);
    }
}

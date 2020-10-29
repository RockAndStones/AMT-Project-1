package stoneoverflow.domain.answer;

import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AnswerIdTest {
    @Test
    public void answerIdShouldGenerateUUID() {
        AnswerId answerId = new AnswerId();
        assertNotNull(answerId.asString());
    }

    @Test
    public void answerIdShouldReturnException() {
        Throwable exception = assertThrows(NullPointerException.class, () -> new AnswerId((String) null));
    }

    @Test
    public void answerIdShouldInitFromExistingUUID() {
        String validUUID = "05f7601b-43b4-4814-b9a0-fd2b07a70b18";
        UUID uuid = UUID.fromString(validUUID);

        AnswerId answerId =  new AnswerId(validUUID);
        AnswerId answerId2 = new AnswerId(uuid);

        assertEquals(answerId.asString(), validUUID);
        assertEquals(answerId2.asString(), validUUID);
    }
}

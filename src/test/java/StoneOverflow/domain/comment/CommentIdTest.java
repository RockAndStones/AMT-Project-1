package StoneOverflow.domain.comment;

import ch.heigvd.amt.StoneOverflow.domain.answer.AnswerId;
import ch.heigvd.amt.StoneOverflow.domain.comment.CommentId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentIdTest {
    @Test
    public void commentIdShouldGenerateUUID() {
        CommentId commentId = new CommentId();
        assertNotNull(commentId.asString());
    }

    @Test
    public void commentIdShouldReturnException() {
        Throwable exception = assertThrows(NullPointerException.class, () -> new CommentId((String) null));
    }

    @Test
    public void commentIdShouldInitFromExistingUUID() {
        String validUUID = "05f7601b-43b4-4814-b9a0-fd2b07a70b18";
        UUID uuid = UUID.fromString(validUUID);

        CommentId commentId =  new CommentId(validUUID);
        CommentId commentId2 = new CommentId(uuid);

        assertEquals(commentId.asString(), validUUID);
        assertEquals(commentId2.asString(), validUUID);
    }
}

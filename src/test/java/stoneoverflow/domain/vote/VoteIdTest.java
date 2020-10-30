package stoneoverflow.domain.vote;

import ch.heigvd.amt.stoneoverflow.domain.vote.VoteId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoteIdTest {
    @Test
    public void voteIddShouldGenerateUUID() {
        VoteId voteId = new VoteId();
        assertNotNull(voteId.asString());
    }

    @Test
    public void voteIdShouldReturnException() {
        Throwable exception = assertThrows(NullPointerException.class, () -> new VoteId((String) null));
    }

    @Test
    public void voteIdShouldInitFromExistingUUID() {
        String validUUID = "05f7601b-43b4-4814-b9a0-fd2b07a70b18";
        UUID uuid = UUID.fromString(validUUID);

        VoteId voteId1 = new VoteId(validUUID);
        VoteId voteId2 = new VoteId(uuid);

        assertEquals(voteId1.asString(), validUUID);
        assertEquals(voteId2.asString(), validUUID);
    }
}

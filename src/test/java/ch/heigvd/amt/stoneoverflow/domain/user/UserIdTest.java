package ch.heigvd.amt.stoneoverflow.domain.user;

import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserIdTest {
    @Test
    public void userIdShouldGenerateUUID() {
        UserId userId = new UserId();
        assertNotNull(userId.asString());
    }

    @Test
    public void userIdShouldInitFromExistingUUID() {
        String validUUID = "05f7601b-43b4-4814-b9a0-fd2b07a70b18";
        UUID uuid = UUID.fromString(validUUID);

        UserId userId = new UserId(validUUID);
        UserId userId2 = new UserId(uuid);

        assertEquals(userId.asString(), validUUID);
        assertEquals(userId2.asString(), validUUID);
    }
}

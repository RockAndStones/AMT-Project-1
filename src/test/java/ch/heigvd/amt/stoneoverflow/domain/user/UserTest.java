package ch.heigvd.amt.stoneoverflow.domain.user;

import ch.heigvd.amt.stoneoverflow.domain.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    public void deepCloneCreatesNewObject() {
        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("pass")
                .build();
        User u2 = u.deepClone();

        assertEquals(u, u2);
        assertNotSame(u, u2);
    }

    @Test
    public void userIdShouldBeAutomaticallGenerated() {
        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("pass")
                .build();

        assertNotNull(u.getId());
    }

    @Test
    public void userPasswordShouldBeHashed() {
        String password = "pass";

        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword(password)
                .build();

        assertNotEquals(password, u.getHashedPassword());
    }

    @Test
    public void userShouldAuthenticateWithCorrectPassword() {
        String password = "pass";

        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword(password)
                .build();

        assertTrue(u.authenticate(password));
    }

    @Test
    public void userShouldNotAuthenticateWithIncorrectPassword() {
        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("pass")
                .build();

        assertFalse(u.authenticate("Different password"));
    }
}
package StoneOverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.StoneOverflow.domain.user.User;
import ch.heigvd.amt.StoneOverflow.infrastructure.persistance.exception.IntegrityConstraintViolationException;
import ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserRepositoryTest {
    private InMemoryUserRepository inMemoryUserRepository;

    @BeforeEach
    public void initInMemoryUserRepository() {
        inMemoryUserRepository = new InMemoryUserRepository();
    }

    @Test
    public void shouldSaveUser() {
        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("pass")
                .build();
        inMemoryUserRepository.save(u);

        Optional<User> foundUser = inMemoryUserRepository.findById(u.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), u);
    }

    @Test
    public void shouldThrowWhenSavingDuplicatedUsername() {
        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("pass")
                .build();
        User u2 = User.builder()
                .username("test")
                .email("nottest@test.com")
                .firstName("Not First name")
                .lastName("Not Last name")
                .plaintextPassword("password")
                .build();

        inMemoryUserRepository.save(u);
        assertThrows(IntegrityConstraintViolationException.class, () -> inMemoryUserRepository.save(u2));
    }

    @Test
    public void shouldFindCorrectUserById() {
        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("pass")
                .build();
        User u2 = User.builder()
                .username("test2")
                .email("test2@test.com")
                .firstName("First name2")
                .lastName("Last name2")
                .plaintextPassword("pass2")
                .build();

        inMemoryUserRepository.save(u);
        inMemoryUserRepository.save(u2);

        Optional<User> foundUser = inMemoryUserRepository.findById(u.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), u);
    }

    @Test
    public void shouldFindCorrectUserByUsername() {
        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("pass")
                .build();
        User u2 = User.builder()
                .username("test2")
                .email("test2@test.com")
                .firstName("First name2")
                .lastName("Last name2")
                .plaintextPassword("pass2")
                .build();

        inMemoryUserRepository.save(u);
        inMemoryUserRepository.save(u2);

        Optional<User> foundUser = inMemoryUserRepository.findByUsername(u.getUsername());
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), u);
    }

    @Test
    public void findShouldDeepCloneUser() {
        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("pass")
                .build();
        inMemoryUserRepository.save(u);

        Optional<User> foundUser = inMemoryUserRepository.findById(u.getId());
        assertTrue(foundUser.isPresent());
        assertNotSame(foundUser.get(), u);

        Optional<User> foundUserByUsername = inMemoryUserRepository.findByUsername(u.getUsername());
        assertTrue(foundUserByUsername.isPresent());
        assertNotSame(foundUserByUsername.get(), u);

        Collection<User> users = inMemoryUserRepository.findAll();
        assertEquals(users.size(), 1);
        assertNotSame(users.toArray()[0], u);
    }

    @Test
    public void removeShouldRemoveUser() {
        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("pass")
                .build();
        inMemoryUserRepository.save(u);

        assertEquals(inMemoryUserRepository.findAll().size(), 1);
        inMemoryUserRepository.remove(u.getId());
        assertEquals(inMemoryUserRepository.findAll().size(), 0);
    }
}

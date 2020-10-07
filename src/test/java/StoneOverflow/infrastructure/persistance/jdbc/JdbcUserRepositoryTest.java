package StoneOverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.StoneOverflow.domain.user.User;
import ch.heigvd.amt.StoneOverflow.infrastructure.persistance.jdbc.JdbcUserRepository;
import ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUserRepositoryTest {
    private JdbcUserRepository jdbcUserRepository;

    @BeforeAll
    public void initJdbcUserRepository() {
        jdbcUserRepository = new JdbcUserRepository();
    }

    @Test
    public void shouldSaveAValidUser() {
        User u = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("pass")
                .build();
        jdbcUserRepository.save(u);

        Optional<User> foundUser = jdbcUserRepository.findById(u.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), u);
    }

    @Test
    void findByUsername() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }
}
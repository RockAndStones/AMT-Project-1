package stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception.IntegrityConstraintViolationException;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcUserRepository;
import com.github.javafaker.Faker;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcUserRepositoryTest {
//    private static JdbcUserRepository jdbcUserRepository;
//    private static Faker faker;
//
//    @BeforeAll
//    static void initTests() {
//        MysqlDataSource ds = new MysqlDataSource();
//        ds.setUser("root");
//        ds.setPassword("mysql");
//        ds.setServerName("127.0.0.1");
//        ds.setPort(3306);
//        ds.setDatabaseName("db_stoneoverflow");
//
//        jdbcUserRepository = new JdbcUserRepository(ds);
//        faker = new Faker();
//    }
//
//    private User generateFakerUser() {
//        return User.builder()
//                .username(faker.name().username())
//                .email(faker.internet().emailAddress())
//                .firstName(faker.name().firstName())
//                .lastName(faker.name().lastName())
//                .plaintextPassword(faker.internet().password(8,
//                        24,
//                        true,
//                        true,
//                        true))
//                .build();
//    }
//
//    @Test
//    public void shouldSaveUser() {
//        User u = generateFakerUser();
//        jdbcUserRepository.save(u);
//
//        Optional<User> foundUser = jdbcUserRepository.findById(u.getId());
//        assertTrue(foundUser.isPresent());
//        assertEquals(foundUser.get(), u);
//    }
//
//    @Test
//    public void shouldThrowWhenSavingDuplicatedUsername() {
//        User u = generateFakerUser();
//
//        User u2 = generateFakerUser().toBuilder()
//                .username(u.getUsername())
//                .build();
//
//        jdbcUserRepository.save(u);
//        assertThrows(IntegrityConstraintViolationException.class, () -> jdbcUserRepository.save(u2));
//    }
//
//    @Test
//    public void shouldFindCorrectUserById() {
//        User u = generateFakerUser();
//        User u2 = generateFakerUser();
//
//        jdbcUserRepository.save(u);
//        jdbcUserRepository.save(u2);
//
//        Optional<User> foundUser = jdbcUserRepository.findById(u.getId());
//        assertTrue(foundUser.isPresent());
//        assertEquals(foundUser.get(), u);
//    }
//
//    @Test
//    public void shouldFindCorrectUserByUsername() {
//        User u = generateFakerUser();
//        User u2 = generateFakerUser();
//
//        jdbcUserRepository.save(u);
//        jdbcUserRepository.save(u2);
//
//        Optional<User> foundUser = jdbcUserRepository.findByUsername(u.getUsername());
//        assertTrue(foundUser.isPresent());
//        assertEquals(foundUser.get(), u);
//    }
//
//    @Test
//    public void findShouldDeepCloneUser() {
//        User u = generateFakerUser();
//        jdbcUserRepository.save(u);
//
//        Optional<User> foundUser = jdbcUserRepository.findById(u.getId());
//        assertTrue(foundUser.isPresent());
//        assertNotSame(foundUser.get(), u);
//
//        Optional<User> foundUserByUsername = jdbcUserRepository.findByUsername(u.getUsername());
//        assertTrue(foundUserByUsername.isPresent());
//        assertNotSame(foundUserByUsername.get(), u);
//    }
//
//    @Test
//    public void removeShouldRemoveUser() {
//        int initialSize = jdbcUserRepository.getRepositorySize();
//        User u = generateFakerUser();
//        jdbcUserRepository.save(u);
//
//        assertEquals(jdbcUserRepository.getRepositorySize(), initialSize + 1);
//        jdbcUserRepository.remove(u.getId());
//        assertEquals(jdbcUserRepository.findAll().size(), initialSize);
//    }
}

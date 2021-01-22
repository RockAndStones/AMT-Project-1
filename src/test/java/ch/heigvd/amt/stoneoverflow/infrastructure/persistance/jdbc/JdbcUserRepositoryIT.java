package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception.IntegrityConstraintViolationException;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcUserRepository;
import com.github.javafaker.Faker;
import java.util.Optional;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import java.io.File;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class JdbcUserRepositoryIT {
    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private JdbcUserRepository jdbcUserRepository;
    private Faker faker;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        // Dynamically import maven dependencies
        // Source: https://stackoverflow.com/a/30694968
        File[] files = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importCompileAndRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();

        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt")
                .addAsLibraries(files);

        return archive;
    }

    @Before
    public void initTests() {
        faker = new Faker();
    }

    private User generateFakerUser() {
        return User.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .plaintextPassword(faker.internet().password(8,
                        24,
                        true,
                        true,
                        true))
                .build();
    }

    @Test
    public void shouldSaveUser() {
        User u = generateFakerUser();
        jdbcUserRepository.save(u);

        Optional<User> foundUser = jdbcUserRepository.findById(u.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), u);
    }

    @Test(expected = IntegrityConstraintViolationException.class)
    public void shouldThrowWhenSavingDuplicatedUsername() {
        User u = generateFakerUser();

        User u2 = generateFakerUser().toBuilder()
                .username(u.getUsername())
                .build();

        jdbcUserRepository.save(u);
        jdbcUserRepository.save(u2);
    }

    @Test
    public void shouldFindCorrectUserById() {
        User u = generateFakerUser();
        User u2 = generateFakerUser();

        jdbcUserRepository.save(u);
        jdbcUserRepository.save(u2);

        Optional<User> foundUser = jdbcUserRepository.findById(u.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), u);
    }

    @Test
    public void shouldFindCorrectUserByUsername() {
        User u = generateFakerUser();
        User u2 = generateFakerUser();

        jdbcUserRepository.save(u);
        jdbcUserRepository.save(u2);

        Optional<User> foundUser = jdbcUserRepository.findByUsername(u.getUsername());
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), u);
    }

    @Test
    public void findShouldDeepCloneUser() {
        User u = generateFakerUser();
        jdbcUserRepository.save(u);

        Optional<User> foundUser = jdbcUserRepository.findById(u.getId());
        assertTrue(foundUser.isPresent());
        assertNotSame(foundUser.get(), u);

        Optional<User> foundUserByUsername = jdbcUserRepository.findByUsername(u.getUsername());
        assertTrue(foundUserByUsername.isPresent());
        assertNotSame(foundUserByUsername.get(), u);
    }

    @Test
    public void removeShouldRemoveUser() {
        int initialSize = jdbcUserRepository.getRepositorySize();
        User u = generateFakerUser();
        jdbcUserRepository.save(u);

        assertEquals(jdbcUserRepository.getRepositorySize(), initialSize + 1);
        jdbcUserRepository.remove(u.getId());
        assertEquals(jdbcUserRepository.findAll().size(), initialSize);
    }
}

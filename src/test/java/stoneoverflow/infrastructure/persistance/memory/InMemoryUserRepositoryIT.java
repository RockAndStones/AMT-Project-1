package stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception.IntegrityConstraintViolationException;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryCommentRepository;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryUserRepository;
import com.github.javafaker.Faker;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InMemoryUserRepositoryIT {
    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    @Named("InMemoryUserRepository")
    private InMemoryUserRepository inMemoryUserRepository;

    private Faker faker;
    private User user;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
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
        generateUser();
    }

    private User generateUser(){
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
        User u = generateUser();
        inMemoryUserRepository.save(u);

        Optional<User> foundUser = inMemoryUserRepository.findById(u.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), u);
    }

    @Test(expected = IntegrityConstraintViolationException.class)
    public void shouldThrowWhenSavingDuplicatedUsername() {
        User u = generateUser();
        User u2 = u;

        inMemoryUserRepository.save(u);
        inMemoryUserRepository.save(u2);
    }

    @Test
    public void shouldFindCorrectUserById() {
        User u = generateUser();
        User u2 = generateUser();

        inMemoryUserRepository.save(u);
        inMemoryUserRepository.save(u2);

        Optional<User> foundUser = inMemoryUserRepository.findById(u.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), u);
    }

    @Test
    public void shouldFindCorrectUserByUsername() {
        User u = generateUser();
        User u2 = generateUser();

        inMemoryUserRepository.save(u);
        inMemoryUserRepository.save(u2);

        Optional<User> foundUser = inMemoryUserRepository.findByUsername(u.getUsername());
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get(), u);
    }

    @Test
    public void findShouldDeepCloneUser() {
        User u = generateUser();
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
        User u = generateUser();
        inMemoryUserRepository.save(u);

        int beforeDelete = inMemoryUserRepository.findAll().size();

        inMemoryUserRepository.remove(u.getId());
        assertEquals(inMemoryUserRepository.findAll().size(), beforeDelete - 1);
    }

    @Test
    public void updateUser() {
        User u = generateUser();
        inMemoryUserRepository.save(u);

        User updated = User.builder()
                .id(u.getId())
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

        inMemoryUserRepository.update(updated);

        assertEquals(inMemoryUserRepository.findById(u.getId()).get(), updated);
    }
}

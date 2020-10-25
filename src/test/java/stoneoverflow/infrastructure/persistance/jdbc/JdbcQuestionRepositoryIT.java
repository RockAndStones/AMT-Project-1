package stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcQuestionRepository;
import com.github.javafaker.Faker;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;

import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class JdbcQuestionRepositoryIT {
    private final static String WARNAME = "arquillian-managed.war";

    @Inject @Named("JdbcQuestionRepository")
    private JdbcQuestionRepository jdbcQuestionRepository;
    private static Faker faker;
    private static User user;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt")
                .addPackages(true, "com.github.javafaker.Faker")
                .addClass(Faker.class);
        return archive;
    }

    @BeforeClass
    public static void initTests() {
        faker = new Faker();
        user = User.builder()
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

    private Question generateFakerQuestion() {
        return Question.builder()
                .title(faker.lorem().sentence())
                .description(faker.lorem().paragraph())
                .creatorId(user.getId())
                .creator(user.getUsername())
                .nbViews(new AtomicInteger(faker.number().numberBetween(0, 1000)))
                .date(faker.date().birthday())
                .build();
    }

    @Test
    public void shouldSaveQuestions() {
        Question q = generateFakerQuestion();
        jdbcQuestionRepository.save(q);

        Optional<Question> foundQuestion = jdbcQuestionRepository.findById(q.getId());
        assertTrue(foundQuestion.isPresent());
        assertEquals(foundQuestion.get(), q);
    }

    @Test
    public void shouldFindQuestions() {
        int initialSize = jdbcQuestionRepository.getRepositorySize();
        jdbcQuestionRepository.save(generateFakerQuestion());
        jdbcQuestionRepository.save(generateFakerQuestion());
        jdbcQuestionRepository.save(generateFakerQuestion());

        assertEquals(initialSize + 3, jdbcQuestionRepository.findAll().size());
    }
}

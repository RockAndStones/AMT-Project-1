package stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuerySortBy;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcAnswerRepository;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcQuestionRepository;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcUserRepository;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcVoteRepository;
import com.github.javafaker.Faker;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(Arquillian.class)
public class JdbcAnswerRepositoryIT {
    private final static String WARNAME = "arquillian-managed.war";

    @Inject @Named("JdbcQuestionRepository")
    private JdbcQuestionRepository jdbcQuestionRepository;

    @Inject @Named("JdbcAnswerRepository")
    private JdbcAnswerRepository jdbcAnswerRepository;

    @Inject @Named("JdbcVoteRepository")
    private JdbcVoteRepository jdbcVoteRepository;

    @Inject
    private JdbcUserRepository jdbcUserRepository;
    private Faker faker;
    private User user;
    private Question question;

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
//                .addClass(Faker.class);
        return archive;
    }

    @Before
    public void initTests() {
        faker = new Faker();
        generateUser();
        generateQuestion();
    }

    private void generateUser() {
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
        jdbcUserRepository.save(user);
    }

    private void generateQuestion() {
        question = Question.builder()
                .title(faker.lorem().sentence())
                .description(faker.lorem().paragraph())
                .creatorId(user.getId())
                .creator(user.getUsername())
                .build();
        jdbcQuestionRepository.save(question);
    }

    private Answer generateFakerAnswer(QuestionId answerTo, Date date) {
        return Answer.builder()
                .answerTo(answerTo)
                .description(faker.lorem().paragraph())
                .creatorId(user.getId())
                .creator(user.getUsername())
                .date(date).build();
    }

    private Answer generateFakerAnswer(QuestionId answerTo) {
        return generateFakerAnswer(answerTo, Date.from(new Date().toInstant().truncatedTo(ChronoUnit.MILLIS)));
    }

    private void addVoteAnswer(AnswerId id) {
        jdbcVoteRepository.save(Vote.builder().votedBy(user.getId()).votedObject(id).build());
    }

    @Test
    public void shouldSaveAnswer() {
        Answer answer = generateFakerAnswer(question.getId());
        jdbcAnswerRepository.save(answer);

        Optional<Answer> foundAnswer = jdbcAnswerRepository.findById(answer.getId());
        assertTrue(foundAnswer.isPresent());
        assertEquals(answer, foundAnswer.get());
    }

    @Test
    public void shouldFindAllAnswers() {
        int initialSize = jdbcAnswerRepository.getRepositorySize();
        jdbcAnswerRepository.save(generateFakerAnswer(question.getId()));
        jdbcAnswerRepository.save(generateFakerAnswer(question.getId()));
        jdbcAnswerRepository.save(generateFakerAnswer(question.getId()));

        assertEquals(initialSize + 3, jdbcAnswerRepository.getRepositorySize());
    }

    @Test
    public void shouldFindAnswersByAnswerTo() {
        generateQuestion();
        jdbcAnswerRepository.save(generateFakerAnswer(question.getId()));
        jdbcAnswerRepository.save(generateFakerAnswer(question.getId()));
        jdbcAnswerRepository.save(generateFakerAnswer(question.getId()));

        assertEquals(3, jdbcAnswerRepository.find(
                AnswerQuery.builder().answerTo(question.getId()).build(),
                0,
                jdbcAnswerRepository.getRepositorySize()).size());
    }

    @Test
    public void shouldFindAnswersByDate() {
        generateQuestion();

        // Expected result
        ArrayList<Answer> answersSortedByDate = new ArrayList<>();
        answersSortedByDate.add(generateFakerAnswer(question.getId(), new Date(System.currentTimeMillis())));
        answersSortedByDate.add(generateFakerAnswer(question.getId(), new Date(System.currentTimeMillis() - 1000)));
        answersSortedByDate.add(generateFakerAnswer(question.getId(), new Date(System.currentTimeMillis() - 2000)));


        // Add the answers not in the same order
        jdbcAnswerRepository.save(answersSortedByDate.get(1));
        jdbcAnswerRepository.save(answersSortedByDate.get(2));
        jdbcAnswerRepository.save(answersSortedByDate.get(0));

        Assert.assertEquals(answersSortedByDate, new ArrayList<>(jdbcAnswerRepository.find(
                AnswerQuery.builder().sortBy(AnswerQuerySortBy.DATE).build(), 0, 3)));
    }

    @Test
    public void shouldFindAnswersByVotes() {
        generateQuestion();

        // Expected result
        ArrayList<Answer> answersSortedByDate = new ArrayList<>();
        answersSortedByDate.add(generateFakerAnswer(question.getId()));
        answersSortedByDate.add(generateFakerAnswer(question.getId()));
        answersSortedByDate.add(generateFakerAnswer(question.getId()));

        // Add the answers not in the same order
        jdbcAnswerRepository.save(answersSortedByDate.get(1));
        jdbcAnswerRepository.save(answersSortedByDate.get(2));
        jdbcAnswerRepository.save(answersSortedByDate.get(0));

        for(int i = 0; i < 10; i++) {
            generateUser();
            addVoteAnswer(answersSortedByDate.get(0).getId());
        }

        for(int i = 0; i < 5; i++) {
            generateUser();
            addVoteAnswer(answersSortedByDate.get(1).getId());
        }

        for(int i = 0; i < 3; i++) {
            generateUser();
            addVoteAnswer(answersSortedByDate.get(2).getId());
        }

        Assert.assertEquals(answersSortedByDate, new ArrayList<>(jdbcAnswerRepository.find(
                AnswerQuery.builder().sortBy(AnswerQuerySortBy.VOTES).build(), 0, 3)));
    }

    @Test
    public void shouldGetNumbersOfAnswersOfQuestion() {
        generateQuestion();

        jdbcAnswerRepository.save(generateFakerAnswer(question.getId()));
        jdbcAnswerRepository.save(generateFakerAnswer(question.getId()));
        jdbcAnswerRepository.save(generateFakerAnswer(question.getId()));
        jdbcAnswerRepository.save(generateFakerAnswer(question.getId()));
        jdbcAnswerRepository.save(generateFakerAnswer(question.getId()));

        assertEquals(5, jdbcAnswerRepository.getNumberOfAnswers(question.getId()));
    }
}

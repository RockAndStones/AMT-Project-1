package stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.*;
import com.github.javafaker.Faker;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.Date;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(Arquillian.class)
public class JdbcCommentRepositoryIT {
    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    @Named("JdbcQuestionRepository")
    private JdbcQuestionRepository jdbcQuestionRepository;

    @Inject @Named("JdbcAnswerRepository")
    private JdbcAnswerRepository jdbcAnswerRepository;

    @Inject @Named("JdbcCommentRepository")
    private JdbcCommentRepository jdbcCommentRepository;

    @Inject @Named("JdbcVoteRepository")
    private JdbcVoteRepository jdbcVoteRepository;

    @Inject
    private JdbcUserRepository jdbcUserRepository;
    private Faker faker;
    private User user;
    private Question question;
    private Answer answer;

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
        generateAnswer();
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

    private void generateAnswer() {
        answer = Answer.builder()
                .answerTo(question.getId())
                .description(faker.lorem().paragraph())
                .creatorId(user.getId())
                .creator(user.getUsername()).build();
        jdbcAnswerRepository.save(answer);
    }

    private Comment generateFakerComment(Id commentTo, Date date) {
        return Comment.builder()
                .commentTo(commentTo)
                .description(faker.lorem().sentence())
                .creatorId(user.getId())
                .creator(user.getUsername())
                .date(date).build();
    }

    private Comment generateFakerComment(Id commentTo) {
        return generateFakerComment(commentTo, new Date());
    }

    @Test
    public void shouldSaveComments() {
        Comment comment1 = generateFakerComment(question.getId());
        Comment comment2 = generateFakerComment(answer.getId());
        jdbcCommentRepository.save(comment1);
        jdbcCommentRepository.save(comment2);

        Optional<Comment> foundComment1 = jdbcCommentRepository.findById(comment1.getId());
        Optional<Comment> foundComment2 = jdbcCommentRepository.findById(comment2.getId());

        assertTrue(foundComment1.isPresent());
        assertTrue(foundComment2.isPresent());
        assertEquals(comment1, foundComment1.get());
        assertEquals(comment2, foundComment2.get());
    }

    @Test
    public void shouldFindAllComments() {
        int initialSize = jdbcCommentRepository.getRepositorySize();
        jdbcCommentRepository.save(generateFakerComment(question.getId()));
        jdbcCommentRepository.save(generateFakerComment(question.getId()));
        jdbcCommentRepository.save(generateFakerComment(question.getId()));
        jdbcCommentRepository.save(generateFakerComment(question.getId()));

        assertEquals(initialSize + 4, jdbcCommentRepository.getRepositorySize());
    }

    @Test
    public void shouldFindCommentsByCommentTo() {

    }

    @Test
    public void shouldFindCommentsByDate() {

    }
}

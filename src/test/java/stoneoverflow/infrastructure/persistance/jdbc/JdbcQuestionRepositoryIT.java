package stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuerySortBy;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcQuestionRepository;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcUserRepository;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcVoteRepository;
import com.github.javafaker.Faker;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;

import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class JdbcQuestionRepositoryIT {
    private final static String WARNAME = "arquillian-managed.war";

    @Inject @Named("JdbcQuestionRepository")
    private JdbcQuestionRepository jdbcQuestionRepository;

    @Inject @Named("JdbcVoteRepository")
    private JdbcVoteRepository jdbcVoteRepository;

    @Inject
    private JdbcUserRepository jdbcUserRepository;
    private Faker faker;
    private User user;

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
        generateUser();
    }

    private void generateUser(){
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

    private Question generateFakerQuestion() {
        return Question.builder()
                .title(faker.lorem().sentence())
                .description(faker.lorem().paragraph())
                .creatorId(user.getId())
                .creator(user.getUsername())
                .build();
    }

    @Test
    public void shouldSaveQuestions() {
        Question q = generateFakerQuestion();
        jdbcQuestionRepository.save(q);

        Optional<Question> foundQuestion = jdbcQuestionRepository.findById(q.getId());
        assertTrue(foundQuestion.isPresent());
        assertEquals(foundQuestion.get().getId(), q.getId());
    }

    @Test
    public void shouldFindQuestions() {
        int initialSize = jdbcQuestionRepository.getRepositorySize();
        jdbcQuestionRepository.save(generateFakerQuestion());
        jdbcQuestionRepository.save(generateFakerQuestion());
        jdbcQuestionRepository.save(generateFakerQuestion());

        assertEquals(initialSize + 3, jdbcQuestionRepository.findAll().size());
    }

    private void addVoteQuestion(QuestionId id){
        jdbcVoteRepository.save(Vote.builder().votedBy(user.getId()).votedObject(id).build());
    }

    @Test
    public void shouldFindQuestionByVotes() {
        // Set the expected result
        ArrayList<Question> questionsSortedByVotesResult = new ArrayList<>();
        questionsSortedByVotesResult.add(Question.builder()
                .creator(user.getUsername())
                .creatorId(user.getId()).build());
        questionsSortedByVotesResult.add(Question.builder()
                .creator(user.getUsername())
                .creatorId(user.getId()).build());
        questionsSortedByVotesResult.add(Question.builder()
                .creator(user.getUsername())
                .creatorId(user.getId()).build());

        // Add the question not in the same order
        jdbcQuestionRepository.save(questionsSortedByVotesResult.get(1));
        jdbcQuestionRepository.save(questionsSortedByVotesResult.get(2));
        jdbcQuestionRepository.save(questionsSortedByVotesResult.get(0));

        for(int i = 0; i < 10; i++){
            generateUser();
            addVoteQuestion(questionsSortedByVotesResult.get(0).getId());
        }

        for(int i = 0; i < 7; i++){
            generateUser();
            addVoteQuestion(questionsSortedByVotesResult.get(1).getId());
        }

        for(int i = 0; i < 5; i++){
            generateUser();
            addVoteQuestion(questionsSortedByVotesResult.get(2).getId());
        }

        ArrayList<Question> questionsSortedByVotes = new ArrayList<>(jdbcQuestionRepository.find(
                QuestionQuery.builder().sortBy(QuestionQuerySortBy.VOTES).build(), 0, 3));

        assertEquals(questionsSortedByVotes, questionsSortedByVotesResult);
    }

    @Test
    public void shouldFindQuestionByViews() {
        ArrayList<Question> questionsSortedByViewsResult = new ArrayList<>();
        questionsSortedByViewsResult.add(Question.builder().creator(user.getUsername()).creatorId(user.getId()).nbViews(new AtomicInteger(150))
                .date(Date.from(new Date().toInstant().truncatedTo(ChronoUnit.MILLIS)))
                .build());
        questionsSortedByViewsResult.add(Question.builder().creator(user.getUsername()).creatorId(user.getId()).nbViews(new AtomicInteger(50))
                .date(Date.from(new Date().toInstant().truncatedTo(ChronoUnit.MILLIS)))
                .build());
        questionsSortedByViewsResult.add(Question.builder().creator(user.getUsername()).creatorId(user.getId()).nbViews(new AtomicInteger(45))
                .date(Date.from(new Date().toInstant().truncatedTo(ChronoUnit.MILLIS)))
                .build());

        jdbcQuestionRepository.save(questionsSortedByViewsResult.get(2));
        jdbcQuestionRepository.save(questionsSortedByViewsResult.get(1));
        jdbcQuestionRepository.save(questionsSortedByViewsResult.get(0));

        ArrayList<Question> questionsSortedByViews = new ArrayList<>(jdbcQuestionRepository.find(QuestionQuery.builder().sortBy(QuestionQuerySortBy.VIEWS).build(), 0, 3));

        assertEquals(questionsSortedByViews, questionsSortedByViewsResult);
        assertEquals(questionsSortedByViews.get(0).getNbViewsAsInt(), questionsSortedByViewsResult.get(0).getNbViewsAsInt());
        assertEquals(questionsSortedByViews.get(1).getNbViewsAsInt(), questionsSortedByViewsResult.get(1).getNbViewsAsInt());
        assertEquals(questionsSortedByViews.get(2).getNbViewsAsInt(), questionsSortedByViewsResult.get(2).getNbViewsAsInt());
    }

    @Test
    public void shouldFindQueryQuestionsByType() {
        jdbcQuestionRepository.save(Question.builder().creator(user.getUsername()).creatorId(user.getId()).questionType(QuestionType.SQL).build());
        jdbcQuestionRepository.save(Question.builder().creator(user.getUsername()).creatorId(user.getId()).build());
        jdbcQuestionRepository.save(Question.builder().creator(user.getUsername()).creatorId(user.getId()).questionType(QuestionType.SQL).build());

        QuestionQuery questionQuery = QuestionQuery.builder().type(QuestionType.SQL).build();

        jdbcQuestionRepository.find(questionQuery,0,jdbcQuestionRepository.getRepositorySize());

        assertEquals(jdbcQuestionRepository.find(questionQuery,0,jdbcQuestionRepository.getRepositorySize()).size(), 2);
    }
}

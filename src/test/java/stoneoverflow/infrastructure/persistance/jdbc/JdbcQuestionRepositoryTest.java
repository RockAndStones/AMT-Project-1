package stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcQuestionRepository;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc.JdbcUserRepository;
import com.github.javafaker.Faker;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JdbcQuestionRepositoryTest {
//    private static JdbcQuestionRepository jdbcQuestionRepository;
//    private static Faker faker;
//    private static User user;
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
//        jdbcQuestionRepository = new JdbcQuestionRepository(ds);
//        faker = new Faker();
//        user = User.builder()
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
//
//        // Question repository needs to create a user for tests
//        JdbcUserRepository jdbcUserRepository = new JdbcUserRepository(ds);
//        jdbcUserRepository.save(user);
//    }
//
//    private Question generateFakerQuestion() {
//        return Question.builder()
//                .title(faker.lorem().sentence())
//                .description(faker.lorem().paragraph())
//                .creatorId(user.getId())
//                .creator(user.getUsername())
//                .nbViews(new AtomicInteger(faker.number().numberBetween(0, 1000)))
//                .date(faker.date().birthday())
//                .build();
//    }
//
//    @Test
//    public void shouldSaveQuestions() {
//        Question q = generateFakerQuestion();
//        jdbcQuestionRepository.save(q);
//
//        Optional<Question> foundQuestion = jdbcQuestionRepository.findById(q.getId());
//        assertTrue(foundQuestion.isPresent());
//        assertEquals(foundQuestion.get(), q);
//    }
//
//    @Test
//    public void shouldFindQuestions() {
//        int initialSize = jdbcQuestionRepository.getRepositorySize();
//        jdbcQuestionRepository.save(generateFakerQuestion());
//        jdbcQuestionRepository.save(generateFakerQuestion());
//        jdbcQuestionRepository.save(generateFakerQuestion());
//
//        assertEquals(initialSize + 3, jdbcQuestionRepository.findAll().size());
//    }

    /*
    @Test
    public void shouldFindQuestionByVotes() {
        // Set the expected result
        ArrayList<Question> questionsSortedByVotesResult = new ArrayList<>();
        questionsSortedByVotesResult.add(generateFakerQuestion().toBuilder()
                .nbVotes(450)
                .build());
        questionsSortedByVotesResult.add(generateFakerQuestion().toBuilder()
                .nbVotes(0)
                .build());
        questionsSortedByVotesResult.add(generateFakerQuestion().toBuilder()
                .nbVotes(-6)
                .build());

        // Add the question not in the same order
        jdbcQuestionRepository.save(questionsSortedByVotesResult.get(1));
        jdbcQuestionRepository.save(questionsSortedByVotesResult.get(2));
        jdbcQuestionRepository.save(questionsSortedByVotesResult.get(0));

        ArrayList<Question> questionsSortedByVotes = new ArrayList<>(jdbcQuestionRepository.find(QuestionQuery.builder().byNbVotes(true).build()));

        assertEquals(questionsSortedByVotes, questionsSortedByVotesResult);
    }

    @Test
    public void shouldFindQuestionByViews() {
        ArrayList<Question> questionsSortedByViewsResult = new ArrayList<>();
        questionsSortedByViewsResult.add(Question.builder().nbViews(150).build());
        questionsSortedByViewsResult.add(Question.builder().nbViews(50).build());
        questionsSortedByViewsResult.add(Question.builder().nbViews(0).build());

        jdbcQuestionRepository.save(questionsSortedByViewsResult.get(2));
        jdbcQuestionRepository.save(questionsSortedByViewsResult.get(1));
        jdbcQuestionRepository.save(questionsSortedByViewsResult.get(0));

        ArrayList<Question> questionsSortedByViews = new ArrayList<>(jdbcQuestionRepository.find(QuestionQuery.builder().byNbViews(true).build()));

        assertEquals(questionsSortedByViews, questionsSortedByViewsResult);
    }

    @Test
    public void shouldFindQueryQuestionsByType() {
        jdbcQuestionRepository.save(Question.builder().questionType(QuestionType.SQL).build());
        jdbcQuestionRepository.save(Question.builder().build());
        jdbcQuestionRepository.save(Question.builder().questionType(QuestionType.SQL).build());

        QuestionQuery questionQuery = QuestionQuery.builder().type(QuestionType.SQL).build();

        assertEquals(jdbcQuestionRepository.find(questionQuery).size(), 2);
    }
    */
}

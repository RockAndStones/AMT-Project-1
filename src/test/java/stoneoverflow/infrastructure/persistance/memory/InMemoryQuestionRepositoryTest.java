package stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuerySortBy;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryQuestionRepositoryTest {
    private InMemoryQuestionRepository inMemoryQuestionRepository;

    @BeforeEach
    public void initInMemoryUserRepository() {
        inMemoryQuestionRepository = new InMemoryQuestionRepository();
    }

    @Test
    public void shouldFindQuestions() {
        inMemoryQuestionRepository.save(Question.builder().build());
        inMemoryQuestionRepository.save(Question.builder().build());
        inMemoryQuestionRepository.save(Question.builder().build());

        assertEquals(inMemoryQuestionRepository.findAll().size(), 3);
    }

    //@Test
    public void shouldFindQuestionByVotes() {
        // Set the expected result
        ArrayList<Question> questionsSortedByVotesResult = new ArrayList<>();
        questionsSortedByVotesResult.add(Question.builder().build());
        questionsSortedByVotesResult.add(Question.builder().build());
        questionsSortedByVotesResult.add(Question.builder().build());

        // Add the question not in the same order
        inMemoryQuestionRepository.save(questionsSortedByVotesResult.get(1));
        inMemoryQuestionRepository.save(questionsSortedByVotesResult.get(2));
        inMemoryQuestionRepository.save(questionsSortedByVotesResult.get(0));

        ArrayList<Question> questionsSortedByVotes = new ArrayList<>(inMemoryQuestionRepository.find(
                QuestionQuery.builder().sortBy(QuestionQuerySortBy.VOTES).build(), 0, inMemoryQuestionRepository.getRepositorySize()));

        assertEquals(questionsSortedByVotes, questionsSortedByVotesResult);
    }

    @Test
    public void shouldFindQuestionByViews() {
        ArrayList<Question> questionsSortedByViewsResult = new ArrayList<>();
        questionsSortedByViewsResult.add(Question.builder().nbViews(new AtomicInteger(150)).build());
        questionsSortedByViewsResult.add(Question.builder().nbViews(new AtomicInteger(50)).build());
        questionsSortedByViewsResult.add(Question.builder().nbViews(new AtomicInteger(0)).build());

        inMemoryQuestionRepository.save(questionsSortedByViewsResult.get(2));
        inMemoryQuestionRepository.save(questionsSortedByViewsResult.get(1));
        inMemoryQuestionRepository.save(questionsSortedByViewsResult.get(0));

        ArrayList<Question> questionsSortedByViews = new ArrayList<>(inMemoryQuestionRepository.find(QuestionQuery.builder().sortBy(QuestionQuerySortBy.VIEWS).build(), 0, inMemoryQuestionRepository.getRepositorySize()));

        assertEquals(questionsSortedByViews, questionsSortedByViewsResult);
    }

    @Test
    public void shouldFindQueryQuestionsByType() {
        inMemoryQuestionRepository.save(Question.builder().questionType(QuestionType.SQL).build());
        inMemoryQuestionRepository.save(Question.builder().build());
        inMemoryQuestionRepository.save(Question.builder().questionType(QuestionType.SQL).build());

        QuestionQuery questionQuery = QuestionQuery.builder().type(QuestionType.SQL).build();

        inMemoryQuestionRepository.find(questionQuery,0,inMemoryQuestionRepository.getRepositorySize());

        assertEquals(inMemoryQuestionRepository.find(questionQuery,0,inMemoryQuestionRepository.getRepositorySize()).size(), 2);
    }
}

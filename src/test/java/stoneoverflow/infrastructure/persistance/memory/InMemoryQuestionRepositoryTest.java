package stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    public void shouldFindQuestionByVotes() {
        // Set the expected result
        ArrayList<Question> questionsSortedByVotesResult = new ArrayList<>();
        questionsSortedByVotesResult.add(Question.builder().nbVotes(450).build());
        questionsSortedByVotesResult.add(Question.builder().nbVotes(0).build());
        questionsSortedByVotesResult.add(Question.builder().nbVotes(-6).build());

        // Add the question not in the same order
        inMemoryQuestionRepository.save(questionsSortedByVotesResult.get(1));
        inMemoryQuestionRepository.save(questionsSortedByVotesResult.get(2));
        inMemoryQuestionRepository.save(questionsSortedByVotesResult.get(0));

        ArrayList<Question> questionsSortedByVotes = new ArrayList<>(inMemoryQuestionRepository.findByVotes(QuestionQuery.builder().build()));

        assertEquals(questionsSortedByVotes, questionsSortedByVotesResult);
    }

    @Test
    public void shouldFindQuestionByViews() {
        ArrayList<Question> questionsSortedByViewsResult = new ArrayList<>();
        questionsSortedByViewsResult.add(Question.builder().nbViews(150).build());
        questionsSortedByViewsResult.add(Question.builder().nbViews(50).build());
        questionsSortedByViewsResult.add(Question.builder().nbViews(0).build());

        inMemoryQuestionRepository.save(questionsSortedByViewsResult.get(2));
        inMemoryQuestionRepository.save(questionsSortedByViewsResult.get(1));
        inMemoryQuestionRepository.save(questionsSortedByViewsResult.get(0));

        ArrayList<Question> questionsSortedByViews = new ArrayList<>(inMemoryQuestionRepository.findByViews(QuestionQuery.builder().build()));

        assertEquals(questionsSortedByViews, questionsSortedByViewsResult);
    }

    @Test
    public void shouldFindQueryQuestionsByType() {
        inMemoryQuestionRepository.save(Question.builder().questionType(QuestionType.SQL).build());
        inMemoryQuestionRepository.save(Question.builder().build());
        inMemoryQuestionRepository.save(Question.builder().questionType(QuestionType.SQL).build());

        QuestionQuery questionQuery = QuestionQuery.builder().byDate(false).type(QuestionType.SQL).build();

        assertEquals(inMemoryQuestionRepository.findByType(questionQuery).size(), 2);
    }
}

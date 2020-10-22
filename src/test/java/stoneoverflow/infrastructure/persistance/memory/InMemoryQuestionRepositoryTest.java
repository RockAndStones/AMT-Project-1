package stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuerySortBy;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class InMemoryQuestionRepositoryTest {
    private InMemoryQuestionRepository inMemoryQuestionRepository;

    @Mock
    Question basicQuestion;

    @Mock
    QuestionQuery questionQueryWithType;

    @Mock
    Question votedQuestion;

    @Mock
    Question viewedQuestion;

    @Mock
    Question questionWithType;

    @Mock
    Question questionWithTypeCopy;

    @BeforeEach
    private void initMockito() {
        lenient().when(questionQueryWithType.getType()).thenReturn(QuestionType.SQL);
        //lenient().when(questionQueryWithType.getType()).thenReturn(Optional.of(Question.builder().build()));
        lenient().when(questionWithType.getId()).thenReturn(new QuestionId());
        lenient().when(questionWithType.getQuestionType()).thenReturn(QuestionType.SQL);
        lenient().when(basicQuestion.getId()).thenReturn(new QuestionId());
        lenient().when(basicQuestion.getQuestionType()).thenReturn(QuestionType.UNCLASSIFIED);

        questionWithTypeCopy = Question.builder()
                .id(new QuestionId(questionWithType.getId().asString()))
                .build();
        lenient().when(questionWithType.deepClone()).thenReturn(questionWithTypeCopy);
    }

    @BeforeEach
    public void initInMemoryUserRepository() {
        inMemoryQuestionRepository = new InMemoryQuestionRepository();
    }

    @Test
    public void shouldFindQuestions() {
        inMemoryQuestionRepository.save(basicQuestion);
        inMemoryQuestionRepository.save(basicQuestion);
        inMemoryQuestionRepository.save(basicQuestion);

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

        ArrayList<Question> questionsSortedByVotes = new ArrayList<>(inMemoryQuestionRepository.find(
                QuestionQuery.builder().sortBy(QuestionQuerySortBy.VOTES).build(), 0, inMemoryQuestionRepository.getRepositorySize()));

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

        ArrayList<Question> questionsSortedByViews = new ArrayList<>(inMemoryQuestionRepository.find(QuestionQuery.builder().sortBy(QuestionQuerySortBy.VIEWS).build(), 0, inMemoryQuestionRepository.getRepositorySize()));

        assertEquals(questionsSortedByViews, questionsSortedByViewsResult);
    }

    @Test
    public void shouldFindQueryQuestionsByType() {
        inMemoryQuestionRepository.save(questionWithType);
        inMemoryQuestionRepository.save(questionWithType);

        lenient().when(questionWithType.deepClone()).thenReturn(questionWithType);
        QuestionQuery questionQuery = QuestionQuery.builder().type(QuestionType.SQL).build();

        assertEquals(inMemoryQuestionRepository.find(questionQuery,0,inMemoryQuestionRepository.getRepositorySize()).size(), 2);
    }
}

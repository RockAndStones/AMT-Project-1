package StoneOverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.StoneOverflow.application.Question.QuestionQuery;
import ch.heigvd.amt.StoneOverflow.domain.Question.Question;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionType;
import ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory.InMemoryQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void shouldFindQueryQuestions() {
        inMemoryQuestionRepository.save(Question.builder().questionType(QuestionType.SQL).build());
        inMemoryQuestionRepository.save(Question.builder().build());
        inMemoryQuestionRepository.save(Question.builder().questionType(QuestionType.SQL).build());

        QuestionQuery questionQuery = QuestionQuery.builder().sqlSearch(true).build();

        assertEquals(inMemoryQuestionRepository.find(questionQuery).size(), 2);
    }
}

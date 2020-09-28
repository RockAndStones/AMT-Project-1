package StoneOverflow.application.question;

import ch.heigvd.amt.StoneOverflow.application.Question.QuestionsDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuestionDTOTest {
    @Test
    public void shouldSendShortDescription() {
        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder().description("a".repeat(100)).build();
        assertEquals(67, questionDTO.shortDescription().length());
    }
}

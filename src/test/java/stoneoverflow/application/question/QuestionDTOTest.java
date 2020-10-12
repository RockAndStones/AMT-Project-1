package stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuestionDTOTest {
    @Test
    public void shouldSendShortDescription() {
        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder().description(new String(new char[100]).replace("\0", "a")).build();
        assertEquals(67, questionDTO.shortDescription().length());
    }
}

package stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.application.question.AddQuestionCommand;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddQuestionCommandTest {
    @Test
    public void shouldSendDefaultValues() {
        AddQuestionCommand addQuestionCommand = AddQuestionCommand.builder().build();

        assertEquals(addQuestionCommand.getTitle(), "My default question");
        assertEquals(addQuestionCommand.getDescription(), "No content");
        assertEquals(addQuestionCommand.getCreator(), "Anonymous");
        assertEquals(addQuestionCommand.getNbViews().get(), 0);
        assertEquals(addQuestionCommand.getType(), QuestionType.UNCLASSIFIED);

    }
}

package stoneoverflow.application.vote;

import ch.heigvd.amt.stoneoverflow.application.answer.AddAnswerCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AddVoteCommandTest {
    @Test
    public void shouldSendDefaultValues() {
        AddAnswerCommand addQuestionCommand = AddAnswerCommand.builder().build();

        assertNull(addQuestionCommand.getAnswerTo());
        assertEquals(addQuestionCommand.getDescription(), "No content");
        assertEquals(addQuestionCommand.getCreator(), "Anonymous");
        assertNull(addQuestionCommand.getDate());
    }
}

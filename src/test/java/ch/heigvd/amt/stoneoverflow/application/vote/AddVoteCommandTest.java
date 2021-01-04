package ch.heigvd.amt.stoneoverflow.application.vote;

import ch.heigvd.amt.stoneoverflow.application.answer.AddAnswerCommand;
import ch.heigvd.amt.stoneoverflow.application.vote.UpdateVoteCommand;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AddVoteCommandTest {
    @Test
    public void shouldSendDefaultValues() {
        UpdateVoteCommand command = UpdateVoteCommand.builder().build();

        assertNull(command.getId());
        assertNull(command.getVotedBy());
        assertEquals(command.getVoteType(), Vote.VoteType.UP);
    }
}

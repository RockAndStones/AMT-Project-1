package ch.heigvd.amt.stoneoverflow.domain.vote;

import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VoteTest {

    @Test
    public void deepCloneCreatesNewObject() {
        Vote vote1 = Vote.builder()
                .votedBy(new UserId())
                .votedObject(new QuestionId())
                .voteType(Vote.VoteType.UP).build();
        Vote vote2 = vote1.deepClone();

        assertEquals(vote1, vote2);
        assertNotSame(vote1, vote2);
    }

    @Test
    public void voteIdShouldBeAutomaticallyGenerated() {
        Vote vote = Vote.builder()
                .votedBy(new UserId())
                .votedObject(new QuestionId())
                .voteType(Vote.VoteType.UP).build();

        assertNotNull(vote.getId());
    }
}

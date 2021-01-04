package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryVoteRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)// To not have tests making others fail because runned before
public class InMemoryVoteRepositoryIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject @Named("InMemoryVoteRepository")
    private InMemoryVoteRepository inMemoryVoteRepository;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt");
        return archive;
    }

    private Vote generateVote(UserId userId, Id id, Vote.VoteType voteType) {
        return Vote.builder()
                .votedBy(userId)
                .votedObject(id)
                .voteType(voteType).build();
    }

    private Vote generateVote(QuestionId questionId, Vote.VoteType voteType) {
        return generateVote(new UserId(), questionId, voteType);
    }

    private Vote generateVote(AnswerId answerId, Vote.VoteType voteType) {
        return generateVote(new UserId(), answerId, voteType);
    }

    private Vote generateVote() {
        return generateVote(new QuestionId(), Vote.VoteType.UP);
    }


    @Test
    public void shouldFindAllVotes() {
        inMemoryVoteRepository.save(generateVote());
        inMemoryVoteRepository.save(generateVote());
        inMemoryVoteRepository.save(generateVote());

        assertEquals(3, inMemoryVoteRepository.getRepositorySize());
    }

    @Test
    public void shouldFindNbVotesOfObject() {
        QuestionId questionId = new QuestionId();
        AnswerId answerId = new AnswerId();

        inMemoryVoteRepository.save(generateVote(questionId, Vote.VoteType.UP));
        inMemoryVoteRepository.save(generateVote(questionId, Vote.VoteType.UP));
        inMemoryVoteRepository.save(generateVote(questionId, Vote.VoteType.UP));
        inMemoryVoteRepository.save(generateVote(questionId, Vote.VoteType.DOWN));

        inMemoryVoteRepository.save(generateVote(answerId, Vote.VoteType.DOWN));
        inMemoryVoteRepository.save(generateVote(answerId, Vote.VoteType.DOWN));
        inMemoryVoteRepository.save(generateVote(answerId, Vote.VoteType.DOWN));
        inMemoryVoteRepository.save(generateVote(answerId, Vote.VoteType.UP));

        assertEquals(2, inMemoryVoteRepository.findNbVotes(questionId, UserMessageType.QUESTION));
        assertEquals(2, inMemoryVoteRepository.findNbVotes(questionId, UserMessageType.ANSWER));
        assertEquals(-2, inMemoryVoteRepository.findNbVotes(answerId, UserMessageType.QUESTION));
        assertEquals(-2, inMemoryVoteRepository.findNbVotes(answerId, UserMessageType.ANSWER));
    }

    @Test
    public void shouldFindVoteWithVoteId() {
        Vote vote = Vote.builder()
                .votedBy(new UserId())
                .votedObject(new QuestionId())
                .voteType(Vote.VoteType.UP).build();
        inMemoryVoteRepository.save(vote);
        Optional<Vote> resVote1 = inMemoryVoteRepository.findVote(vote.getId(), UserMessageType.QUESTION);
        Optional<Vote> resVote2 = inMemoryVoteRepository.findVote(vote.getId(), UserMessageType.ANSWER);

        assertTrue(resVote1.isPresent());
        assertTrue(resVote2.isPresent());
        assertEquals(vote, resVote1.get());
        assertEquals(vote, resVote2.get());
    }

    @Test
    public void shouldFindVoteWithObjectIdAndUserId() {
        UserId userId = new UserId();
        QuestionId questionId = new QuestionId();
        Vote vote = Vote.builder()
                .votedBy(userId)
                .votedObject(questionId)
                .voteType(Vote.VoteType.UP).build();
        inMemoryVoteRepository.save(vote);

        Optional<Vote> resVote1 = inMemoryVoteRepository.findVote(questionId, userId, UserMessageType.QUESTION);
        Optional<Vote> resVote2 = inMemoryVoteRepository.findVote(questionId, userId, UserMessageType.ANSWER);

        assertTrue(resVote1.isPresent());
        assertTrue(resVote2.isPresent());
        assertEquals(vote, resVote1.get());
        assertEquals(vote, resVote2.get());
    }
}

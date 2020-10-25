package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.domain.vote.IVoteRepository;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import ch.heigvd.amt.stoneoverflow.domain.vote.VoteId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
@Named("InMemoryVoteRepository")
public class InMemoryVoteRepository extends InMemoryRepository<Vote, VoteId> implements IVoteRepository {

    @Override
    public int findNbVotes(Id id, UserMessageType userMessageType) {
        Collection<Vote> allVotes = super.findAll();

        int nbVotes=0;
        for (Vote vote : allVotes)
            if (vote.getVotedObject().equals(id))
                nbVotes += vote.getVoteType() == Vote.VoteType.UP ? 1 : -1;

        return nbVotes;
    }

    @Override
    public Optional<Vote> findVote(VoteId id, UserMessageType userMessageType) {
        Collection<Vote> votes = super.findAll();

        for (Vote vote : votes) {
            if (vote.getId().equals(id)) {
                return Optional.of(vote);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Vote> findVote(Id id, UserId userId, UserMessageType userMessageType) {
        Collection<Vote> votes = super.findAll();

        for (Vote vote : votes) {
            if (vote.getVotedObject().equals(id) && vote.getVotedBy().equals(userId)) {
                return Optional.of(vote);
            }
        }

        return Optional.empty();
    }
}

package ch.heigvd.amt.stoneoverflow.domain.vote;

import ch.heigvd.amt.stoneoverflow.domain.IRepository;
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;

import java.util.Optional;

public interface IVoteRepository extends IRepository<Vote, VoteId> {
    int findNbVotes(Id id, UserMessageType userMessageType);
    Optional<Vote> findVote(VoteId id, UserMessageType userMessageType);
    Optional<Vote> findVote(Id id, UserId userId, UserMessageType userMessageType);

}

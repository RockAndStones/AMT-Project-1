package ch.heigvd.amt.stoneoverflow.application.vote;

import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote.VoteType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class AddVoteCommand {

    @Builder.Default
    private UserId votedBy = null;

    @Builder.Default
    private Id votedObject = null;

    @Builder.Default
    private Vote.VoteType voteType = VoteType.UP;

}

package ch.heigvd.amt.stoneoverflow.application.vote;

import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class VoteDTO {
    private String uuid;
    private String votedBy;
    private Vote.VoteType voteType;
}

package ch.heigvd.amt.stoneoverflow.domain.vote;

import ch.heigvd.amt.stoneoverflow.domain.IEntity;
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Vote implements IEntity<Vote, VoteId> {

    public enum VoteType {
        UP,
        DOWN
    }

    private VoteId   id;
    private UserId   votedBy;
    private Id       votedObject;
    private VoteType voteType;

    @Override
    public Vote deepClone() {
        return this.toBuilder()
                .id(new VoteId(id.asString()))
                .build();
    }

    public static class VoteBuilder {

        public Vote build() {

            if (id == null)
                id = new VoteId();

            if (votedBy == null)
                throw new IllegalArgumentException("votedBy cannot be null");

            if (votedObject == null)
                throw new IllegalArgumentException("votedObject cannot be null");

            if (voteType == null)
                voteType = VoteType.UP;

            return new Vote(id, votedBy, votedObject, voteType);
        }
    }
}

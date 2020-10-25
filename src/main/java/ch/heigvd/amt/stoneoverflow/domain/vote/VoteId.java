package ch.heigvd.amt.stoneoverflow.domain.vote;

import ch.heigvd.amt.stoneoverflow.domain.Id;

import java.util.UUID;

public class VoteId extends Id {

    public VoteId() {
        super();
    }

    public VoteId(String asString) {
        super(asString);
    }

    public VoteId(UUID uuid) {
        super(uuid);
    }
}

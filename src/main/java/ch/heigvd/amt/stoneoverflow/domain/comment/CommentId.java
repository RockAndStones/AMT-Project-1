package ch.heigvd.amt.stoneoverflow.domain.comment;

import ch.heigvd.amt.stoneoverflow.domain.Id;

import java.util.UUID;

public class CommentId extends Id {

    public CommentId() {
        super();
    }

    public CommentId(String asString) {
        super(asString);
    }

    public CommentId(UUID uuid) {
        super(uuid);
    }
}

package ch.heigvd.amt.StoneOverflow.domain.comment;

import ch.heigvd.amt.StoneOverflow.domain.Id;

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

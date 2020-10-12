package ch.heigvd.amt.stoneoverflow.domain.answer;

import ch.heigvd.amt.stoneoverflow.domain.Id;

import java.util.UUID;

public class AnswerId extends Id {

    public AnswerId() {
        super();
    }

    public AnswerId(String asString) {
        super(asString);
    }

    public AnswerId(UUID uuid) {
        super(uuid);
    }
}

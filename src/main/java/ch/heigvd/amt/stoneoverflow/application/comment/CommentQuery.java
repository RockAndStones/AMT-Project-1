package ch.heigvd.amt.stoneoverflow.application.comment;

import ch.heigvd.amt.stoneoverflow.domain.Id;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class CommentQuery {
    @Builder.Default
    private Id commentTo = null;

    @Builder.Default
    private boolean byDate = false;
}

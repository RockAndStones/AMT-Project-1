package ch.heigvd.amt.stoneoverflow.application.comment;

import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class CommentQuery {

    @Builder.Default
    private UserMessageType userMessageType = UserMessageType.QUESTION;

    @Builder.Default
    private Id commentTo = null;

    @Builder.Default
    private CommentQuerySortBy sortBy = CommentQuerySortBy.DATE;

    @Builder.Default
    private boolean sortDescending = true;
}

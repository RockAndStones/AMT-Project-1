package ch.heigvd.amt.StoneOverflow.application.comment;

import ch.heigvd.amt.StoneOverflow.domain.Id;
import ch.heigvd.amt.StoneOverflow.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
@EqualsAndHashCode
public class AddCommentCommand {

    @Builder.Default
    private Id CommentTo = null;

    @Builder.Default
    private String content = "No content";

    @Builder.Default
    private UserId creatorId = null;

    @Builder.Default
    private String creator = "Anonymous";

    @Builder.Default
    private Date date = null;
    ;

}

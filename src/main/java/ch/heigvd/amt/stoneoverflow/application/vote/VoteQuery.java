package ch.heigvd.amt.stoneoverflow.application.vote;

import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class VoteQuery {

    @Builder.Default
    private UserMessageType userMessageType = UserMessageType.QUESTION;

    @Builder.Default
    private Id votedObject = null;

}

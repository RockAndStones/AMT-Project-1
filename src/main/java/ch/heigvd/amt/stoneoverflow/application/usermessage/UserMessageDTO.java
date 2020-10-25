package ch.heigvd.amt.stoneoverflow.application.usermessage;

import ch.heigvd.amt.stoneoverflow.application.comment.CommentsDTO;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class UserMessageDTO {
    private String  uuid;
    private String  description;
    private String  creator;
    private int     nbVotes;
    private DateDTO date;
    private String  type;
    private VoteDTO vote;

    private Collection<CommentsDTO.CommentDTO> comments;
}

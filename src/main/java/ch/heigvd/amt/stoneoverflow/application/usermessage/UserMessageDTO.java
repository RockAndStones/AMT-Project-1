package ch.heigvd.amt.stoneoverflow.application.usermessage;

import ch.heigvd.amt.stoneoverflow.application.comment.CommentsDTO;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
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

    private Collection<CommentsDTO.CommentDTO> comments;
}

package ch.heigvd.amt.stoneoverflow.application.answer;

import ch.heigvd.amt.stoneoverflow.application.comment.CommentsDTO;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteDTO;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class AnswersDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class AnswerDTO {
        private String  uuid;
        private String  description;
        private String  creator;
        private DateDTO date;

        @Setter private int nbVotes;
        @Setter private VoteDTO voteDTO;
        @Setter private Collection<CommentsDTO.CommentDTO> comments;
    }

    @Singular
    private List<AnswerDTO> answers;
}

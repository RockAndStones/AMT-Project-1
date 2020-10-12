package ch.heigvd.amt.stoneoverflow.application.answer;

import ch.heigvd.amt.stoneoverflow.application.comment.CommentsDTO;
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
        private String uuid;
        private String description;
        private String creator;
        private int    nbVotes;
        private String date;

        @Setter
        private Collection<CommentsDTO.CommentDTO> comments;
    }

    @Singular
    private List<AnswerDTO> answers;
}

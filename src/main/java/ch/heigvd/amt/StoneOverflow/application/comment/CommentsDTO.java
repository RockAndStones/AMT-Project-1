package ch.heigvd.amt.StoneOverflow.application.comment;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class CommentsDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class CommentDTO {
        private String creator;
        private String content;
        private Date   date;
    }

    @Singular
    private List<CommentDTO> comments;
}

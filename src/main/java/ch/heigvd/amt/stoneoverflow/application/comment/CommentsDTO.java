package ch.heigvd.amt.stoneoverflow.application.comment;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class CommentsDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class CommentDTO {
        private String uuid;
        private String creator;
        private String content;
        private String date;
    }

    @Singular
    private List<CommentDTO> comments;
}

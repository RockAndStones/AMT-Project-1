package ch.heigvd.amt.stoneoverflow.domain.comment;

import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {
    @Test
    public void deepCloneCreatesNewObject() {
        Comment comment = Comment.builder()
                .commentTo(new QuestionId())
                .description("My test comment")
                .creatorId(new UserId())
                .creator("test").build();
        Comment comment2 = comment.deepClone();

        assertEquals(comment, comment2);
        assertNotSame(comment, comment2);
    }

    @Test
    public void commentIdShouldBeAutomaticallyGenerated() {
        Comment comment = Comment.builder()
                .commentTo(new QuestionId())
                .description("My test comment")
                .creatorId(new UserId())
                .creator("test").build();

        assertNotNull(comment.getId());
    }
}

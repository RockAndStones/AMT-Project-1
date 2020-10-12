package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.CommentId;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

@ApplicationScoped
@Named("InMemoryCommentRepository")
public class InMemoryCommentRepository extends InMemoryRepository<Comment, CommentId> implements ICommentRepository {

    @Override
    public Collection<Comment> findByCommentToId(Id commentToId) {
        Collection<Comment> allComments = super.findAll();

        ArrayList<Comment> commentsWithCommentToId = new ArrayList<>();
        for (Comment comment : allComments) {
            if (comment.getCommentTo().equals(commentToId)) {
                commentsWithCommentToId.add(comment);
            }
        }

        return commentsWithCommentToId;
    }
}

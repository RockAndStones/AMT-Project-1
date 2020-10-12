package ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.StoneOverflow.domain.Id;
import ch.heigvd.amt.StoneOverflow.domain.comment.Comment;
import ch.heigvd.amt.StoneOverflow.domain.comment.CommentId;
import ch.heigvd.amt.StoneOverflow.domain.comment.ICommentRepository;

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

        System.out.println("allComments=" + allComments.size());
        System.out.println("commentToId=" + commentToId.asString());

        ArrayList<Comment> commentsWithCommentToId = new ArrayList<>();
        for (Comment comment : allComments) {
            System.out.println("comment.getCommentTo=" + comment.getCommentTo().asString());
            if (comment.getCommentTo().equals(commentToId)) {
                commentsWithCommentToId.add(comment);
            }
        }

        System.out.println("filteredComments: " + commentsWithCommentToId.size());


        return commentsWithCommentToId;
    }
}

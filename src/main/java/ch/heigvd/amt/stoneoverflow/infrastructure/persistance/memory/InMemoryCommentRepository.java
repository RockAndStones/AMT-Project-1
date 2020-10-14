package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuery;
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.CommentId;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@ApplicationScoped
@Named("InMemoryCommentRepository")
public class InMemoryCommentRepository extends InMemoryRepository<Comment, CommentId> implements ICommentRepository {

    @Override
    public Collection<Comment> find(CommentQuery commentQuery) {
        Collection<Comment> allComments = super.findAll();

        // Filter comments by commentQuery::commentTo
        if (commentQuery.getCommentTo() !=  null) {
            ArrayList<Comment> queriedComment = new ArrayList<>();
            for (Comment comment : allComments) {
                if (comment.getCommentTo().equals(commentQuery.getCommentTo())) {
                    queriedComment.add(comment);
                }
            }
            allComments = queriedComment;
        }

        // Sort comments if asked by query
        if (commentQuery.isByDate()) {
            allComments = allComments.stream().sorted(Comparator.comparing(Comment::getDate).reversed())
                    .collect(Collectors.toList());
        }

        return allComments;
    }
}

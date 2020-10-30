package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuery;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuerySortBy;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.CommentId;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        Comparator<Comment> comparator;
        if (commentQuery.getSortBy() == CommentQuerySortBy.DATE)
            if(commentQuery.isSortDescending()) {
                comparator = Comparator.comparing(Comment::getDate);
            } else {
                comparator = Comparator.comparing(Comment::getDate).reversed();
            }
        else
            throw new UnsupportedOperationException("Unsupported question sort");

        Stream<Comment> stream = allComments.stream().sorted(comparator);
        allComments = stream.collect(Collectors.toList());

        return allComments;
    }
}

package ch.heigvd.amt.stoneoverflow.domain.comment;

import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuery;
import ch.heigvd.amt.stoneoverflow.domain.IRepository;

import java.util.*;

public interface ICommentRepository extends IRepository<Comment, CommentId> {
    Collection<Comment> find(CommentQuery commentQuery);
}

package ch.heigvd.amt.StoneOverflow.domain.comment;

import ch.heigvd.amt.StoneOverflow.domain.IRepository;
import ch.heigvd.amt.StoneOverflow.domain.Id;

import java.util.Collection;

public interface ICommentRepository extends IRepository<Comment, CommentId> {
    Collection<Comment> findByCommentToId(Id commentToId);
}

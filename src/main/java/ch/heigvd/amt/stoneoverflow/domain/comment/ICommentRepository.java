package ch.heigvd.amt.stoneoverflow.domain.comment;

import ch.heigvd.amt.stoneoverflow.domain.IRepository;
import ch.heigvd.amt.stoneoverflow.domain.Id;

import java.util.Collection;

public interface ICommentRepository extends IRepository<Comment, CommentId> {
    Collection<Comment> findByCommentToId(Id commentToId);
}

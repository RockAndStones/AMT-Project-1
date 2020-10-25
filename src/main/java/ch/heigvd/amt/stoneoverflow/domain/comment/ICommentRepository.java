package ch.heigvd.amt.stoneoverflow.domain.comment;

import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuery;
import ch.heigvd.amt.stoneoverflow.domain.IRepository;
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;

import java.util.*;
import java.util.stream.Collectors;

public interface ICommentRepository extends IRepository<Comment, CommentId> {
    Collection<Comment> find(CommentQuery commentQuery);
}

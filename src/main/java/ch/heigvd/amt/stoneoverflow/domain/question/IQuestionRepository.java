package ch.heigvd.amt.stoneoverflow.domain.question;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.domain.IRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;

import java.util.Collection;

public interface IQuestionRepository extends IRepository<Question, QuestionId> {
    public Collection<Question> find(QuestionQuery questionQuery, int offset, int limit);
    public Collection<Question> findByUser(UserId userId);
}

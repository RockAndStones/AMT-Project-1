package ch.heigvd.amt.StoneOverflow.domain.answer;

import ch.heigvd.amt.StoneOverflow.domain.IRepository;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionId;

import java.util.Collection;

public interface IAnswerRepository extends IRepository<Answer, AnswerId> {
    Collection<Answer> findByQuestionId(QuestionId questionId);
}

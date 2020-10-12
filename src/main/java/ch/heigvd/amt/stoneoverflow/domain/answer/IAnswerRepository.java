package ch.heigvd.amt.stoneoverflow.domain.answer;

import ch.heigvd.amt.stoneoverflow.domain.IRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;

import java.util.Collection;

public interface IAnswerRepository extends IRepository<Answer, AnswerId> {
    Collection<Answer> findByQuestionId(QuestionId questionId);
}

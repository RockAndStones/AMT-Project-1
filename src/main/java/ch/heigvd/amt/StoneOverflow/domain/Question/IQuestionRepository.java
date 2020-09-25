package ch.heigvd.amt.StoneOverflow.domain.Question;

import ch.heigvd.amt.StoneOverflow.application.Question.QuestionQuery;
import ch.heigvd.amt.StoneOverflow.domain.IRepository;

import java.util.Collection;

public interface IQuestionRepository extends IRepository<Question, QuestionId> {
    public Collection<Question> find(QuestionQuery questionQuery);
}

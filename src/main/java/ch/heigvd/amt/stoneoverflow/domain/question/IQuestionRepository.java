package ch.heigvd.amt.stoneoverflow.domain.question;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.domain.IRepository;

import java.util.Collection;

public interface IQuestionRepository extends IRepository<Question, QuestionId> {
    public Collection<Question> find(QuestionQuery questionQuery);

    public Collection<Question> findByVotes(QuestionQuery questionQuery);

    public Collection<Question> findByViews(QuestionQuery questionQuery);

    public Collection<Question> findByType(QuestionQuery questionQuery);
}

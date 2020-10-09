package ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory;


import ch.heigvd.amt.StoneOverflow.application.Question.QuestionQuery;
import ch.heigvd.amt.StoneOverflow.domain.Question.IQuestionRepository;
import ch.heigvd.amt.StoneOverflow.domain.Question.Question;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionId;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

@ApplicationScoped
@Named("InMemoryQuestionRepository")
public class InMemoryQuestionRepository extends InMemoryRepository<Question, QuestionId> implements IQuestionRepository {
    @Override
    public Collection<Question> find(QuestionQuery questionQuery) {
        //todo: implement question queries
        Collection<Question> questions = super.findAll();
        if(questionQuery.isSqlSearch()) {
            ArrayList<Question> queredQuestion = new ArrayList<>();
            for (Question question : questions) {
                if (question.getQuestionType() == QuestionType.SQL) {
                    queredQuestion.add(question);
                }
            }
            return queredQuestion;
        }
        return questions;
    }
}

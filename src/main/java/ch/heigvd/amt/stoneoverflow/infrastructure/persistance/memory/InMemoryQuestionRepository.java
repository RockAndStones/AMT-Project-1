package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;


import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;

import java.util.ArrayList;
import java.util.Collection;

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

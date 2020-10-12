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
import java.util.Comparator;
import java.util.stream.Collectors;

@ApplicationScoped
@Named("InMemoryQuestionRepository")
public class InMemoryQuestionRepository extends InMemoryRepository<Question, QuestionId> implements IQuestionRepository {
    @Override
    public Collection<Question> find(QuestionQuery questionQuery) {
        //todo: implement question queries
        return super.findAll().stream().sorted(Comparator.comparing(Question::getDate).reversed()).collect(Collectors.toList());
    }

    @Override
    public Collection<Question> findByVotes(QuestionQuery questionQuery) {
        //todo: implement question queries
        return super.findAll().stream().sorted(Comparator.comparing(Question::getNbVotes).reversed()).collect(Collectors.toList());
    }

    @Override
    public Collection<Question> findByViews(QuestionQuery questionQuery) {
        //todo: implement question queries
        return super.findAll().stream().sorted(Comparator.comparing(Question::getNbViews).reversed()).collect(Collectors.toList());
    }

    @Override
    public Collection<Question> findByType(QuestionQuery questionQuery) {
        Collection<Question> questions = super.findAll();
        if(questionQuery.getType() == QuestionType.SQL) {
            ArrayList<Question> queredQuestion = new ArrayList<>();
            for (Question question : questions) {
                if (question.getQuestionType() == QuestionType.SQL) {
                    queredQuestion.add(question);
                }
            }
            return queredQuestion;
        }
        return null;
    }
}

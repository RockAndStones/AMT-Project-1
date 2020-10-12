package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

@ApplicationScoped
@Named("InMemoryAnswerRepository")
public class InMemoryAnswerRepository extends InMemoryRepository<Answer, AnswerId> implements IAnswerRepository {

    @Override
    public Collection<Answer> findByQuestionId(QuestionId questionId) {
        Collection<Answer> allAnswers = super.findAll();

        ArrayList<Answer> answersFromQuestionId = new ArrayList<>();
        for (Answer answer : allAnswers) {
            if (answer.getAnswerTo().equals(questionId)) {
                answersFromQuestionId.add(answer);
            }
        }

        return answersFromQuestionId;
    }
}

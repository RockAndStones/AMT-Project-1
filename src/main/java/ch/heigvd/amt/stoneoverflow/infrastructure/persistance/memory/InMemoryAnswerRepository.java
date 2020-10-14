package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@ApplicationScoped
@Named("InMemoryAnswerRepository")
public class InMemoryAnswerRepository extends InMemoryRepository<Answer, AnswerId> implements IAnswerRepository {

    @Override
    public Collection<Answer> find(AnswerQuery answerQuery) {
        Collection<Answer> allAnswers = super.findAll();

        // Filter answers by answerQuery::answerTo
        if (answerQuery.getAnswerTo() !=  null) {
            ArrayList<Answer> queriedAnswer = new ArrayList<>();
            for (Answer answer : allAnswers) {
                if (answer.getAnswerTo().equals(answerQuery.getAnswerTo())) {
                    queriedAnswer.add(answer);
                }
            }
            allAnswers = queriedAnswer;
        }

        // Sort answers if asked by query
        if (answerQuery.isByDate()) {
            allAnswers = allAnswers.stream().sorted(Comparator.comparing(Answer::getDate).reversed())
                    .collect(Collectors.toList());
        } else if (answerQuery.isByNbVotes()) {
            allAnswers = allAnswers.stream().sorted(Comparator.comparing(Answer::getNbVotes).reversed())
                    .collect(Collectors.toList());
        }

        return allAnswers;
    }
}

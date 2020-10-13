package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;


import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;

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
        Collection<Question> allQuestions = super.findAll();
        if (questionQuery.isByDate()) {
            allQuestions = allQuestions.stream().sorted(Comparator.comparing(Question::getDate).reversed())
                    .collect(Collectors.toList());
        } else if (questionQuery.isByNbVotes()) {
            allQuestions = allQuestions.stream().sorted(Comparator.comparing(Question::getNbVotes).reversed())
                    .collect(Collectors.toList());
        } else if (questionQuery.isByNbViews()) {
            allQuestions = allQuestions.stream().sorted(Comparator.comparing(Question::getNbViews).reversed())
                    .collect(Collectors.toList());
        } else if (questionQuery.getType() != QuestionType.UNCLASSIFIED) {
            ArrayList<Question> queredQuestion = new ArrayList<>();
            for (Question question : allQuestions) {
                if (question.getQuestionType() == questionQuery.getType()) {
                    queredQuestion.add(question);
                }
            }
            allQuestions = queredQuestion;
        }
        return allQuestions;
    }
}

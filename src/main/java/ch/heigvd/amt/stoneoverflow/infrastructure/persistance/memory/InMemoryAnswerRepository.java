package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuerySortBy;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteFacade;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
@Named("InMemoryAnswerRepository")
public class InMemoryAnswerRepository extends InMemoryRepository<Answer, AnswerId> implements IAnswerRepository {

    @Inject
    ServiceRegistry serviceRegistry;
    VoteFacade voteFacade;

    @PostConstruct
    private void initDefaultValues() {
        voteFacade = serviceRegistry.getVoteFacade();

    }

    private int nbVotesComparator(AnswerId id) {
        return voteFacade.getNumberOfVotes(id);
    }

    @Override
    public Collection<Answer> find(AnswerQuery answerQuery, int offset, int limit) {
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

        Comparator<Answer> comparator;
        if (answerQuery.getSortBy() == AnswerQuerySortBy.DATE)
            comparator = Comparator.comparing(Answer::getDate).reversed();
        else if (answerQuery.getSortBy() == AnswerQuerySortBy.VOTES)
            comparator = Comparator.comparing((Answer a) -> nbVotesComparator(a.getId())).reversed();
        else
            throw new UnsupportedOperationException("Unsupported answer sort");

        Stream<Answer> stream = allAnswers.stream().sorted(comparator);

        List<Answer> filteredAnswers = stream.collect(Collectors.toList());
        // To not be out of bound
        int lastIndex = filteredAnswers.size();
        if(lastIndex > offset + limit){
            lastIndex = offset + limit;
        }

        return filteredAnswers.subList(offset, lastIndex);
    }

    @Override
    public int getNumberOfAnswers(QuestionId questionId) {
        return find(AnswerQuery.builder().answerTo(questionId).build(), 0, super.getRepositorySize()).size();
    }
}

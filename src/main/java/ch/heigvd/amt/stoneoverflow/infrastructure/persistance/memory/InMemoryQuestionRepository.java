package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;


import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuerySortBy;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteFacade;
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
@Named("InMemoryQuestionRepository")
public class InMemoryQuestionRepository extends InMemoryRepository<Question, QuestionId> implements IQuestionRepository {

    @Inject @Named("InMemoryVoteRepository")
    InMemoryVoteRepository voteRepository;


    private int nbVotesComparator(QuestionId id) {
        return voteRepository.findNbVotes(id, UserMessageType.QUESTION);
    }

    @Override
    public Collection<Question> find(QuestionQuery questionQuery, int offset, int limit) {
        Collection<Question> allQuestions = super.findAll();

        Comparator<Question> comparator;
        if (questionQuery.getSortBy() == QuestionQuerySortBy.DATE)
            comparator = Comparator.comparing(Question::getDate).reversed();
        else if (questionQuery.getSortBy() == QuestionQuerySortBy.VOTES)
            comparator = Comparator.comparing((Question q) -> nbVotesComparator(q.getId())).reversed();
        else if (questionQuery.getSortBy() == QuestionQuerySortBy.VIEWS)
            comparator = Comparator.comparing(Question::getNbViewsAsInt).reversed();
        else
            throw new UnsupportedOperationException("Unsupported question sort");

        Stream<Question> stream = allQuestions.stream().sorted(comparator);
        if (questionQuery.getType() != QuestionType.UNCLASSIFIED)
            stream = stream.filter(q -> q.getQuestionType() == questionQuery.getType());

        if (!questionQuery.getSearchCondition().isEmpty())
            stream = stream.filter(q -> q.getTitle().contains(questionQuery.getSearchCondition()));


        List<Question> filteredQuestions = stream.collect(Collectors.toList());
        // To not be out of bound
        int lastIndex = Math.min(filteredQuestions.size(), offset + limit);

        return filteredQuestions.subList(offset, lastIndex);
    }

    @Override
    public Collection<Question> findByUser(UserId userId) {
        return super.findAll().stream()
                .filter(q -> q.getCreatorId().equals(userId))
                .collect(Collectors.toList());
    }
}

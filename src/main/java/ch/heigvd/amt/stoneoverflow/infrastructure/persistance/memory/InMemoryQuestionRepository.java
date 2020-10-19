package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;


import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuerySortBy;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
@Named("InMemoryQuestionRepository")
public class InMemoryQuestionRepository extends InMemoryRepository<Question, QuestionId> implements IQuestionRepository {
    @Override
    public Collection<Question> find(QuestionQuery questionQuery) {
        Collection<Question> allQuestions = super.findAll();

        Comparator<Question> comparator;
        if (questionQuery.getSortBy() == QuestionQuerySortBy.DATE)
            comparator = Comparator.comparing(Question::getDate).reversed();
        else if (questionQuery.getSortBy() == QuestionQuerySortBy.VOTES)
            comparator = Comparator.comparing(Question::getNbVotes).reversed();
        else if (questionQuery.getSortBy() == QuestionQuerySortBy.VIEWS)
            comparator = Comparator.comparing(Question::getNbViews).reversed();
        else
            throw new UnsupportedOperationException("Unsupported question sort");

        Stream<Question> stream = allQuestions.stream().sorted(comparator);
        if (questionQuery.getType() != QuestionType.UNCLASSIFIED)
            stream = stream.filter(q -> q.getQuestionType() == questionQuery.getType());

        allQuestions = stream.collect(Collectors.toList());
        return allQuestions;
    }
}

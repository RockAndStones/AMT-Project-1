package ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory;


import ch.heigvd.amt.StoneOverflow.application.Question.QuestionQuery;
import ch.heigvd.amt.StoneOverflow.domain.Question.IQuestionRepository;
import ch.heigvd.amt.StoneOverflow.domain.Question.Question;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionId;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryQuestionRepository implements IQuestionRepository {
   private Map<QuestionId, Question> store = new ConcurrentHashMap<>();

   @Override
   public void save(Question question){
       store.put(question.getId(), question);
   }

    @Override
    public void remove(QuestionId questionId) {
        store.remove(questionId);
    }

    @Override
    public Optional<Question> findById(QuestionId questionId) {
        Question existingQuestion = store.get(questionId);
        if (existingQuestion == null){
            return Optional.empty();
        }
        Question question = existingQuestion.toBuilder().build();
        return Optional.of(question);
    }

    @Override
    public Collection<Question> findAll() {
        return store.values().stream()
                .map(question -> question.toBuilder().build())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Question> find(QuestionQuery questionQuery) {
        if(questionQuery != null && questionQuery.isSqlSearch()){
            return findAll().stream()
                    .filter(question -> question.getQuestionType() == QuestionType.SQL)
                    .collect(Collectors.toList());
        }
        return findAll();
    }
}

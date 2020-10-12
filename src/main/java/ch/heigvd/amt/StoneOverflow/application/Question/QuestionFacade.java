package ch.heigvd.amt.StoneOverflow.application.Question;

import ch.heigvd.amt.StoneOverflow.domain.Question.IQuestionRepository;
import ch.heigvd.amt.StoneOverflow.domain.Question.Question;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionFacade {
    private IQuestionRepository questionRepository;

    public QuestionFacade(IQuestionRepository questionRepository){
        this.questionRepository = questionRepository;
    }

    public void addQuestion(AddQuestionCommand command){
        Question addedQuestion = Question.builder().
                title(command.getTitle()).
                description(command.getDescription()).
                creatorId(command.getCreatorId()).
                creator(command.getCreator()).
                nbVotes(command.getNbVotes()).
                questionType(command.getType()).build();
        questionRepository.save(addedQuestion);
    }

    public QuestionsDTO getQuestions(QuestionQuery query){
        Collection<Question> allQuestions = questionRepository.find(query);

        List<QuestionsDTO.QuestionDTO> allQuestionsDTO = allQuestions.stream()
                .sorted(Comparator.comparing(Question::getDate).reversed())
                .map(question -> QuestionsDTO.QuestionDTO.builder()
                    .title(question.getTitle())
                    .creator(question.getCreator())
                    .description(question.getDescription())
                    .nbVotes(question.getNbVotes())
                    .date(question.getDate())
                    .type(question.getQuestionType()).build())
                .collect(Collectors.toList());

        return QuestionsDTO.builder()
                .questions(allQuestionsDTO)
                .build();
    }
}

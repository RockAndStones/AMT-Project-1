package ch.heigvd.amt.StoneOverflow.application.Question;

import ch.heigvd.amt.StoneOverflow.domain.Question.IQuestionRepository;
import ch.heigvd.amt.StoneOverflow.domain.Question.Question;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionId;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuestionFacade {
    private IQuestionRepository questionRepository;

    public QuestionFacade(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void addQuestion(AddQuestionCommand command){
        Question addedQuestion = Question.builder().
                title(command.getTitle()).
                description(command.getDescription()).
                creatorId(command.getCreatorId()).
                creator(command.getCreator()).
                nbVotes(command.getNbVotes()).
                nbViews(command.getNbViews()).
                questionType(command.getType()).build();
        questionRepository.save(addedQuestion);
    }

    public QuestionsDTO getQuestions(QuestionQuery query) {
        Collection<Question> allQuestions = questionRepository.find(query);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        List<QuestionsDTO.QuestionDTO> allQuestionsDTO = allQuestions.stream()
                .sorted(Comparator.comparing(Question::getDate).reversed())
                .map(question -> QuestionsDTO.QuestionDTO.builder()
                    .title(question.getTitle())
                    .creator(question.getCreator())
                    .description(question.getDescription())
                    .nbVotes(question.getNbVotes())
                    .nbViews(question.getNbViews())
                    .date(question.getDate())
                    .type(question.getQuestionType()).build())
                .collect(Collectors.toList());

        return QuestionsDTO.builder().questions(allQuestionsDTO).build();
    }

    public QuestionsDTO.QuestionDTO getQuestion(QuestionId id) {
        Optional<Question> question = questionRepository.findById(id);
        question.ifPresent(Question::addView);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // TODO : Is it right ?
        return question.map(value -> QuestionsDTO.QuestionDTO.builder()
                .uuid(value.getId().asString())
                .title(value.getTitle())
                .description(value.getDescription())
                .creator(value.getCreator())
                .nbVotes(value.getNbVotes())
                .nbViews(value.getNbViews())
                .date(value.getDate())
                .type(value.getQuestionType()).build()).orElse(null);
    }
}

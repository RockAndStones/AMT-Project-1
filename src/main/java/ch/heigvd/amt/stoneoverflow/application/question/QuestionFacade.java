package ch.heigvd.amt.stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class QuestionFacade {
    private IQuestionRepository questionRepository;
    private GamificationFacade gamificationFacade;

    public QuestionId addQuestion(AddQuestionCommand command){
        Question addedQuestion = Question.builder()
                .title(command.getTitle())
                .description(command.getDescription())
                .creatorId(command.getCreatorId())
                .creator(command.getCreator())
                .nbViews(command.getNbViews())
                .date(command.getDate())
                .questionType(command.getType()).build();
        questionRepository.save(addedQuestion);
        // Send to the gamification
        gamificationFacade.addQuestionAsync(command.getCreatorId().asString(), null);
        gamificationFacade.stonerProgressAsync(command.getCreatorId().asString(), null);
        return addedQuestion.getId();
    }

    public QuestionsDTO getQuestions(QuestionQuery query, int offset, int limit) {
        Collection<Question> allQuestions = questionRepository.find(query, offset, limit);
        List<QuestionsDTO.QuestionDTO> allQuestionsDTO = allQuestions.stream()
        .map(question -> QuestionsDTO.QuestionDTO.builder()
                .uuid(question.getId().asString())
                .title(question.getTitle())
                .type(question.getQuestionType().name())
                .creator(question.getCreator())
                .description(question.getDescription())
                .nbViews(question.getNbViews())
                .date(new DateDTO(question.getDate()))
                .type(question.getQuestionType().name()).build())
        .collect(Collectors.toList());

        return QuestionsDTO.builder().questions(allQuestionsDTO).build();
    }

    public QuestionsDTO.QuestionDTO getQuestion(QuestionId id) {
        Optional<Question> question = questionRepository.findById(id);

        return question.map(value -> QuestionsDTO.QuestionDTO.builder()
                .uuid(value.getId().asString())
                .title(value.getTitle())
                .type(value.getQuestionType().name())
                .description(value.getDescription())
                .creator(value.getCreator())
                .nbViews(value.getNbViews())
                .date(new DateDTO(value.getDate()))
                .type(value.getQuestionType().name()).build())
            .orElse(null);
    }

    public synchronized void addViewToQuestion(QuestionId id){
        Optional<Question> question = questionRepository.findById(id);
        question.ifPresent(Question::addView);
        question.ifPresent(value -> questionRepository.update(value));
    }

    public int getNumberOfQuestions() {
        return questionRepository.getRepositorySize();
    }
}

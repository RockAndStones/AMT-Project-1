package ch.heigvd.amt.stoneoverflow.application.answer;

import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerFacade {
    private IAnswerRepository answerRepository;

    public AnswerFacade(IAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void addAnswer(AddAnswerCommand command) {
        Answer addAnswer = Answer.builder().
                answerTo(command.getAnswerTo()).
                description(command.getDescription()).
                creator(command.getCreator()).
                nbVotes(command.getNbVotes()).
                date(command.getDate()).build();
        answerRepository.save(addAnswer);
    }

    public AnswersDTO getAnswersFromQuestion(QuestionId questionId) {
        Collection<Answer> answersFromQuestionId = answerRepository.findByQuestionId(questionId);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        List<AnswersDTO.AnswerDTO> answersFromQuestionIdDTO = answersFromQuestionId.stream().map(
                answer -> AnswersDTO.AnswerDTO.builder()
                        .uuid(answer.getId().asString())
                        .description(answer.getDescription())
                        .creator(answer.getCreator())
                        .nbVotes(answer.getNbVotes())
                        .date(formatter.format(answer.getDate())).build()).collect(Collectors.toList());

        return AnswersDTO.builder().answers(answersFromQuestionIdDTO).build();
    }
}

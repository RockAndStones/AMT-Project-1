package ch.heigvd.amt.StoneOverflow.application.answer;

import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionId;
import ch.heigvd.amt.StoneOverflow.domain.answer.Answer;
import ch.heigvd.amt.StoneOverflow.domain.answer.IAnswerRepository;

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

    public AnswersDTO getQuestionsByQuestionId(QuestionId questionId) {
        Collection<Answer> answersFromQuestionId = answerRepository.findByQuestionId(questionId);

        List<AnswersDTO.AnswerDTO> answersFromQuestionIdDTO = answersFromQuestionId.stream().map(
                answer -> AnswersDTO.AnswerDTO.builder()
                        .description(answer.getDescription())
                        .creator(answer.getCreator())
                        .nbVotes(answer.getNbVotes())
                        .date(answer.getDate()).build()).collect(Collectors.toList());

        return AnswersDTO.builder().answers(answersFromQuestionIdDTO).build();
    }
}

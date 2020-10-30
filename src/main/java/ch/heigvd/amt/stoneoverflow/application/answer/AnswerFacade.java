package ch.heigvd.amt.stoneoverflow.application.answer;

import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnswerFacade {
    private IAnswerRepository answerRepository;

    public AnswerFacade(IAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public AnswerId addAnswer(AddAnswerCommand command) {
        Answer addAnswer = Answer.builder()
                .answerTo(command.getAnswerTo())
                .description(command.getDescription())
                .creatorId(command.getCreatorId())
                .creator(command.getCreator())
                .date(command.getDate()).build();
        answerRepository.save(addAnswer);
        return addAnswer.getId();
    }

    public AnswersDTO getAnswers(AnswerQuery answerQuery, int offset, int limit) {
        Collection<Answer> answers = answerRepository.find(answerQuery, offset, limit);

        List<AnswersDTO.AnswerDTO> answersFromQuestionIdDTO = answers.stream().map(
                answer -> AnswersDTO.AnswerDTO.builder()
                        .uuid(answer.getId().asString())
                        .description(answer.getDescription())
                        .creator(answer.getCreator())
                        .date(new DateDTO(answer.getDate())).build())
                .collect(Collectors.toList());

        return AnswersDTO.builder().answers(answersFromQuestionIdDTO).build();
    }

    public AnswersDTO.AnswerDTO getAnswer(AnswerId answerId) {
        Optional<Answer> answer = answerRepository.findById(answerId);

        return answer.map(value -> AnswersDTO.AnswerDTO.builder()
                .uuid(value.getId().asString())
                .description(value.getDescription())
                .creator(value.getCreator())
                .date(new DateDTO(value.getDate())).build())
                .orElse(null);
    }

    public int getNumberOfAnswers() {
        return answerRepository.getRepositorySize();
    }

    public int getNumberOfAnswers(QuestionId questionId) {
        return answerRepository.getNumberOfAnswers(questionId);
    }
}

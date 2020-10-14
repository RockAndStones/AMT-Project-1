package ch.heigvd.amt.stoneoverflow.application.answer;

import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerFacade {
    private IAnswerRepository answerRepository;

    public AnswerFacade(IAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void addAnswer(AddAnswerCommand command) {
        Answer addAnswer = Answer.builder()
                .answerTo(command.getAnswerTo())
                .description(command.getDescription())
                .creatorId(command.getCreatorId())
                .creator(command.getCreator())
                .nbVotes(command.getNbVotes())
                .date(command.getDate()).build();
        answerRepository.save(addAnswer);
    }

    public AnswersDTO getAnswersFromQuestion(AnswerQuery answerQuery) {
        Collection<Answer> answers = answerRepository.find(answerQuery);

        List<AnswersDTO.AnswerDTO> answersFromQuestionIdDTO = answers.stream().map(
                answer -> AnswersDTO.AnswerDTO.builder()
                        .uuid(answer.getId().asString())
                        .description(answer.getDescription())
                        .creator(answer.getCreator())
                        .nbVotes(answer.getNbVotes())
                        .date(new DateDTO(answer.getDate())).build())
                .collect(Collectors.toList());

        return AnswersDTO.builder().answers(answersFromQuestionIdDTO).build();
    }
}

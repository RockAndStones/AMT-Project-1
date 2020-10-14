package stoneoverflow.application.answer;

import ch.heigvd.amt.stoneoverflow.application.answer.AddAnswerCommand;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswersDTO;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryAnswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class AnswerFacadeIT {
    private IAnswerRepository answerRepository;
    private Date              answerDate;
    private UserId            creatorId;

    @BeforeEach
    public void initializeIdentityManagementFacade() {
        this.answerRepository = new InMemoryAnswerRepository();

        // Use a fix date for all the tests
        this.answerDate       = new Date(System.currentTimeMillis());

        // Define a UserId to simulate a user responding
        this.creatorId        = new UserId();
    }

    @Test
    public void shouldAddAnswer() {
        AnswerFacade answerFacade = new AnswerFacade(answerRepository);

        // Define a QuestionId to simulate a response to a question
        QuestionId answerTo = new QuestionId();

        // Add answer to repository
        AddAnswerCommand answerCommand = AddAnswerCommand.builder()
                .answerTo(answerTo)
                .creatorId(creatorId)
                .date(answerDate).build();
        answerFacade.addAnswer(answerCommand);

        // Prepare answer query
        AnswerQuery answerQuery = AnswerQuery.builder()
                .answerTo(answerTo).build();

        // Create the expected result
        // Recover the uuid from the answer in the repository
        AnswersDTO.AnswerDTO answerDTO = AnswersDTO.AnswerDTO.builder()
                .uuid(answerFacade.getAnswersFromQuestion(answerQuery).getAnswers().get(0).getUuid())
                .description("No content")
                .creator("Anonymous")
                .nbVotes(0)
                .date(new DateDTO(answerDate)).build();

        assertEquals(answerFacade.getAnswersFromQuestion(answerQuery).getAnswers().get(0), answerDTO);
    }

    @Test
    public void shouldGetOnlyAnswersFromAQuestionId() {
        AnswerFacade answerFacade = new AnswerFacade(answerRepository);

        // Define multiple QuestionId to simulate responses to multiple questions
        QuestionId answerTo1 = new QuestionId();
        QuestionId answerTo2 = new QuestionId();
        QuestionId answerTo3 = new QuestionId();

        // Add answers to repository
        AddAnswerCommand answerCommand1 = AddAnswerCommand.builder()
                .answerTo(answerTo1)
                .creatorId(creatorId)
                .date(answerDate).build();
        AddAnswerCommand answerCommand2 = AddAnswerCommand.builder()
                .answerTo(answerTo2)
                .creatorId(creatorId)
                .date(answerDate).build();
        AddAnswerCommand answerCommand3 = AddAnswerCommand.builder()
                .answerTo(answerTo3)
                .creatorId(creatorId).build();
        answerFacade.addAnswer(answerCommand1);
        answerFacade.addAnswer(answerCommand2);
        answerFacade.addAnswer(answerCommand3);

        // Prepare answer query
        AnswerQuery answerQuery = AnswerQuery.builder()
                .answerTo(answerTo1).build();

        // Create the expected result
        // Recover the uuid from the answer in the repository
        AnswersDTO.AnswerDTO answerDTO = AnswersDTO.AnswerDTO.builder()
                .uuid(answerFacade.getAnswersFromQuestion(answerQuery).getAnswers().get(0).getUuid())
                .description("No content")
                .creator("Anonymous")
                .nbVotes(0)
                .date(new DateDTO(answerDate)).build();

        assertEquals(answerFacade.getAnswersFromQuestion(answerQuery).getAnswers().get(0), answerDTO);
    }
}

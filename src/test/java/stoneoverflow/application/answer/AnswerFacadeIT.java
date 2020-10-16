package stoneoverflow.application.answer;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.answer.AddAnswerCommand;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswersDTO;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class AnswerFacadeIT {
    private Date              answerDate;
    private SimpleDateFormat  formatter;
    private UserId            creatorId;

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private AnswerFacade answerFacade;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt.BCrypt");
        return archive;
    }

    @Before
    public void initializeIdentityManagementFacade() {
        this.answerFacade = serviceRegistry.getAnswerFacade();

        // Use a fix date for all the tests
        this.answerDate       = new Date(System.currentTimeMillis());
        this.formatter        = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // Define a UserId to simulate a user responding
        this.creatorId        = new UserId();
    }

    @Test
    public void shouldAddAnswer() {
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

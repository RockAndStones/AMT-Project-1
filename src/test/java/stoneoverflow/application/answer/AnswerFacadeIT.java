package stoneoverflow.application.answer;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.answer.AddAnswerCommand;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswersDTO;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.stoneoverflow.application.question.*;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class AnswerFacadeIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private AuthenticatedUserDTO testUser;
    private QuestionFacade       questionFacade;
    private AnswerFacade         answerFacade;
    private DateDTO              date;


    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt.BCrypt");
        return archive;
    }

    @Before
    public void init() throws LoginFailedException  {

        this.questionFacade = serviceRegistry.getQuestionFacade();
        this.answerFacade   = serviceRegistry.getAnswerFacade();

        // Use a fix date for all the tests
        this.date = new DateDTO(new Date(System.currentTimeMillis()));

        this.testUser = serviceRegistry.getIdentityManagementFacade().login(LoginCommand.builder()
                .username("test")
                .plaintextPassword("test")
                .build());
    }

    @Test
    public void shouldAddAnswer() {

        int questionIndex = questionFacade.getNumberOfQuestions();
        int answerIndex   = answerFacade.getNumberOfAnswers();

        // Insert a question in the repository to respond to
        AddQuestionCommand questionCommand = AddQuestionCommand.builder()
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .date(date)
                .build();
        questionFacade.addQuestion(questionCommand);

        // Get the QuestionId of the added question
        QuestionId questionId = new QuestionId(questionFacade.getQuestions(
                QuestionQuery.builder().build(),
                0,
                questionFacade.getNumberOfQuestions()).getQuestions().get(questionIndex).getUuid());

        // Add answer to repository
        AddAnswerCommand answerCommand = AddAnswerCommand.builder()
                .answerTo(questionId)
                .creatorId(testUser.getId())
                .date(date).build();
        answerFacade.addAnswer(answerCommand);

        // Prepare answer query
        AnswerQuery answerQuery = AnswerQuery.builder().build();

        // Create the expected result
        // Recover the uuid from the answer in the repository
        AnswersDTO.AnswerDTO answerDTO = AnswersDTO.AnswerDTO.builder()
                .uuid(answerFacade.getAnswers(answerQuery, 0, answerFacade.getNumberOfAnswers())
                        .getAnswers().get(answerIndex).getUuid())
                .description("No content")
                .creator(testUser.getUsername())
                .nbVotes(0)
                .date(date).build();

        assertEquals(answerFacade.getAnswers(answerQuery, 0, answerFacade.getNumberOfAnswers()).getAnswers().get(answerIndex), answerDTO);
    }

    @Test
    public void shouldGetOnlyAnswersFromAQuestionId() {

        int questionIndex = questionFacade.getNumberOfQuestions();

        // Insert 3 questions in the repository to respond to
        AddQuestionCommand questionCommand = AddQuestionCommand.builder()
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .date(date)
                .build();
        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);

        // Define multiple QuestionId to simulate responses to multiple questions
        List<QuestionsDTO.QuestionDTO> questionDTOS = questionFacade.getQuestions(
                QuestionQuery.builder().build(), 0, questionFacade.getNumberOfQuestions()).getQuestions();

        QuestionId  questionId1 = new QuestionId(questionDTOS.get(questionIndex).getUuid());
        QuestionId  questionId2 = new QuestionId(questionDTOS.get(questionIndex + 1).getUuid());
        QuestionId  questionId3 = new QuestionId(questionDTOS.get(questionIndex + 2).getUuid());

        // Add answers to repository
        AddAnswerCommand answerCommand1 = AddAnswerCommand.builder()
                .answerTo(questionId1)
                .creatorId(testUser.getId())
                .date(date).build();
        AddAnswerCommand answerCommand2 = AddAnswerCommand.builder()
                .answerTo(questionId2)
                .creatorId(testUser.getId())
                .date(date).build();
        AddAnswerCommand answerCommand3 = AddAnswerCommand.builder()
                .answerTo(questionId3)
                .creatorId(testUser.getId()).build();
        answerFacade.addAnswer(answerCommand1);
        answerFacade.addAnswer(answerCommand2);
        answerFacade.addAnswer(answerCommand3);

        // Prepare answer query
        AnswerQuery answerQuery = AnswerQuery.builder()
                .answerTo(questionId1).build();

        // Create the expected result
        // Recover the uuid from the answer in the repository
        AnswersDTO.AnswerDTO answerDTO = AnswersDTO.AnswerDTO.builder()
                .uuid(answerFacade.getAnswers(answerQuery, 0, answerFacade.getNumberOfAnswers()).getAnswers().get(0).getUuid())
                .description("No content")
                .creator(testUser.getUsername())
                .nbVotes(0)
                .date(date).build();

        assertEquals(answerFacade.getAnswers(answerQuery, 0, answerFacade.getNumberOfAnswers()).getAnswers().get(0), answerDTO);
    }

    @Test
    public void shouldGetAllQuestions() {

        int questionIndex = questionFacade.getNumberOfQuestions();

        // Add question to question repo
        AddQuestionCommand questionCommand = AddQuestionCommand.builder()
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .build();
        questionFacade.addQuestion(questionCommand);

        // Retrieve questionId from added question
        QuestionId  questionId = new QuestionId(questionFacade.getQuestions(
                QuestionQuery.builder().build(),
                0,
                questionFacade.getNumberOfQuestions()).getQuestions().get(questionIndex).getUuid());


        int sizeBefore = answerFacade.getNumberOfAnswers();

        // Add 4 new answers to answer repo
        AddAnswerCommand answerCommand = AddAnswerCommand.builder()
                .answerTo(questionId)
                .creatorId(testUser.getId())
                .date(date).build();
        answerFacade.addAnswer(answerCommand);
        answerFacade.addAnswer(answerCommand);
        answerFacade.addAnswer(answerCommand);
        answerFacade.addAnswer(answerCommand);

        assertEquals(answerFacade.getNumberOfAnswers(),
                sizeBefore + 4);
    }
}

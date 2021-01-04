package ch.heigvd.amt.stoneoverflow.application.answer;

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
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.util.Date;

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
                .addAsDirectory("target/generated-sources/openapi")
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "ch.heigvd.amt.gamification")
                .addClass(ch.heigvd.amt.gamification.Configuration.class)
                .addPackages(true, "okhttp3")
                .addPackages(true, "okio")
                .addPackages(true, "com.squareup.okhttp3")
                .addPackages(true, "io.gsonfire")
                .addPackages(true, "com.google.gson")
                .addPackages(true, "org.springframework.security.crypto.bcrypt")
                .addAsResource(new File("src/main/resources/environment.properties"), "environment.properties");
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

    private QuestionId addQuestion() {
        return questionFacade.addQuestion(AddQuestionCommand.builder()
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .date(date).build());
    }

    private AnswerId addAnswer(QuestionId answerTo) {
        return answerFacade.addAnswer(AddAnswerCommand.builder()
                .answerTo(answerTo)
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .date(date).build());
    }

    @Test
    public void shouldAddAnswer() {

        QuestionId questionId = addQuestion();
        AnswerId answerId = addAnswer(questionId);

        AnswersDTO.AnswerDTO answerDTO = AnswersDTO.AnswerDTO.builder()
                .uuid(answerId.asString())
                .description("No content")
                .creator(testUser.getUsername())
                .date(date).build();

        assertEquals(answerFacade.getAnswer(answerId), answerDTO);
    }

    @Test
    public void shouldGetOnlyAnswersFromAQuestionId() {

        QuestionId  questionId1 = addQuestion();
        QuestionId  questionId2 = addQuestion();
        QuestionId  questionId3 = addQuestion();

        AnswerId answerId1 = addAnswer(questionId1);
        AnswerId answerId2 = addAnswer(questionId1);
        addAnswer(questionId2);
        addAnswer(questionId3);

        AnswersDTO answersDTO = answerFacade.getAnswers(AnswerQuery.builder().answerTo(questionId1).build(),
                0,
                answerFacade.getNumberOfAnswers());

        assertEquals(answersDTO.getAnswers().size(), 2);

        String resultAnswerId1 = answersDTO.getAnswers().get(0).getUuid();
        String resultAnswerId2 = answersDTO.getAnswers().get(1).getUuid();

        assertTrue(
                (resultAnswerId1.equals(answerId1.asString()) || resultAnswerId1.equals(answerId2.asString()))
                        &&
                        (resultAnswerId2.equals(answerId1.asString()) || resultAnswerId2.equals(answerId2.asString()))
        );
    }
}

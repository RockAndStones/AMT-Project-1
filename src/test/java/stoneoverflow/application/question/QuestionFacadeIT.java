package stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.stoneoverflow.application.question.*;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;


import javax.inject.Inject;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class QuestionFacadeIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private AuthenticatedUserDTO testUser;
    private QuestionFacade questionFacade;
    private DateDTO date;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackage("org.springframework.security.crypto.bcrypt");
        return archive;
    }

    @Before
    public void init() throws LoginFailedException {
        testUser = serviceRegistry.getIdentityManagementFacade().login(LoginCommand.builder()
                .username("test")
                .plaintextPassword("test")
                .build());

        questionFacade = serviceRegistry.getQuestionFacade();
        this.date = new DateDTO(new Date(System.currentTimeMillis()));
    }

    private QuestionId addQuestion(QuestionType questionType) {
        return questionFacade.addQuestion(AddQuestionCommand.builder()
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .date(date)
                .type(questionType).build());
    }

    @Test
    public void shouldAddQuestion() {
        QuestionId questionId = addQuestion(QuestionType.UNCLASSIFIED);

        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder()
                .uuid(questionId.asString())
                .title("My default question")
                .description("No content")
                .creator(testUser.getUsername())
                .date(date)
                .type(QuestionType.UNCLASSIFIED.name()).build();

        assertEquals(questionFacade.getQuestion(questionId), questionDTO);
    }

    @Test
    public void shouldGetAllQuestions() {
        AddQuestionCommand questionCommand = AddQuestionCommand.builder()
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .build();

        int sizeBefore = questionFacade.getNumberOfQuestions();

        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);

        assertEquals(questionFacade.getNumberOfQuestions(),
                sizeBefore + 4);
    }
}

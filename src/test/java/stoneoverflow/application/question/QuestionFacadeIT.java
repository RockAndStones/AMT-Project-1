package stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.stoneoverflow.application.question.*;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;


import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class QuestionFacadeIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private AuthenticatedUserDTO testUser;
    private QuestionFacade questionFacade;
    private DateDTO questionDate;

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
        this.questionDate = new DateDTO(new Date(System.currentTimeMillis()));
    }

    @Test
    public void shouldAddQuestion() {
        int index = questionFacade.getQuestions(QuestionQuery.builder().build()).getQuestions().size();
        // Add question in repository
        AddQuestionCommand questionCommand = AddQuestionCommand.builder()
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .date(questionDate)
                .build();
        questionFacade.addQuestion(questionCommand);

        // Create the expected result
        // Recover the uuid from the question in the repository
        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder()
                .uuid(questionFacade.getQuestions(QuestionQuery.builder().build())
                        .getQuestions().get(index).getUuid())
                .title("My default question")
                .description("No content")
                .creator("test")
                .nbVotes(0)
                .nbViews(0)
                .date(questionDate)
                .type(QuestionType.UNCLASSIFIED.name()).build();

        assertEquals(questionFacade.getQuestions(QuestionQuery.builder().build()).getQuestions().get(index).getUuid(),
                questionDTO.getUuid());
    }

    @Test
    public void shouldGetOnlySQLQuestions() {
        // Add SQL question in repository
        AddQuestionCommand questionCommandSQL = AddQuestionCommand.builder()
                .title("My SQL Question")
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .type(QuestionType.SQL)
                .date(questionDate)
                .build();
        questionFacade.addQuestion(questionCommandSQL);

        // Create the expected result
        // Recover the uuid from the question in the repository
        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder()
                .uuid(questionFacade.getQuestions(QuestionQuery.builder().sortBy(QuestionQuerySortBy.DATE).type(QuestionType.SQL).build())
                        .getQuestions().get(0).getUuid())
                .title("My SQL Question")
                .description("No content")
                .creator(testUser.getUsername())
                .nbVotes(0)
                .nbViews(0)
                .date(questionDate)
                .type(QuestionType.SQL.name()).build();

        // Add other question in repository
        AddQuestionCommand questionCommand = AddQuestionCommand.builder().build();
        questionFacade.addQuestion(questionCommand);

        QuestionsDTO.QuestionDTO q = questionFacade.getQuestions(QuestionQuery.builder().sortBy(QuestionQuerySortBy.DATE).type(QuestionType.SQL).build()).getQuestions().get(0);
        assertEquals(questionDTO.getUuid(), q.getUuid());
    }

    @Test
    public void shouldGetAllQuestions() {
        AddQuestionCommand questionCommand = AddQuestionCommand.builder()
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .build();

        int sizeBefore = questionFacade.getQuestions(QuestionQuery.builder().build()).getQuestions().size();

        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);

        assertEquals(questionFacade.getQuestions(QuestionQuery.builder().build()).getQuestions().size(),
                sizeBefore + 4);
    }
}

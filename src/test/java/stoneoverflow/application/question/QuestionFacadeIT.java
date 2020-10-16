package stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.question.AddQuestionCommand;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
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
    public void init(){
        questionFacade = serviceRegistry.getQuestionFacade();
        this.questionDate = new DateDTO(new Date(System.currentTimeMillis()));
    }

    @Test
    public void shouldAddQuestion() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        int index = questionFacade.getQuestions(QuestionQuery.builder().build()).getQuestions().size();
        // Add question in repository
        AddQuestionCommand questionCommand = AddQuestionCommand.builder()
                .date(questionDate).build();
        questionFacade.addQuestion(questionCommand);

        // Create the expected result
        // Recover the uuid from the question in the repository
        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder()
                .uuid(questionFacade.getQuestions(QuestionQuery.builder().build())
                        .getQuestions().get(index).getUuid())
                .title("My default question")
                .description("No content")
                .creator("Anonymous")
                .nbVotes(0)
                .nbViews(0)
                .date(questionDate)
                .type(QuestionType.UNCLASSIFIED.name()).build();

        assertEquals(questionFacade.getQuestions(QuestionQuery.builder().build()).getQuestions().get(index).getUuid(),
                questionDTO.getUuid());
    }

    @Test
    public void shouldGetOnlySQLQuestions() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // Add SQL question in repository
        AddQuestionCommand questionCommandSQL = AddQuestionCommand.builder()
                .title("My SQL Question")
                .type(QuestionType.SQL)
                .date(questionDate).build();
        questionFacade.addQuestion(questionCommandSQL);

        // Create the expected result
        // Recover the uuid from the question in the repository
        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder()
                .uuid(questionFacade.getQuestions(QuestionQuery.builder().byDate(false).type(QuestionType.SQL).build())
                        .getQuestions().get(0).getUuid())
                .title("My SQL Question")
                .description("No content")
                .creator("Anonymous")
                .nbVotes(0)
                .nbViews(0)
                .date(questionDate)
                .type(QuestionType.SQL.name()).build();

        // Add other question in repository
        AddQuestionCommand questionCommand = AddQuestionCommand.builder().build();
        questionFacade.addQuestion(questionCommand);

        assertEquals(questionFacade.getQuestions(QuestionQuery.builder().byDate(false).type(QuestionType.SQL).build()).getQuestions().get(0),
                questionDTO);
    }

    @Test
    public void shouldGetAllQuestions() {
        AddQuestionCommand questionCommand = AddQuestionCommand.builder().build();
        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder()
                .title("My default question")
                .description("No content")
                .creator("Anonymous")
                .nbVotes(0)
                .type(QuestionType.UNCLASSIFIED.name()).build();

        int sizeBefore = questionFacade.getQuestions(QuestionQuery.builder().build()).getQuestions().size();

        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);

        assertEquals(questionFacade.getQuestions(QuestionQuery.builder().build()).getQuestions().size(),
                sizeBefore + 4);
    }
}

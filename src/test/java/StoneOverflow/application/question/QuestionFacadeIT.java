package StoneOverflow.application.question;

import ch.heigvd.amt.StoneOverflow.application.Question.AddQuestionCommand;
import ch.heigvd.amt.StoneOverflow.application.Question.QuestionFacade;
import ch.heigvd.amt.StoneOverflow.application.Question.QuestionQuery;
import ch.heigvd.amt.StoneOverflow.application.Question.QuestionsDTO;
import ch.heigvd.amt.StoneOverflow.domain.Question.IQuestionRepository;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionType;
import ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory.InMemoryQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionFacadeIT {
    private IQuestionRepository questionRepository;

    @BeforeEach
    public void initializeIdentityManagementFacade() {
        this.questionRepository = new InMemoryQuestionRepository();

    }

    @Test
    public void shouldAddQuestion() {
        QuestionFacade questionFacade = new QuestionFacade(questionRepository);

        // Use a fix date for the test
        Date questionDate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // Add question in repository
        AddQuestionCommand questionCommand = AddQuestionCommand.builder()
                .date(questionDate).build();
        questionFacade.addQuestion(questionCommand);

        // Create the expected result
        // Recover the uuid from the question in the repository
        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder()
                .uuid(questionFacade.getQuestions(QuestionQuery.builder().build())
                        .getQuestions().get(0).getUuid())
                .title("My default question")
                .description("No content")
                .creator("Anonymous")
                .nbVotes(0)
                .nbViews(0)
                .date(questionDate)
                .type(QuestionType.UNCLASSIFIED.name()).build();

        assertEquals(questionFacade.getQuestions(QuestionQuery.builder().build()).getQuestions().get(0),
                questionDTO);
    }

    @Test
    public void shouldGetAllQuestions() {
        QuestionFacade questionFacade = new QuestionFacade(questionRepository);
        AddQuestionCommand questionCommand = AddQuestionCommand.builder().build();
        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder()
                .title("My default question")
                .description("No content")
                .creator("Anonymous")
                .nbVotes(0)
                .type(QuestionType.UNCLASSIFIED.name()).build();

        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommand);

        assertEquals(questionFacade.getQuestions(QuestionQuery.builder().build()).getQuestions().size(),
                4);
    }

    @Test
    public void shouldGetOnlySQLQuestions() {
        QuestionFacade questionFacade = new QuestionFacade(questionRepository);

        // Use a fix date for the test
        Date questionDate = new Date(System.currentTimeMillis());
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
                .uuid(questionFacade.getQuestions(QuestionQuery.builder().build())
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

        assertEquals(questionFacade.getQuestions(QuestionQuery.builder().type(QuestionType.SQL).build()).getQuestions().get(0),
                questionDTO);
    }
}

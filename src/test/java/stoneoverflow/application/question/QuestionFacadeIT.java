package stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.application.question.AddQuestionCommand;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        AddQuestionCommand questionCommand = AddQuestionCommand.builder().build();
        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder()
                .title("My default question")
                .description("No content")
                .creator("Anonymous")
                .nbVotes(0)
                .type(QuestionType.UNCLASSIFIED).build();

        questionFacade.addQuestion(questionCommand);

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
                .type(QuestionType.UNCLASSIFIED).build();

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
        AddQuestionCommand questionCommand = AddQuestionCommand.builder().build();
        QuestionsDTO.QuestionDTO questionDTO = QuestionsDTO.QuestionDTO.builder()
                .title("My SQL Question")
                .description("No content")
                .creator("Anonymous")
                .nbVotes(0)
                .type(QuestionType.SQL).build();

        AddQuestionCommand questionCommandSQL = AddQuestionCommand.builder()
                .title("My SQL Question")
                .type(QuestionType.SQL).build();

        questionFacade.addQuestion(questionCommand);
        questionFacade.addQuestion(questionCommandSQL);

        assertEquals(questionFacade.getQuestions(QuestionQuery.builder().sqlSearch(true).build()).getQuestions().get(0),
                questionDTO);
    }
}

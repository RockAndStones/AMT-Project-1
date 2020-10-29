package stoneoverflow.application.comment;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.answer.*;
import ch.heigvd.amt.stoneoverflow.application.comment.AddCommentCommand;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentFacade;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuery;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentsDTO;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.stoneoverflow.application.question.*;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
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
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CommentFacadeIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private AuthenticatedUserDTO testUser;
    private QuestionFacade       questionFacade;
    private AnswerFacade         answerFacade;
    private CommentFacade        commentFacade;
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
    public void init() throws LoginFailedException {

        this.questionFacade = serviceRegistry.getQuestionFacade();
        this.answerFacade   = serviceRegistry.getAnswerFacade();
        this.commentFacade  = serviceRegistry.getCommentFacade();

        // Use a fix date for all the tests
        this.date = new DateDTO(new Date(System.currentTimeMillis()));

        this.testUser = serviceRegistry.getIdentityManagementFacade().login(LoginCommand.builder()
                .username("test")
                .plaintextPassword("test")
                .build());
    }

    @Test
    public void shouldAddCommentToQuestion() {

        int questionIndex  = questionFacade.getNumberOfQuestions();
        int commentIndex   = commentFacade.getNumberOfQuestionComments();

        // Add question in repository
        AddQuestionCommand questionCommand = AddQuestionCommand.builder()
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .date(date)
                .build();
        questionFacade.addQuestion(questionCommand);

        // Get the QuestionId of the added question
        QuestionId questionId = new QuestionId(questionFacade.getQuestions(
                QuestionQuery.builder()
                        .sortBy(QuestionQuerySortBy.DATE)
                        .sortDescending(true).build(),
                0,
                questionFacade.getNumberOfQuestions()).getQuestions().get(questionIndex).getUuid());

        // Add comment to repository
        AddCommentCommand commentCommand = AddCommentCommand.builder()
                .commentTo(questionId)
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .date(date).build();
        commentFacade.addComment(commentCommand);

        // Create the expected result
        // Recover the uuid from the answer in the repository
        CommentsDTO.CommentDTO commentDTO = CommentsDTO.CommentDTO.builder()
                .uuid(commentFacade.getComments(CommentQuery.builder().build())
                        .getComments().get(commentIndex).getUuid())
                .content("No content")
                .creator(testUser.getUsername())
                .date(date).build();

        assertEquals(commentFacade.getComments(CommentQuery.builder().build())
                .getComments().get(commentIndex), commentDTO);
    }

    @Test
    public void shouldAddCommentToAnswer() {

        int questionIndex = questionFacade.getNumberOfQuestions();
        int answerIndex   = answerFacade.getNumberOfAnswers();
        int commentIndex  = commentFacade.getNumberOfAnswerComments();



        // Add question in repository
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
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .date(date)
                .build();
        answerFacade.addAnswer(answerCommand);

        // Get the AnswerId of the added answer
        // Get the QuestionId of the added question
        AnswerId answerId = new AnswerId(answerFacade.getAnswers(
                AnswerQuery.builder().build(),
                0,
                answerFacade.getNumberOfAnswers()).getAnswers().get(answerIndex).getUuid());



        // Add comment to repository
        AddCommentCommand commentCommand = AddCommentCommand.builder()
                .commentTo(answerId)
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .date(date).build();
        commentFacade.addComment(commentCommand);

        // Create the expected result
        // Recover the uuid from the answer in the repository
        CommentsDTO.CommentDTO commentDTO = CommentsDTO.CommentDTO.builder()
                .uuid(commentFacade.getComments(CommentQuery.builder()
                        .userMessageType(UserMessageType.ANSWER).build())
                            .getComments().get(commentIndex).getUuid())
                .content("No content")
                .creator(testUser.getUsername())
                .date(date).build();

        assertEquals(commentFacade.getComments(CommentQuery.builder()
                .userMessageType(UserMessageType.ANSWER).build())
                    .getComments().get(commentIndex), commentDTO);
    }

    @Test
    public void shouldGetOnlyCommentFromAQuestionId() {

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

        // Add comments to repository
        AddCommentCommand answerCommand1 = AddCommentCommand.builder()
                .commentTo(questionId1)
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .date(date).build();
        AddCommentCommand answerCommand2 = AddCommentCommand.builder()
                .commentTo(questionId2)
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .date(date).build();
        AddCommentCommand answerCommand3 = AddCommentCommand.builder()
                .commentTo(questionId3)
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .build();
        commentFacade.addComment(answerCommand1);
        commentFacade.addComment(answerCommand2);
        commentFacade.addComment(answerCommand3);

        // Prepare comment query
        CommentQuery commentQuery = CommentQuery.builder().commentTo(questionId1).build();

        // Create the expected result
        // Recover the uuid from the comment in the repository
        CommentsDTO.CommentDTO commentDTO = CommentsDTO.CommentDTO.builder()
                .uuid(commentFacade.getComments(commentQuery).getComments().get(0).getUuid())
                .content("No content")
                .creator(testUser.getUsername())
                .date(date).build();

        assertEquals(commentFacade.getComments(commentQuery).getComments().get(0), commentDTO);
    }

    @Test
    public void shouldGetOnlyCommentFromAAnswerId() {

        int questionIndex = questionFacade.getNumberOfQuestions();
        int answerIndex   = answerFacade.getNumberOfAnswers();

        // Insert a question in the repository to respond to
        AddQuestionCommand questionCommand = AddQuestionCommand.builder()
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .date(date)
                .build();
        questionFacade.addQuestion(questionCommand);

        // Define multiple QuestionId to simulate responses to multiple questions
        List<QuestionsDTO.QuestionDTO> questionDTOS = questionFacade.getQuestions(
                QuestionQuery.builder().build(), 0, questionFacade.getNumberOfQuestions()).getQuestions();

        QuestionId  questionId = new QuestionId(questionDTOS.get(questionIndex).getUuid());



        // Insert 3 answers in the repository to comment to
        AddAnswerCommand answerCommand = AddAnswerCommand.builder()
                .answerTo(questionId)
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .date(date)
                .build();
        answerFacade.addAnswer(answerCommand);
        answerFacade.addAnswer(answerCommand);
        answerFacade.addAnswer(answerCommand);

        // Define multiple AnswerId to simulate comment to multiple answers
        List<AnswersDTO.AnswerDTO> answerDTOS = answerFacade.getAnswers(
                AnswerQuery.builder().build(), 0, answerFacade.getNumberOfAnswers()).getAnswers();

        AnswerId  answerId1 = new AnswerId(answerDTOS.get(answerIndex).getUuid());
        AnswerId  answerId2 = new AnswerId(answerDTOS.get(answerIndex + 1).getUuid());
        AnswerId  answerId3 = new AnswerId(answerDTOS.get(answerIndex + 2).getUuid());



        // Add comments to repository
        AddCommentCommand answerCommand1 = AddCommentCommand.builder()
                .commentTo(answerId1)
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .date(date).build();
        AddCommentCommand answerCommand2 = AddCommentCommand.builder()
                .commentTo(answerId2)
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .date(date).build();
        AddCommentCommand answerCommand3 = AddCommentCommand.builder()
                .commentTo(answerId3)
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .build();
        commentFacade.addComment(answerCommand1);
        commentFacade.addComment(answerCommand2);
        commentFacade.addComment(answerCommand3);

        // Prepare comment query
        CommentQuery commentQuery = CommentQuery.builder()
                .userMessageType(UserMessageType.ANSWER)
                .commentTo(answerId1).build();

        // Create the expected result
        // Recover the uuid from the comment in the repository
        CommentsDTO.CommentDTO commentDTO = CommentsDTO.CommentDTO.builder()
                .uuid(commentFacade.getComments(commentQuery).getComments().get(0).getUuid())
                .content("No content")
                .creator(testUser.getUsername())
                .date(date).build();

        assertEquals(commentFacade.getComments(commentQuery).getComments().get(0), commentDTO);
    }
}

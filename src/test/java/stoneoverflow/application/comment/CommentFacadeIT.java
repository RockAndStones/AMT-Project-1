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
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.comment.CommentId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.ArrayList;
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

    private CommentId addComment(Id commentTo) {
        return commentFacade.addComment(AddCommentCommand.builder()
                .commentTo(commentTo)
                .creatorId(testUser.getId())
                .creator(testUser.getUsername())
                .date(date).build());
    }

    @Test
    public void shouldAddCommentToQuestion() {

        QuestionId questionId = addQuestion();
        CommentId commentId = addComment(questionId);

        CommentsDTO.CommentDTO commentDTO = CommentsDTO.CommentDTO.builder()
                .uuid(commentId.asString())
                .description("No content")
                .creator(testUser.getUsername())
                .date(date).build();

        assertEquals(commentFacade.getComment(commentId), commentDTO);
    }

    @Test
    public void shouldAddCommentToAnswer() {

        QuestionId questionId = addQuestion();
        AnswerId   answerId   = addAnswer(questionId);
        CommentId  commentId  = addComment(answerId);

        CommentsDTO.CommentDTO commentDTO = CommentsDTO.CommentDTO.builder()
                .uuid(commentId.asString())
                .description("No content")
                .creator(testUser.getUsername())
                .date(date).build();

        assertEquals(commentFacade.getComment(commentId), commentDTO);
    }

    @Test
    public void shouldGetOnlyCommentFromAQuestionId() {

        QuestionId  questionId1 = addQuestion();
        QuestionId  questionId2 = addQuestion();
        QuestionId  questionId3 = addQuestion();

        CommentId commentId1 = addComment(questionId1);
        CommentId commentId2 = addComment(questionId1);
        addComment(questionId2);
        addComment(questionId3);

        CommentsDTO commentsDTO = commentFacade.getComments(CommentQuery.builder().commentTo(questionId1).build());

        assertEquals(commentsDTO.getComments().size(), 2);

        String resultCommentId1 = commentsDTO.getComments().get(0).getUuid();
        String resultCommentId2 = commentsDTO.getComments().get(1).getUuid();

        assertTrue(
                (resultCommentId1.equals(commentId1.asString()) || resultCommentId1.equals(commentId2.asString()))
                        &&
                        (resultCommentId2.equals(commentId1.asString()) || resultCommentId2.equals(commentId2.asString()))
        );
    }

    @Test
    public void shouldGetOnlyCommentFromAAnswerId() {

        QuestionId  questionId = addQuestion();
        AnswerId answerId1 = addAnswer(questionId);
        AnswerId answerId2 = addAnswer(questionId);
        AnswerId answerId3 = addAnswer(questionId);

        CommentId commentId1 = addComment(answerId1);
        CommentId commentId2 = addComment(answerId1);
        addComment(answerId2);
        addComment(answerId3);

        CommentsDTO commentsDTO = commentFacade.getComments(CommentQuery.builder().commentTo(answerId1).userMessageType(UserMessageType.ANSWER).build());

        assertEquals(2, commentsDTO.getComments().size());

        String resultCommentId1 = commentsDTO.getComments().get(0).getUuid();
        String resultCommentId2 = commentsDTO.getComments().get(1).getUuid();

        assertTrue(
                (resultCommentId1.equals(commentId1.asString()) || resultCommentId1.equals(commentId2.asString()))
                        &&
                        (resultCommentId2.equals(commentId1.asString()) || resultCommentId2.equals(commentId2.asString()))
        );
    }
}

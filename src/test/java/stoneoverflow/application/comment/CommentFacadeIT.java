package stoneoverflow.application.comment;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.comment.AddCommentCommand;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentFacade;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuery;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentsDTO;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
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
public class CommentFacadeIT {
    private DateDTO            commentDate;
    private SimpleDateFormat   formatter;
    private UserId             creatorId;

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private CommentFacade commentFacade;

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
        this.commentFacade = serviceRegistry.getCommentFacade();

        // Use a fix date for all the tests
        this.commentDate       = new DateDTO(new Date(System.currentTimeMillis()));
        this.formatter         = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // Define a UserId to simulate a user responding
        this.creatorId         = new UserId();
    }

    @Test
    public void shouldAddCommentToQuestion() {

        // Define a QuestionId to simulate a response to a question
        QuestionId commentTo = new QuestionId();

        // Add answer to repository
        AddCommentCommand commentCommand = AddCommentCommand.builder()
                .commentTo(commentTo)
                .creatorId(creatorId)
                .date(commentDate).build();
        commentFacade.addComment(commentCommand);

        // Create the expected result
        // Recover the uuid from the answer in the repository
        CommentsDTO.CommentDTO commentDTO = CommentsDTO.CommentDTO.builder()
                .uuid(commentFacade.getComments(CommentQuery.builder()
                        .commentTo(commentTo).build()).getComments().get(0).getUuid())
                .content("No content")
                .creator("Anonymous")
                .date(commentDate).build();

        assertEquals(commentFacade.getComments(CommentQuery.builder()
                .commentTo(commentTo).build()).getComments().get(0), commentDTO);
    }

    @Test
    public void shouldAddCommentToAnswer() {

        // Define a QuestionId to simulate a response to a question
        AnswerId commentTo = new AnswerId();

        // Add answer to repository
        AddCommentCommand commentCommand = AddCommentCommand.builder()
                .commentTo(commentTo)
                .creatorId(creatorId)
                .date(commentDate).build();
        commentFacade.addComment(commentCommand);

        // Create the expected result
        // Recover the uuid from the answer in the repository
        CommentsDTO.CommentDTO commentDTO = CommentsDTO.CommentDTO.builder()
                .uuid(commentFacade.getComments(CommentQuery.builder()
                        .commentTo(commentTo).build()).getComments().get(0).getUuid())
                .content("No content")
                .creator("Anonymous")
                .date(commentDate).build();

        assertEquals(commentFacade.getComments(CommentQuery.builder()
                .commentTo(commentTo).build()).getComments().get(0), commentDTO);
    }

    @Test
    public void shouldGetOnlyCommentFromAQuestionId() {

        // Define multiple QuestionId to simulate responses to multiple questions
        QuestionId commentTo1 = new QuestionId();
        QuestionId commentTo2 = new QuestionId();
        QuestionId commentTo3 = new QuestionId();

        // Add answers to repository
        AddCommentCommand answerCommand1 = AddCommentCommand.builder()
                .commentTo(commentTo1)
                .creatorId(creatorId)
                .date(commentDate).build();
        AddCommentCommand answerCommand2 = AddCommentCommand.builder()
                .commentTo(commentTo2)
                .creatorId(creatorId)
                .date(commentDate).build();
        AddCommentCommand answerCommand3 = AddCommentCommand.builder()
                .commentTo(commentTo3)
                .creatorId(creatorId).build();
        commentFacade.addComment(answerCommand1);
        commentFacade.addComment(answerCommand2);
        commentFacade.addComment(answerCommand3);

        // Create the expected result
        // Recover the uuid from the comment in the repository
        CommentsDTO.CommentDTO commentDTO = CommentsDTO.CommentDTO.builder()
                .uuid(commentFacade.getComments(CommentQuery.builder()
                        .commentTo(commentTo1).build()).getComments().get(0).getUuid())
                .content("No content")
                .creator("Anonymous")
                .date(commentDate).build();

        assertEquals(commentFacade.getComments(CommentQuery.builder()
                .commentTo(commentTo1).build()).getComments().get(0), commentDTO);
    }

    @Test
    public void shouldGetOnlyCommentFromAAnswerId() {

        // Define multiple QuestionId to simulate responses to multiple questions
        AnswerId commentTo1 = new AnswerId();
        AnswerId commentTo2 = new AnswerId();
        AnswerId commentTo3 = new AnswerId();

        // Add answers to repository
        AddCommentCommand answerCommand1 = AddCommentCommand.builder()
                .commentTo(commentTo1)
                .creatorId(creatorId)
                .date(commentDate).build();
        AddCommentCommand answerCommand2 = AddCommentCommand.builder()
                .commentTo(commentTo2)
                .creatorId(creatorId)
                .date(commentDate).build();
        AddCommentCommand answerCommand3 = AddCommentCommand.builder()
                .commentTo(commentTo3)
                .creatorId(creatorId).build();
        commentFacade.addComment(answerCommand1);
        commentFacade.addComment(answerCommand2);
        commentFacade.addComment(answerCommand3);

        // Create the expected result
        // Recover the uuid from the comment in the repository
        CommentsDTO.CommentDTO commentDTO = CommentsDTO.CommentDTO.builder()
                .uuid(commentFacade.getComments(CommentQuery.builder()
                        .commentTo(commentTo1).build()).getComments().get(0).getUuid())
                .content("No content")
                .creator("Anonymous")
                .date(commentDate).build();

        assertEquals(commentFacade.getComments(CommentQuery.builder()
                .commentTo(commentTo1).build()).getComments().get(0), commentDTO);
    }
}

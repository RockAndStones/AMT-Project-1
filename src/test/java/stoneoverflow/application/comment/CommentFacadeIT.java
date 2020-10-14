package stoneoverflow.application.comment;

import ch.heigvd.amt.stoneoverflow.application.comment.AddCommentCommand;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentFacade;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuery;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentsDTO;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryCommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentFacadeIT {
    private ICommentRepository commentRepository;
    private Date               commentDate;
    private SimpleDateFormat   formatter;
    private UserId             creatorId;

    @BeforeEach
    public void initializeIdentityManagementFacade() {
        this.commentRepository = new InMemoryCommentRepository();

        // Use a fix date for all the tests
        this.commentDate       = new Date(System.currentTimeMillis());
        this.formatter         = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // Define a UserId to simulate a user responding
        this.creatorId         = new UserId();
    }

    @Test
    public void shouldAddCommentToQuestion() {
        CommentFacade commentFacade = new CommentFacade(commentRepository);

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
                .date(formatter.format(commentDate)).build();

        assertEquals(commentFacade.getComments(CommentQuery.builder()
                .commentTo(commentTo).build()).getComments().get(0), commentDTO);
    }

    @Test
    public void shouldAddCommentToAnswer() {
        CommentFacade commentFacade = new CommentFacade(commentRepository);

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
                .date(formatter.format(commentDate)).build();

        assertEquals(commentFacade.getComments(CommentQuery.builder()
                .commentTo(commentTo).build()).getComments().get(0), commentDTO);
    }

    @Test
    public void shouldGetOnlyCommentFromAQuestionId() {
        CommentFacade commentFacade = new CommentFacade(commentRepository);

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
                .date(formatter.format(commentDate)).build();

        assertEquals(commentFacade.getComments(CommentQuery.builder()
                .commentTo(commentTo1).build()).getComments().get(0), commentDTO);
    }

    @Test
    public void shouldGetOnlyCommentFromAAnswerId() {
        CommentFacade commentFacade = new CommentFacade(commentRepository);

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
                .date(formatter.format(commentDate)).build();

        assertEquals(commentFacade.getComments(CommentQuery.builder()
                .commentTo(commentTo1).build()).getComments().get(0), commentDTO);
    }
}

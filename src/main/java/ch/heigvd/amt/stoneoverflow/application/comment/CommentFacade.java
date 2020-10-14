package ch.heigvd.amt.stoneoverflow.application.comment;

import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CommentFacade {
    private ICommentRepository commentRepository;

    public CommentFacade(ICommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addComment(AddCommentCommand command) {
        Comment addComment = Comment.builder()
                .commentTo(command.getCommentTo())
                .creatorId(command.getCreatorId())
                .creator(command.getCreator())
                .content(command.getContent())
                .date(command.getDate()).build();
        commentRepository.save(addComment);
    }

    public CommentsDTO getComments(CommentQuery commentQuery) {
        Collection<Comment> commentsByCommentToId = commentRepository.find(commentQuery);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        List<CommentsDTO.CommentDTO> commentsByCommentToIdDTO = commentsByCommentToId.stream().map(
                comment -> CommentsDTO.CommentDTO.builder()
                        .creator(comment.getCreator())
                        .content(comment.getContent())
                        .date(formatter.format(comment.getDate())).build()).collect(Collectors.toList());

        return CommentsDTO.builder().comments(commentsByCommentToIdDTO).build();
    }
}

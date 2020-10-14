package ch.heigvd.amt.stoneoverflow.application.comment;

import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;

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
        Collection<Comment> comments = commentRepository.find(commentQuery);

        List<CommentsDTO.CommentDTO> commentsByCommentToIdDTO = comments.stream().map(
                comment -> CommentsDTO.CommentDTO.builder()
                        .creator(comment.getCreator())
                        .content(comment.getContent())
                        .date(new DateDTO(comment.getDate())).build())
                .collect(Collectors.toList());

        return CommentsDTO.builder().comments(commentsByCommentToIdDTO).build();
    }
}

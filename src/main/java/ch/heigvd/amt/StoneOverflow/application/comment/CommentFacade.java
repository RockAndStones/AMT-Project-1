package ch.heigvd.amt.StoneOverflow.application.comment;

import ch.heigvd.amt.StoneOverflow.domain.Id;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionId;
import ch.heigvd.amt.StoneOverflow.domain.answer.AnswerId;
import ch.heigvd.amt.StoneOverflow.domain.comment.Comment;
import ch.heigvd.amt.StoneOverflow.domain.comment.ICommentRepository;

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

    private CommentsDTO getCommentsByCommentToId(Id commentTo) {
        Collection<Comment> commentsByCommentToId = commentRepository.findByCommentToId(commentTo);

        List<CommentsDTO.CommentDTO> commentsByCommentToIdDTO = commentsByCommentToId.stream().map(
                comment -> CommentsDTO.CommentDTO.builder()
                        .creator(comment.getCreator())
                        .content(comment.getContent())
                        .date(comment.getDate()).build()).collect(Collectors.toList());

        return CommentsDTO.builder().comments(commentsByCommentToIdDTO).build();
    }

    public CommentsDTO getCommentsByCommentToId(QuestionId commentTo) {
        return getCommentsByCommentToId((Id) commentTo);
    }

    public CommentsDTO getCommentsByCommentToId(AnswerId commentTo) {
        return getCommentsByCommentToId((Id) commentTo);
    }
}

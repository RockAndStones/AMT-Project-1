package ch.heigvd.amt.stoneoverflow.application.comment;

import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.CommentId;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommentFacade {
    private ICommentRepository commentRepository;
    private GamificationFacade gamificationFacade;

    public CommentFacade(ICommentRepository commentRepository, GamificationFacade gamificationFacade) {
        this.commentRepository = commentRepository;
        this.gamificationFacade = gamificationFacade;
    }

    public CommentId addComment(AddCommentCommand command) {
        Comment addComment = Comment.builder()
                .commentTo(command.getCommentTo())
                .creatorId(command.getCreatorId())
                .creator(command.getCreator())
                .description(command.getContent())
                .date(command.getDate()).build();
        commentRepository.save(addComment);
        // Send to the gamification
        gamificationFacade.addCommentAsync(command.getCreatorId().asString(), null);
        gamificationFacade.stonerProgressAsync(command.getCreatorId().asString(), null);
        return addComment.getId();
    }

    public CommentsDTO getComments(CommentQuery commentQuery) {
        Collection<Comment> comments = commentRepository.find(commentQuery);

        List<CommentsDTO.CommentDTO> commentsByCommentToIdDTO = comments.stream().map(
                comment -> CommentsDTO.CommentDTO.builder()
                        .uuid(comment.getId().asString())
                        .creator(comment.getCreator())
                        .description(comment.getDescription())
                        .date(new DateDTO(comment.getDate())).build())
                .collect(Collectors.toList());

        return CommentsDTO.builder().comments(commentsByCommentToIdDTO).build();
    }

    public CommentsDTO.CommentDTO getComment(CommentId commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);

        return comment.map(value -> CommentsDTO.CommentDTO.builder()
                .uuid(value.getId().asString())
                .description(value.getDescription())
                .creator(value.getCreator())
                .date(new DateDTO(value.getDate())).build())
                .orElse(null);
    }

    public int getNumberOfComments() {
        return commentRepository.getRepositorySize();
    }

    public int getNumberOfQuestionComments() {
        return commentRepository.find(CommentQuery.builder().build()).size();
    }

    public int getNumberOfAnswerComments() {
        return commentRepository.find(CommentQuery.builder().userMessageType(UserMessageType.ANSWER).build()).size();
    }
}

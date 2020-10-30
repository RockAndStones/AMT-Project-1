package ch.heigvd.amt.stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.application.answer.AnswersDTO;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentsDTO;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteDTO;
import lombok.*;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Builder
@Getter
@EqualsAndHashCode
public class QuestionsDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class QuestionDTO {
        private String        uuid;
        private String        title;
        private String        description;
        private String        creator;
        @EqualsAndHashCode.Exclude
        private AtomicInteger nbViews;
        private DateDTO       date;
        private String        type;

        @Setter private int nbVotes;
        @Setter private int nbAnswers;
        @Setter private VoteDTO voteDTO;
        @Setter private Collection<AnswersDTO.AnswerDTO>   answers;
        @Setter private Collection<CommentsDTO.CommentDTO> comments;

        public String shortDescription() {
            int maxLength = 64;
            String thereIsMore = "...";

            if(this.description.length() > maxLength){
                String shortDescription = this.description.substring(0, maxLength);
                shortDescription += thereIsMore;
                return shortDescription;
            } else{
                return this.description;
            }
        }
    }

    @Singular
    private List<QuestionDTO> questions;
}

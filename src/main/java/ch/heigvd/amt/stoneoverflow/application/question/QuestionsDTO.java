package ch.heigvd.amt.stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.application.answer.AnswersDTO;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentsDTO;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class QuestionsDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class QuestionDTO {
        private String uuid;
        private String title;
        private String description;
        private String creator;
        private int    nbVotes;
        private int    nbViews;
        private int    nbAnswers;
        private Date   date;
        private String type;

        @Setter
        private Collection<AnswersDTO.AnswerDTO> answers;
        @Setter
        private Collection<CommentsDTO.CommentDTO> comments;

        public String shortDescription(){
            int maxLength = 64;
            String thereIsmore = "...";

            if(this.description.length() > maxLength){
                String shortDescription = this.description.substring(0, maxLength);
                shortDescription += thereIsmore;
                return shortDescription;
            } else{
                return this.description;
            }
        }

        public String formattedDate(){
            SimpleDateFormat format = new SimpleDateFormat("MMM dd ''yy"); // '' = single quote
            return format.format(date);
        }

        public String formattedTime(){
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            return format.format(date);
        }
    }

    @Singular
    private List<QuestionDTO> questions;
}
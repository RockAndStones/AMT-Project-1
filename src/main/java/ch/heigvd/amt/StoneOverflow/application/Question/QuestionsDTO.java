package ch.heigvd.amt.StoneOverflow.application.Question;

import ch.heigvd.amt.StoneOverflow.application.answer.AnswersDTO;
import ch.heigvd.amt.StoneOverflow.application.comment.CommentsDTO;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionType;
import ch.heigvd.amt.StoneOverflow.domain.answer.Answer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.Collection;
import java.util.Date;
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
        private String date;
        private String type;

        private Collection<AnswersDTO.AnswerDTO>   answers;
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
    }

    @Singular
    private List<QuestionDTO> questions;
}

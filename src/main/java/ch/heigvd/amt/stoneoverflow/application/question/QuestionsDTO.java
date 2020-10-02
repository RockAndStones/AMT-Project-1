package ch.heigvd.amt.stoneoverflow.application.question;

import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class QuestionsDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class QuestionDTO{
        private String title;
        private String description;
        private String creator;
        private int nbVotes;
        private QuestionType type;

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

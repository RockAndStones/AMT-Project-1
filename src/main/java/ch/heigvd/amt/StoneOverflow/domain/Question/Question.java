package ch.heigvd.amt.StoneOverflow.domain.Question;

import ch.heigvd.amt.StoneOverflow.domain.IEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder(toBuilder = true)
public class Question implements IEntity<Question, QuestionId> {
    @Setter(AccessLevel.NONE)
    private QuestionId id;
    private String title;
    private String description;
    private String creator;
    private int nbVotes;
    @Setter(AccessLevel.NONE)
    private QuestionType questionType;

    @Override
    public Question deepClone() {
        return this.toBuilder()
                .id(new QuestionId(id.asString()))
                .build();
    }

    public void categorizeAs(QuestionType questionType){this.questionType = questionType;}

    public static class QuestionBuilder {

        public Question build(){
            if(id == null){
                id = new QuestionId();
            }

            if(title == null){
                title = "";
            }

            if(description == null){
                description = "";
            }

            if(creator == null){
                creator = "";
            }

            if(questionType == null){
                questionType = QuestionType.UNCLASSIFIED;
            }

            return new Question(id, title, description, creator, nbVotes, questionType);
        }
    }
}

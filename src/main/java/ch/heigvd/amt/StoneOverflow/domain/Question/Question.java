package ch.heigvd.amt.StoneOverflow.domain.Question;

import ch.heigvd.amt.StoneOverflow.domain.IEntity;
import ch.heigvd.amt.StoneOverflow.domain.user.UserId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
@Builder(toBuilder = true)
public class Question implements IEntity<Question, QuestionId> {
    @Setter(AccessLevel.NONE)
    private QuestionId   id;
    private String       title;
    private String       description;
    private UserId       creatorId;
    private String       creator;
    private int          nbVotes;
    private int          nbViews;
    private Date         date;
    @Setter(AccessLevel.NONE)
    private QuestionType questionType;

    @Override
    public Question deepClone() {
        return this.toBuilder()
                .id(new QuestionId(id.asString()))
                .build();
    }

    public void addView(){
        this.nbViews++;
    }

    public void categorizeAs(QuestionType questionType){this.questionType = questionType;}

    public static class QuestionBuilder {

        public Question build(){

            if(id == null)
                id = new QuestionId();

            if(title == null)
                title = "";

            if(description == null)
                description = "";

            if(creatorId == null)
                creatorId = new UserId();

            if(creator == null)
                creator = "";

            if (date == null)
                date = new Date(System.currentTimeMillis());

            if(questionType == null)
                questionType = QuestionType.UNCLASSIFIED;

            return new Question(id, title, description, creatorId, creator, nbVotes, nbViews, date, questionType);
        }
    }
}

package ch.heigvd.amt.stoneoverflow.domain.question;

import ch.heigvd.amt.stoneoverflow.domain.IEntity;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import lombok.*;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
public class Question implements IEntity<Question, QuestionId> {
    @Setter(AccessLevel.NONE)
    private QuestionId    id;
    private String        title;
    private String        description;
    private UserId        creatorId;
    private String        creator;
    @EqualsAndHashCode.Exclude
    private AtomicInteger nbViews;
    private Date          date;
    @Setter(AccessLevel.NONE)
    private QuestionType questionType;

    @Override
    public Question deepClone() {
        return this.toBuilder()
                .id(new QuestionId(id.asString()))
                .build();
    }

    public void addView(){
        this.nbViews.getAndIncrement();
    }

    // Rewrite the getter to get an int and not an AtomicInteger
    public int getNbViewsAsInt(){
        return this.nbViews.get();
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

            if(nbViews == null)
                nbViews = new AtomicInteger(0);

            if (date == null)
                date = new Date(System.currentTimeMillis());

            if(questionType == null)
                questionType = QuestionType.UNCLASSIFIED;

            return new Question(id, title, description, creatorId, creator, nbViews, date, questionType);
        }
    }
}

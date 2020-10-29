package ch.heigvd.amt.stoneoverflow.domain.question;

import ch.heigvd.amt.stoneoverflow.domain.IEntity;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder(toBuilder = true)
public class Question implements IEntity<Question, QuestionId> {
    @Setter(AccessLevel.NONE)
    private QuestionId    id;
    private String        title;
    private String        description;
    private UserId        creatorId;
    private String        creator;
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

    public void addView() {
        this.nbViews.incrementAndGet();
    }

    public int getNbViewsAsInt() {
        return nbViews.get();
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

            return new Question(id, title, description, creatorId, creator, nbViews, date, questionType);
        }
    }
}

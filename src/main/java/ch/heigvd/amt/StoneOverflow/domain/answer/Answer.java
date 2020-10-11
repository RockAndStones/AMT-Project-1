package ch.heigvd.amt.StoneOverflow.domain.answer;

import ch.heigvd.amt.StoneOverflow.domain.IEntity;
import ch.heigvd.amt.StoneOverflow.domain.Question.QuestionId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
@Builder(toBuilder = true)
public class Answer implements IEntity<Answer, AnswerId> {
    @Setter(AccessLevel.NONE)
    private AnswerId   id;
    private QuestionId answerTo;
    private String     description;
    private String     creator;
    private int        nbVotes;
    private Date       date;

    @Override
    public Answer deepClone() {
        return this.toBuilder()
                .id(new AnswerId(id.asString()))
                .build();
    }

    public static class AnswerBuilder {
        public Answer build() {

            if (id == null)
                id = new AnswerId();

            if (answerTo == null)
                throw new IllegalArgumentException("'answerTo' field cannot be null or empty");

            if (description == null || description.isEmpty())
                throw new IllegalArgumentException("Description cannot be null or empty");

            if (creator == null || creator.isEmpty())
                throw new IllegalArgumentException("Creator cannot be null or empty");

            if (date == null)
                date = new Date(System.currentTimeMillis());

            return new Answer(id, answerTo, description, creator, nbVotes, date);
        }
    }
}

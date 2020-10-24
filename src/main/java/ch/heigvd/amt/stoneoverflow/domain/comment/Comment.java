package ch.heigvd.amt.stoneoverflow.domain.comment;

import ch.heigvd.amt.stoneoverflow.domain.IEntity;
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
@Builder(toBuilder = true)
public class Comment implements IEntity<Comment, CommentId> {
    @Setter(AccessLevel.NONE)
    private CommentId id;
    private Id        commentTo;
    private UserId    creatorId;
    private String    creator;
    private String    description;
    private Date      date;

    @Override
    public Comment deepClone() {
        return this.toBuilder()
                .id(new CommentId(id.asString()))
                .build();
    }

    public static class CommentBuilder {

        public Comment build() {
            if (id == null)
                id = new CommentId();

            if (commentTo == null)
                throw new IllegalArgumentException("'commentTo' field cannot be null");

            if (creatorId == null)
                throw new IllegalArgumentException("CreatorId cannot be null");

            if (creator == null)
                throw new IllegalArgumentException("Creator cannot be null");

            if (description == null || description.isEmpty())
                throw new IllegalArgumentException("Content cannot be null or empty");

            if (date == null)
                date = new Date(System.currentTimeMillis());

            return new Comment(id, commentTo, creatorId, creator, description, date);
        }
    }
}

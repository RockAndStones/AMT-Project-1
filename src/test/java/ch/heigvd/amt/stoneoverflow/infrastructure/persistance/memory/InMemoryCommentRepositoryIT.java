package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuery;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuerySortBy;
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryCommentRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)// To not have tests making others fail because runned before
public class InMemoryCommentRepositoryIT {
    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    @Named("InMemoryCommentRepository")
    private InMemoryCommentRepository inMemoryCommentRepository;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt");
        return archive;
    }

    private Comment generateComment(Id id, Date date) {
        return Comment.builder()
                .commentTo(id)
                .creatorId(new UserId())
                .creator("Anonymous")
                .description("Default description")
                .date(date).build();
    }

    private Comment generateComment(QuestionId questionId) {
        return generateComment(questionId, new Date());
    }

    private Comment generateComment(AnswerId answerId) {
        return generateComment(answerId, new Date());
    }

    private Comment generateComment(Date date) {
        return generateComment(new QuestionId(), date);
    }

    private Comment generateComment() {
        return generateComment(new QuestionId());
    }

    @Test
    public void shouldFindAllComments() {
        inMemoryCommentRepository.save(generateComment());
        inMemoryCommentRepository.save(generateComment());
        inMemoryCommentRepository.save(generateComment());

        assertEquals(3, inMemoryCommentRepository.findAll().size());
    }

    @Test
    public void shouldFindCommentsByCommentTo() {
        QuestionId questionId = new QuestionId();
        AnswerId answerId = new AnswerId();

        inMemoryCommentRepository.save(generateComment(questionId));
        inMemoryCommentRepository.save(generateComment(questionId));
        inMemoryCommentRepository.save(generateComment(questionId));
        inMemoryCommentRepository.save(generateComment(answerId));
        inMemoryCommentRepository.save(generateComment(answerId));
        inMemoryCommentRepository.save(generateComment());
        inMemoryCommentRepository.save(generateComment());

        assertEquals(3, inMemoryCommentRepository.find(CommentQuery.builder().commentTo(questionId).build()).size());
        assertEquals(2, inMemoryCommentRepository.find(CommentQuery.builder().commentTo(answerId).build()).size());
    }

    @Test
    public void shouldFindCommentsByDate() {
        ArrayList<Comment> commentsSortedByDate = new ArrayList<>();
        commentsSortedByDate.add(generateComment(new Date(System.currentTimeMillis() - 1002)));
        commentsSortedByDate.add(generateComment(new Date(System.currentTimeMillis() - 1001)));
        commentsSortedByDate.add(generateComment(new Date(System.currentTimeMillis() - 1000)));

        inMemoryCommentRepository.save(commentsSortedByDate.get(1));
        inMemoryCommentRepository.save(commentsSortedByDate.get(2));
        inMemoryCommentRepository.save(commentsSortedByDate.get(0));

        assertEquals(commentsSortedByDate, new ArrayList<>(inMemoryCommentRepository.find(
                CommentQuery.builder().sortBy(CommentQuerySortBy.DATE).build())).subList(0, 3));
    }
}

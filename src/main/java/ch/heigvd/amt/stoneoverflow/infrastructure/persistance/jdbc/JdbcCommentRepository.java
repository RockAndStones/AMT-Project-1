package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuery;
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.CommentId;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@ApplicationScoped
@Named("JdbcCommentRepository")
public class JdbcCommentRepository implements ICommentRepository {

    @Resource(lookup = "jdbc/StoneOverflowDS")
    private DataSource dataSource;

    private Comment resultSetToComment(ResultSet rs, Id commentTo) throws SQLException {
        return Comment.builder()
                .id(new CommentId(rs.getString("id")))
                .commentTo(commentTo)
                .description(rs.getString("description"))
                .creatorId(new UserId(rs.getString("creatorId")))
                .creator(rs.getString("creator"))
                .date(new Date(rs.getTimestamp("date").getTime()))
                .build();
    }

    private Collection<Comment> resultSetToQuestionComments(ResultSet rs) throws SQLException {
        Collection<Comment> comments = new LinkedList<>();
        while(rs.next()) {
            comments.add(resultSetToComment(rs, new QuestionId(rs.getString("commentTo"))));
        }
        return comments;
    }

    private Collection<Comment> resultSetToAnswerComments(ResultSet rs) throws SQLException {
        Collection<Comment> comments = new LinkedList<>();
        while(rs.next()) {
            comments.add(resultSetToComment(rs, new QuestionId(rs.getString("commentTo"))));
        }
        return comments;
    }

    private PreparedStatement getQueryStatement(Connection con, CommentQuery query) throws SQLException {
        String view = "";
        String where = "";
        String direction = query.isSortDescending() ? "DESC" : "ASC";

        // Set WHERE statement if requested
        if (query.getCommentTo() != null)
            where = String.format(" WHERE commentTo='%s'", query.getCommentTo().asString());

        // Choose the view to SELECT FROM
        switch (query.getUserMessageType()) {
            case QUESTION:
                view = "vQuestionComment";
                break;
            case ANSWER:
                view = "vAnswerComment";
                break;
            default:
                break;
        }

        // Prepare and return final statement
        return con.prepareStatement(String.format("SELECT * FROM %s%s ORDER BY %s %s",
                view,
                where,
                query.getSortBy().getSqlFieldName(),
                direction));
    }

    @Override
    public Collection<Comment> find(CommentQuery commentQuery) {
        Collection<Comment> comments = new LinkedList<>();

        try {
            Connection con = dataSource.getConnection();

            PreparedStatement psQuestion = getQueryStatement(con, commentQuery);
            ResultSet rsComments = psQuestion.executeQuery();

            switch (commentQuery.getUserMessageType()) {
                case QUESTION:
                    comments.addAll(resultSetToQuestionComments(rsComments));
                    break;
                case ANSWER:
                    comments.addAll(resultSetToAnswerComments(rsComments));
                    break;
                default:
                    break;
            }


            psQuestion.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return comments;
    }

    @Override
    public void save(Comment comment) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("INSERT INTO Comment VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, comment.getId().asString());
            ps.setString(2, comment.getCreatorId().asString());
            ps.setString(3, comment.getCommentTo().asString());
            ps.setString(4, comment.getDescription());
            ps.setDate(5, new Date(comment.getDate().getTime()));
            ps.executeUpdate();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Comment comment) {
        throw new UnsupportedOperationException("Update is not supported on comment");
    }

    @Override
    public void remove(CommentId commentId) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement psDeleteFromAnswer = con.prepareStatement("DELETE FROM Comment WHERE id=?");
            psDeleteFromAnswer.setString(1, commentId.asString());
            psDeleteFromAnswer.execute();

            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Comment> findById(CommentId commentId) {
        throw new UnsupportedOperationException("findById is not supported on comment");
    }

    @Override
    public Collection<Comment> findAll() {
        Collection<Comment> comments = new LinkedList<>();

        try {
            Connection con = dataSource.getConnection();

            PreparedStatement psQuestionComment = con.prepareStatement("SELECT * FROM vQuestionComment");
            PreparedStatement psAnswerComment   = con.prepareStatement("SELECT * FROM vAnswerComment");
            ResultSet rsQuestionComment = psQuestionComment.executeQuery();
            ResultSet rsAnswerComment   = psAnswerComment.executeQuery();

            comments.addAll(resultSetToQuestionComments(rsQuestionComment));
            comments.addAll(resultSetToAnswerComments(rsAnswerComment));

            psQuestionComment.close();
            psAnswerComment.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return comments;
    }

    @Override
    public int getRepositorySize() {
        int size = 0;
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement( "SELECT COUNT(*) FROM Comment");
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                size = rs.getInt(1);

            ps.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return size;
    }
}

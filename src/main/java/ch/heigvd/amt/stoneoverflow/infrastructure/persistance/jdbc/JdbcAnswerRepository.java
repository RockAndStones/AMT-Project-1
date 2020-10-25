package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception.DataCorruptionException;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@ApplicationScoped
@Named("JdbcAnswerRepository")
public class JdbcAnswerRepository implements IAnswerRepository {

    @Resource(lookup = "jdbc/StoneOverflowDS")
    private DataSource dataSource;

    private Collection<Answer> resultSetToAnswers(ResultSet rs) throws SQLException {
        Collection<Answer> answers = new LinkedList<>();

        while(rs.next()) {
            Answer a = Answer.builder()
                    .id(new AnswerId(rs.getString("id")))
                    .answerTo(new QuestionId(rs.getString("answerTo")))
                    .description(rs.getString("description"))
                    .creatorId(new UserId(rs.getString("creatorId")))
                    .creator(rs.getString("creator"))
                    .date(new Date(rs.getTimestamp("date").getTime()))
                    .build();
            answers.add(a);
        }

        return answers;
    }

    private PreparedStatement getQueryStatement(Connection con, AnswerQuery query, int offset, int limit) throws SQLException {
        String where = "";
        String direction = query.isSortDescending() ? "DESC" : "ASC";

        // Set WHERE statement if requested
        if (query.getAnswerTo() != null)
            where = String.format(" WHERE answerTo='%s'", query.getAnswerTo().asString());

        String qr = String.format("SELECT *FROM vAnswer%s ORDER BY %s %s LIMIT %d, %d",
                where,
                query.getSortBy().getSqlFieldName(),
                direction,
                offset,
                limit);

        // Prepare and return final statement
        return con.prepareStatement(qr);
    }

    @Override
    public Collection<Answer> find(AnswerQuery answerQuery, int offset, int limit) {
        Collection<Answer> answers = new LinkedList<>();

        try {
            Connection con = dataSource.getConnection();

            PreparedStatement psAnswer = getQueryStatement(con, answerQuery, offset, limit);
            ResultSet rsAnswer = psAnswer.executeQuery();

            answers.addAll(resultSetToAnswers(rsAnswer));
            psAnswer.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return answers;
    }

    @Override
    public int getNumberOfAnswers(QuestionId questionId) {
        int size = 0;
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(String.format("SELECT COUNT(*) FROM Answer WHERE idQuestion='%s'", questionId.asString()));
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                size = rs.getInt(1);
            ps.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return size;
    }

    @Override
    public void save(Answer answer) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("INSERT INTO UserMessage VALUES (?, ?, ?, ?)");
            ps.setString(1, answer.getId().asString());
            ps.setString(2, answer.getCreatorId().asString());
            ps.setString(3, answer.getDescription());
            ps.setDate(4, new Date(answer.getDate().getTime()));
            ps.executeUpdate();

            ps = con.prepareStatement("INSERT INTO Answer VALUES (?, ?)");
            ps.setString(1, answer.getId().asString());
            ps.setString(2, answer.getAnswerTo().asString());
            ps.executeUpdate();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }
    }

    @Override
    public void update(Answer answer) {
        throw new UnsupportedOperationException("update is not yet implemented");
    }

    @Override
    public void remove(AnswerId answerId) {
        try {
            Connection con = dataSource.getConnection();

            Statement statement = con.createStatement();
            PreparedStatement psDeleteFromUserMessage = con.prepareStatement("DELETE FROM UserMessage WHERE id=?");
            PreparedStatement psDeleteFromAnswer = con.prepareStatement("DELETE FROM Answer WHERE id=?");

            statement.execute(psDeleteFromUserMessage.toString());
            statement.execute(psDeleteFromAnswer.toString());

            // start transaction block
            con.setAutoCommit(false); // default true

            psDeleteFromUserMessage.setString(1, answerId.asString());
            psDeleteFromAnswer.setString(1, answerId.asString());

            // execute commands
            psDeleteFromUserMessage.execute();
            psDeleteFromAnswer.execute();

            // end transaction block, commit changes
            con.commit();

            // good practice to set it back to default true
            con.setAutoCommit(true);

            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }
    }

    @Override
    public Optional<Answer> findById(AnswerId answerId) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM vAnswer WHERE id=?");
            ps.setString(1, answerId.asString());

            ResultSet rs = ps.executeQuery();
            Collection<Answer> answers = resultSetToAnswers(rs);
            if (answers.isEmpty())
                return Optional.empty();
            else if (answers.size() > 1)
                throw new DataCorruptionException("Data store is corrupted. More than one question with the same id");

            ps.close();
            con.close();

            return answers.stream().findFirst();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return Optional.empty();
    }

    @Override
    public Collection<Answer> findAll() {

        Collection<Answer> answers = new LinkedList<>();

        try {
            Connection con = dataSource.getConnection();

            PreparedStatement psQuestion = con.prepareStatement("SELECT * FROM vAnswer");
            ResultSet rsAnswer = psQuestion.executeQuery();

            answers.addAll(resultSetToAnswers(rsAnswer));
            psQuestion.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return answers;
    }

    @Override
    public int getRepositorySize() {
        int size = 0;
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement( "SELECT COUNT(*) FROM Answer");
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                size = rs.getInt(1);

            ps.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return size;
    }
}

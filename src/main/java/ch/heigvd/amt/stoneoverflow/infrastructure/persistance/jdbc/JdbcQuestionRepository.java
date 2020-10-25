package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception.DataCorruptionException;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
@Named("JdbcQuestionRepository")
public class JdbcQuestionRepository implements IQuestionRepository {
    @Resource(lookup = "jdbc/StoneOverflowDS")
    private DataSource dataSource;

    public JdbcQuestionRepository() {
    }

    public JdbcQuestionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Collection<Question> resultSetToQuestions(ResultSet rs) throws SQLException {
        Collection<Question> questions = new LinkedList<>();

        while(rs.next()) {
            Question q = Question.builder()
                    .id(new QuestionId(rs.getString("id")))
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .creator(rs.getString("creator"))
                    .creatorId(new UserId(rs.getString("creatorId")))
                    .nbViews(new AtomicInteger(rs.getInt("nbViews")))
                    .date(new Date(rs.getTimestamp("date").getTime()))
                    .build();
            questions.add(q);
        }

        return questions;
    }

    private String getQuerySQL(String search, String sortFieldName, boolean isSortDescending, int offset, int limit) {
        String direction = isSortDescending ? "DESC" : "ASC";

        if (search.isEmpty()) {
            return String.format("SELECT * FROM vQuestion ORDER BY %s %s LIMIT %d, %d",
                    sortFieldName,
                    direction,
                    offset,
                    limit);
        } else {
            return String.format("SELECT * FROM vQuestion WHERE title LIKE ? ORDER BY %s %s LIMIT %d, %d",
                    sortFieldName,
                    direction,
                    offset,
                    limit);
        }
    }

    private PreparedStatement getQueryStatement(Connection con, QuestionQuery query, int offset, int limit) throws SQLException {
        PreparedStatement ps = con.prepareStatement(getQuerySQL(
                query.getSearchCondition(),
                query.getSortBy().getSqlFieldName(),
                query.isSortDescending(),
                offset,
                limit)
        );

        if (!query.getSearchCondition().isEmpty())
            ps.setString(1, "%" + query.getSearchCondition() + "%");

        return ps;
    }

    @Override
    public Collection<Question> find(QuestionQuery questionQuery, int offset, int limit) {
        Collection<Question> questions = new LinkedList<>();

        try {
            Connection con = dataSource.getConnection();

            PreparedStatement psQuestion = getQueryStatement(con, questionQuery, offset, limit);
            ResultSet rsQuestion = psQuestion.executeQuery();

            questions.addAll(resultSetToQuestions(rsQuestion));
            psQuestion.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return questions;
    }

    @Override
    public void save(Question question) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("INSERT INTO UserMessage VALUES (?, ?, ?, ?)");
            ps.setString(1, question.getId().asString());
            ps.setString(2, question.getCreatorId().asString());
            ps.setString(3, question.getDescription());
            ps.setTimestamp(4, new Timestamp(question.getDate().getTime()));
            ps.executeUpdate();

            ps = con.prepareStatement("INSERT INTO Question VALUES (?, ?, ?)");
            ps.setString(1, question.getId().asString());
            ps.setString(2, question.getTitle());
            ps.setInt(3, question.getNbViews());
            ps.executeUpdate();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }
    }

    @Override
    public void update(Question question) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("UPDATE Question SET nbViews=? WHERE id=?");
            ps.setInt(1, question.getNbViews());
            ps.setString(2, question.getId().asString());
            ps.executeUpdate();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }
    }

    @Override
    public void remove(QuestionId questionId) {
        //todo: implement remove method
        throw new UnsupportedOperationException("Remove is not yet implemented");
    }

    @Override
    public Optional<Question> findById(QuestionId questionId) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM vQuestion WHERE id=?");
            ps.setString(1, questionId.asString());

            ResultSet rs = ps.executeQuery();
            Collection<Question> questions = resultSetToQuestions(rs);
            if (questions.isEmpty())
                return Optional.empty();
            else if (questions.size() > 1)
                throw new DataCorruptionException("Data store is corrupted. More than one question with the same id");

            ps.close();
            con.close();

            return questions.stream().findFirst();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return Optional.empty();
    }

    @Override
    public Collection<Question> findAll() {
        Collection<Question> questions = new LinkedList<>();

        try {
            Connection con = dataSource.getConnection();

            PreparedStatement psQuestion = con.prepareStatement("SELECT * FROM vQuestion");
            ResultSet rsQuestion = psQuestion.executeQuery();

            questions.addAll(resultSetToQuestions(rsQuestion));
            psQuestion.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return questions;
    }

    @Override
    public int getRepositorySize() {
        int size = 0;
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement( "SELECT COUNT(*) FROM Question");
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

package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

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

    @Override
    public Collection<Question> find(QuestionQuery questionQuery) {
        return findAll();
    }

    @Override
    public Collection<Question> findByVotes(QuestionQuery questionQuery) {
        // TODO
        return findAll();
    }

    @Override
    public Collection<Question> findByViews(QuestionQuery questionQuery) {
        // TODO
        return findAll();
    }

    @Override
    public Collection<Question> findByType(QuestionQuery questionQuery) {
        // TODO
        return findAll();
    }

    @Override
    public void save(Question question) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("INSERT INTO UserMessage VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, question.getId().asString());
            ps.setString(2, question.getCreatorId().asString());
            ps.setString(3, question.getDescription());
            ps.setInt(4, question.getNbVotes());
            ps.setDate(5, new Date(question.getDate().getTime()));
            ps.executeUpdate();

            ps = con.prepareStatement("INSERT INTO Question VALUES (?, ?)");
            ps.setString(1, question.getId().asString());
            ps.setString(2, question.getTitle());
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
                    "SELECT q.id, q.title, u.username, um.description, um.nbVotes, um.date " +
                            "FROM Question AS q " +
                            "INNER JOIN UserMessage AS um on q.id = um.id " +
                            "INNER JOIN User AS u ON q.id=u.id WHERE q.id=?");
            ps.setString(1, questionId.asString());

            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return Optional.empty();

            Question q = Question.builder()
                    .id(new QuestionId(rs.getString("id")))
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .creator(rs.getString("username"))
                    .nbVotes(rs.getInt("nbVotes"))
                    .date(rs.getDate("date"))
                    .build();

            ps.close();
            con.close();

            return Optional.of(q);
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

            PreparedStatement ps = con.prepareStatement(
                    "SELECT q.id, q.title, u.username, um.description, um.nbVotes, um.date " +
                            "FROM Question AS q " +
                            "INNER JOIN UserMessage AS um on q.id = um.id " +
                            "INNER JOIN User AS u ON um.idUser = u.id");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Question q = Question.builder()
                        .id(new QuestionId(rs.getString("id")))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .creator(rs.getString("username"))
                        .nbVotes(rs.getInt("nbVotes"))
                        .date(rs.getDate("date"))
                        .build();

                questions.add(q);
            }
            ps.close();
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

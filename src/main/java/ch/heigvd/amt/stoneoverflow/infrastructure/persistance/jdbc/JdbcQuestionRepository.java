package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;

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
    public void update(Question question) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("UPDATE UserMessage SET nbVotes=? WHERE id=?");
            ps.setInt(1, question.getNbVotes());
            ps.setString(2, question.getId().asString());
            ps.executeUpdate();

            ps = con.prepareStatement("UPDATE Question SET nbViews=? WHERE id=?");
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
                    "SELECT q.id, q.title, q.nbViews, u.username, um.description, um.nbVotes, um.date " +
                            "FROM Question AS q " +
                            "INNER JOIN UserMessage AS um on q.id = um.id " +
                            "INNER JOIN User AS u ON um.idUser=u.id WHERE q.id=?");
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
                    .nbViews(rs.getInt("nbViews"))
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

            PreparedStatement psQuestion = con.prepareStatement("SELECT * FROM vQuestion");
            ResultSet rsQuestion = psQuestion.executeQuery();

            while(rsQuestion.next()) {
                Question q = Question.builder()
                        .id(new QuestionId(rsQuestion.getString("id")))
                        .title(rsQuestion.getString("title"))
                        .description(rsQuestion.getString("description"))
                        .creator(rsQuestion.getString("creator"))
                        .creatorId(new UserId(rsQuestion.getString("creatorId")))
                        .nbViews(rsQuestion.getInt("nbViews"))
                        .nbVotes(rsQuestion.getInt("nbVotes"))
                        .date(rsQuestion.getDate("date"))
                        .build();
                questions.add(q);
            }
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

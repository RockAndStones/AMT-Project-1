package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.domain.vote.IVoteRepository;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import ch.heigvd.amt.stoneoverflow.domain.vote.VoteId;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception.DataCorruptionException;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import static ch.heigvd.amt.stoneoverflow.domain.vote.Vote.VoteType.UP;

@ApplicationScoped
@Named("JdbcVoteRepository")
public class JdbcVoteRepository implements IVoteRepository {

    @Resource(lookup = "jdbc/StoneOverflowDS")
    private DataSource dataSource;

    private Vote resultSetToVote(ResultSet rs, Id votedObject) throws SQLException {
        return Vote.builder()
                .id(new VoteId(rs.getString("id")))
                .votedBy(new UserId(rs.getString("idUser")))
                .votedObject(votedObject)
                .voteType(rs.getBoolean("voteUp") ? UP : Vote.VoteType.DOWN).build();
    }

    private Collection<Vote> resultSetToQuestionVotes(ResultSet rs) throws SQLException {
        Collection<Vote> votes = new LinkedList<>();
        while(rs.next())
            votes.add(resultSetToVote(rs, new QuestionId(rs.getString("idUserMessage"))));
        return votes;
    }

    private Collection<Vote> resultSetToAnswerVotes(ResultSet rs) throws SQLException {
        Collection<Vote> votes = new LinkedList<>();
        while(rs.next())
            votes.add(resultSetToVote(rs, new AnswerId(rs.getString("idUserMessage"))));
        return votes;
    }

    @Override
    public int findNbVotes(Id id, UserMessageType userMessageType) {
        int size = 0;
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = null;


            switch (userMessageType) {
                case QUESTION:
                    ps = con.prepareStatement("SELECT nbVotes FROM vQuestion WHERE id=?");
                    break;
                case ANSWER:
                    ps = con.prepareStatement("SELECT nbVotes FROM vAnswer WHERE id=?");
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown UserMessageType");
            }
            ps.setString(1, id.asString());

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
    public Optional<Vote> findVote(VoteId id, UserMessageType userMessageType) {
        Optional<Vote> vote = Optional.empty();
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT * FROM Vote WHERE id=?");
            ps.setString(1, id.asString());

            vote = findVote(ps, userMessageType);

            ps.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }
        return vote;
    }

    @Override
    public Optional<Vote> findVote(Id id, UserId userId, UserMessageType userMessageType) {
        Optional<Vote> vote = Optional.empty();
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT * FROM Vote WHERE idUserMessage=? AND idUser=?");
            ps.setString(1, id.asString());
            ps.setString(2, userId.asString());

            vote = findVote(ps, userMessageType);

            ps.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }
        return vote;
    }

    private Optional<Vote> findVote(PreparedStatement ps, UserMessageType userMessageType) {
        try {
            ResultSet rs = ps.executeQuery();

            Collection<Vote> votes = new LinkedList<>();
            switch (userMessageType) {
                case QUESTION:
                    votes = resultSetToQuestionVotes(rs);
                    break;
                case ANSWER:
                    votes = resultSetToAnswerVotes(rs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown UserMessageType");
            }

            if (votes.isEmpty())
                return Optional.empty();
            else if (votes.size() > 1)
                throw new DataCorruptionException("Data store is corrupted. More than one question with the same id");

            return votes.stream().findFirst();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return Optional.empty();
    }

    @Override
    public void save(Vote vote) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("INSERT INTO Vote VALUES ( ?, ?, ?, ?)");
            ps.setString(1, vote.getId().asString());
            ps.setString(2, vote.getVotedBy().asString());
            ps.setString(3, vote.getVotedObject().asString());
            ps.setBoolean(4, vote.getVoteType() == UP);
            ps.executeUpdate();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }
    }

    @Override
    public void update(Vote vote) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("UPDATE Vote SET voteUp=? WHERE id=?");
            ps.setBoolean(1, vote.getVoteType() == UP);
            ps.setString(2, vote.getId().asString());
            ps.executeUpdate();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }
    }

    @Override
    public void remove(VoteId voteId) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("DELETE FROM Vote WHERE id=?");
            ps.setString(1, voteId.asString());
            ps.executeUpdate();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }
    }

    @Override
    public Optional<Vote> findById(VoteId voteId) {

        Optional<Vote> vote = Optional.empty();

        try {
            Connection con = dataSource.getConnection();

            // Prepare statement for vote from question and from answer
            PreparedStatement psQuestionVote = con.prepareStatement("SELECT * FROM vQuestionVote WHERE id=?");
            psQuestionVote.setString(1, voteId.asString());
            PreparedStatement psAnswerVote = con.prepareStatement("SELECT * FROM vAnswerVote WHERE id=?");
            psAnswerVote.setString(1, voteId.asString());

            // Try to find the vote from question
            ResultSet rs = psQuestionVote.executeQuery();
            Collection<Vote> votes = resultSetToQuestionVotes(rs);

            if (votes.size() == 1)
                return votes.stream().findFirst();
            else if (votes.size() > 1)
                throw new DataCorruptionException("Data store is corrupted. More than one question with the same id");

            // Try to find the vote from answer
            rs = psAnswerVote.executeQuery();
            votes = resultSetToAnswerVotes(rs);

            if (votes.size() == 1)
                return votes.stream().findFirst();
            else if (votes.size() > 1)
                throw new DataCorruptionException("Data store is corrupted. More than one question with the same id");

            psQuestionVote.close();
            psAnswerVote.close();
            con.close();

            return votes.stream().findFirst();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return Optional.empty();
    }

    @Override
    public Collection<Vote> findAll() {
        Collection<Vote> votes = new LinkedList<>();

        try {
            Connection con = dataSource.getConnection();

            PreparedStatement psQuestionVote = con.prepareStatement("SELECT * FROM vQuestionVote");
            PreparedStatement psAnswerVote   = con.prepareStatement("SELECT * FROM vAnswerVote");
            ResultSet rsQuestionVote = psQuestionVote.executeQuery();
            ResultSet rsAnswerVote   = psAnswerVote.executeQuery();

            votes.addAll(resultSetToQuestionVotes(rsQuestionVote));
            votes.addAll(resultSetToAnswerVotes(rsAnswerVote));

            psQuestionVote.close();
            psAnswerVote.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }

        return votes;
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
            //todo: log/handle error
            System.out.println(ex);
        }
        return size;
    }
}

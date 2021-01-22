package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.stoneoverflow.domain.user.IUserRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception.IntegrityConstraintViolationException;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

@ApplicationScoped
@Named("JdbcUserRepository")
public class JdbcUserRepository implements IUserRepository {
    @Resource(lookup = "jdbc/StoneOverflowDS")
    private DataSource dataSource;

    public JdbcUserRepository() {
    }

    public JdbcUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT id, username, mail, firstName, lastName, password FROM User WHERE LOWER(username)=LOWER(?)");
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                ps.close();
                con.close();
                return Optional.empty();
            }

            User u = User.builder()
                    .id(new UserId(rs.getString("id")))
                    .username(rs.getString("username"))
                    .email(rs.getString("mail"))
                    .firstName(rs.getString("firstName"))
                    .lastName(rs.getString("lastName"))
                    .hashedPassword(rs.getString("password"))
                    .build();

            ps.close();
            con.close();

            return Optional.of(u);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public void save(User user) {
        if (this.findByUsername(user.getUsername()).isPresent())
            throw new IntegrityConstraintViolationException("Cannot save user. Integrity constraint violation: username must be unique");

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();

            ps = con.prepareStatement("INSERT INTO User VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, user.getId().asString());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getUsername());
            ps.setString(6, user.getHashedPassword());

            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("UPDATE User SET firstName=?, lastName=?, mail=?, username=?, password=? WHERE id=?");
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getUsername());
            ps.setString(5, user.getHashedPassword());
            ps.setString(6, user.getId().asString());
            ps.executeUpdate();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void remove(UserId userId) {
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM User WHERE id=?");
            ps.setString(1, userId.asString());

            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Optional<User> findById(UserId userId) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT id, username, mail, firstName, lastName, password FROM User WHERE id=?");
            ps.setString(1, userId.asString());

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                ps.close();
                con.close();
                return Optional.empty();
            }

            User u = User.builder()
                    .id(new UserId(rs.getString("id")))
                    .username(rs.getString("username"))
                    .email(rs.getString("mail"))
                    .firstName(rs.getString("firstName"))
                    .lastName(rs.getString("lastName"))
                    .hashedPassword(rs.getString("password"))
                    .build();

            ps.close();
            con.close();

            return Optional.of(u);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Collection<User> findAll() {
        Collection<User> users = new LinkedList<>();

        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT id, username, mail, firstName, lastName, password FROM User");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                User u = User.builder()
                        .id(new UserId(rs.getString("id")))
                        .username(rs.getString("username"))
                        .email(rs.getString("mail"))
                        .firstName(rs.getString("firstName"))
                        .lastName(rs.getString("lastName"))
                        .hashedPassword(rs.getString("password"))
                        .build();
                users.add(u);
            }
            ps.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return users;
    }

    @Override
    public int getRepositorySize() {
        int size = 0;
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement( "SELECT COUNT(*) FROM User");
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

package ch.heigvd.amt.StoneOverflow.infrastructure.persistance.jdbc;

import ch.heigvd.amt.StoneOverflow.domain.user.IUserRepository;
import ch.heigvd.amt.StoneOverflow.domain.user.User;
import ch.heigvd.amt.StoneOverflow.domain.user.UserId;

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

            PreparedStatement ps = con.prepareStatement("SELECT id, username, mail, firstName, lastName, password FROM User WHERE username=?");
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return Optional.empty();

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
            //todo: log/handle error
            System.out.println(ex);
        }

        return Optional.empty();
    }

    @Override
    public void save(User user) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("INSERT INTO User VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, user.getId().asString());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getUsername());
            ps.setString(6, user.getHashedPassword());

            ps.executeUpdate();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            //todo: log/handle error
            System.out.println(ex);
        }
    }

    @Override
    public void remove(UserId userId) {

    }

    @Override
    public Optional<User> findById(UserId userId) {
        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT id, username, mail, firstName, lastName, password FROM User WHERE id=?");
            ps.setString(1, userId.asString());

            ResultSet rs = ps.executeQuery();
            if (!rs.next())
                return Optional.empty();

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
            //todo: log/handle error
            System.out.println(ex);
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
            //todo: log/handle error
        }

        return users;
    }
}

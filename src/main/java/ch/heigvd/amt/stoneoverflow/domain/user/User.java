package ch.heigvd.amt.stoneoverflow.domain.user;

import ch.heigvd.amt.stoneoverflow.domain.IEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Builder(toBuilder = true)
@Value
public class User implements IEntity<User, UserId> {
    private UserId id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    @EqualsAndHashCode.Exclude
    private String hashedPassword;

    public boolean authenticate(String plaintextPassword) {
        return BCrypt.checkpw(plaintextPassword, this.hashedPassword);
    }

    @Override
    public User deepClone() {
        return this.toBuilder()
                .id(new UserId(id.asString()))
                .build();
    }

    public static class UserBuilder {
        public UserBuilder plaintextPassword(String password) {
            if (password == null || password.isEmpty())
                throw new IllegalArgumentException("Password cannot be null or empty");

            hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            return this;
        }

        public User build() {
            if (id == null)
                id = new UserId();

            if (username == null || username.isEmpty())
                throw new IllegalArgumentException("Username cannot be null or empty");

            if (hashedPassword == null || hashedPassword.isEmpty())
                throw new IllegalArgumentException("Password cannot be null or empty");

            if (email == null || email.isEmpty())
                throw new IllegalArgumentException("Email cannot be null or empty");

            if (firstName == null || firstName.isEmpty())
                throw new IllegalArgumentException("First name cannot be null or empty");

            if (lastName == null || lastName.isEmpty())
                throw new IllegalArgumentException("Last name cannot be null or empty");

            return new User(id, username, email, firstName, lastName, hashedPassword);
        }
    }
}

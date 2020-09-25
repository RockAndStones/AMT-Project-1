package ch.heigvd.amt.StoneOverflow.business;

import ch.heigvd.amt.StoneOverflow.domain.LoginCommand;
import ch.heigvd.amt.StoneOverflow.domain.RegisterCommand;

import ch.heigvd.amt.StoneOverflow.domain.User;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.ejb.Singleton;
import java.util.HashMap;

@Singleton
public class UsersDatastore {
    // Create a HashMap object called capitalCities
    private HashMap<String, User> users = new HashMap<String, User>(){{put("test@test.com", User.builder().username("test@test.com").password(BCrypt.hashpw("test", BCrypt.gensalt())).build());}};

    public void addUser(RegisterCommand register){
        String hashedPassword = BCrypt.hashpw(register.getPassword(), BCrypt.gensalt());
        User newUser = User.builder()
                .username(register.getUsername())
                .password(hashedPassword)
                .build();
        users.put(register.getUsername(), newUser);
    }

    public boolean isValidUser(LoginCommand login){
        User user = users.get(login.getUsername());
        return user != null
                && BCrypt.checkpw(login.getPassword(), user.getPassword());
    }

    public boolean isUserIn(LoginCommand login){
        return users.containsKey(login.getUsername())
                && users.get(login.getUsername()).getPassword().equals(login.getPassword());
    }
}

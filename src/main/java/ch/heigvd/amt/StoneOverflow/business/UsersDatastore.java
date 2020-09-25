package ch.heigvd.amt.StoneOverflow.business;

import ch.heigvd.amt.StoneOverflow.domain.LoginCommand;
import ch.heigvd.amt.StoneOverflow.domain.RegisterCommand;

import ch.heigvd.amt.StoneOverflow.domain.User;

import javax.ejb.Singleton;
import java.util.HashMap;

@Singleton
public class UsersDatastore {
    // Create a HashMap object called capitalCities
    private HashMap<String, User> users = new HashMap<String, User>(){{put("test@test.com", User.builder().username("test@test.com").password("test").build());}};

    public void addUser(RegisterCommand register){
        User newUser = User.builder()
                .username(register.getUsername())
                .password(register.getPassword())
                .build();
        users.put(register.getUsername(), newUser);
    }

    public boolean isValidUser(LoginCommand login){
        return users.containsKey(login.getUsername())
                && users.get(login.getUsername()).getPassword().equals(login.getPassword());
    }

    public boolean isUserIn(LoginCommand login){
        return users.containsKey(login.getUsername())
                && users.get(login.getUsername()).getPassword().equals(login.getPassword());
    }
}

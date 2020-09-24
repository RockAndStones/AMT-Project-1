package ch.heigvd.amt.StoneOverflow.business;

import ch.heigvd.amt.StoneOverflow.model.LoginCommand;
import ch.heigvd.amt.StoneOverflow.model.RegisterCommand;

import ch.heigvd.amt.StoneOverflow.model.User;
import lombok.Builder;

import javax.ejb.Singleton;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Singleton
public class UsersDatastore {
    // Create a HashMap object called capitalCities
    private HashMap<String, User> users = new HashMap<>();

    public void addUser(RegisterCommand register){
        User newUser = User.builder()
                .username(register.getUsername())
                .password(register.getPassword())
                .session(register.getSession())
                .build();
        users.put(register.getUsername(), newUser);
    }

    public boolean isValidUser(LoginCommand login){
        return users.containsKey(login.getUsername())
                && users.get(login.getUsername()).getPassword().equals(login.getPassword());
    }

    public boolean isUserIn(LoginCommand login){
        return users.containsKey(login.getUsername())
                && users.get(login.getUsername()).getSession().equals(login.getSession());
    }
}

package ch.heigvd.amt.StoneOverflow.business;

import ch.heigvd.amt.StoneOverflow.model.LoginCommand;
import ch.heigvd.amt.StoneOverflow.model.RegisterCommand;

import ch.heigvd.amt.StoneOverflow.model.User;
import lombok.Builder;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Builder
public class UsersDatastore {
    // Create a HashMap object called capitalCities
    private static HashMap<String, User> users = new HashMap<String, User>();

    public static void addUser(RegisterCommand register){
        User newUser = User.builder()
                .username(register.getUsername())
                .password(register.getPassword())
                .session(register.getSession())
                .build();
        users.put(register.getUsername(), newUser);
    }

    public static boolean isValidUser(LoginCommand login){
        return users.containsKey(login.getUsername())
                && users.get(login.getUsername()).getPassword().equals(login.getPassword());
    }

    public static boolean isUserIn(LoginCommand login){
        return users.containsKey(login.getUsername())
                && users.get(login.getUsername()).getSession().equals(login.getSession());
    }
}

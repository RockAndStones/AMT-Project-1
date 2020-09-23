package ch.heigvd.amt.StoneOverflow.business;

import ch.heigvd.amt.StoneOverflow.model.LoginCommand;
import ch.heigvd.amt.StoneOverflow.model.RegisterCommand;

import lombok.Builder;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Builder
public class UsersDatastore {
    // Create a HashMap object called capitalCities
    private static HashMap<String, HttpSession> users = new HashMap<String, HttpSession>();

    public static void addUser(RegisterCommand register){
        users.put(register.getUsername(), register.getSession());
    }

    public static boolean isUserIn(LoginCommand login){
        return users.containsKey(login.getUsername())
                && users.containsValue(login.getSession());
    }
}

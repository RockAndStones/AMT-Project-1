package ch.heigvd.amt.StoneOverflow.presentation;

import ch.heigvd.amt.StoneOverflow.business.UsersDatastore;
import ch.heigvd.amt.StoneOverflow.model.RegisterCommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterCommandServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RegisterCommand command = RegisterCommand.builder().
                username(req.getParameter("username")).
                session(req.getSession()).build();
        UsersDatastore.addUser(command);
    }
}

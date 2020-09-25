package ch.heigvd.amt.StoneOverflow.ui.web.login;

import ch.heigvd.amt.StoneOverflow.business.UsersDatastore;
import ch.heigvd.amt.StoneOverflow.domain.RegisterCommand;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterCommandServlet", urlPatterns = "/registerCommand")
public class RegisterCommandServlet extends HttpServlet {
    @EJB
    UsersDatastore usersDatastore;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RegisterCommand command = RegisterCommand.builder().
                username(req.getParameter("username")).
                password(req.getParameter("password")).
                build();
        usersDatastore.addUser(command);
        //todo: Use shared logic for register & login
        req.getSession().setAttribute("loggedInUser", command.getUsername());
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}

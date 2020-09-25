package ch.heigvd.amt.StoneOverflow.ui.web.login;

import ch.heigvd.amt.StoneOverflow.business.UsersDatastore;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.login.LoginCommand;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginCommandServlet", urlPatterns = "/loginCommand")
public class LoginCommandServlet extends HttpServlet {
    @EJB
    UsersDatastore usersDatastore;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginCommand command = LoginCommand.builder()
                .username(req.getParameter("username"))
                .plaintextPassword(req.getParameter("password"))
                .build();
        if(usersDatastore.isValidUser(command)){
            //todo: Use shared logic for register & login
            req.getSession().setAttribute("loggedInUser", command.getUsername());
            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}

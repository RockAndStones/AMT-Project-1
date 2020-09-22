package ch.heigvd.amt.StoneOverflow.presentation.login;

import ch.heigvd.amt.StoneOverflow.business.UsersDatastore;
import ch.heigvd.amt.StoneOverflow.model.LoginCommand;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginCommandServlet", urlPatterns = "/loginCommand")
public class LoginCommandServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginCommand command = LoginCommand.builder()
                .username(req.getParameter("username"))
                .session(req.getSession())
                .build();
        if(UsersDatastore.isUserIn(command)){
            req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
        }
    }
}

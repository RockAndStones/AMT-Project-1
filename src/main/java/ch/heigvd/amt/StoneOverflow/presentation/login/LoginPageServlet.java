package ch.heigvd.amt.StoneOverflow.presentation.login;

import ch.heigvd.amt.StoneOverflow.business.UsersDatastore;
import ch.heigvd.amt.StoneOverflow.model.LoginCommand;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginPageServlet", urlPatterns = "/login")
public class LoginPageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd;
        if(req.getParameter("login") != null) {
            rd = req.getRequestDispatcher("loginCommand");
            rd.forward(req, resp);
        } else if(req.getParameter("register") != null){
            rd = req.getRequestDispatcher("registerCommand");
            rd.forward(req, resp);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}

package ch.heigvd.amt.stoneoverflow.ui.web.login;

import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegisterCommand;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginPageServlet", urlPatterns = "/login")
public class LoginPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get previous values
        RegisterCommand registerCommand = (RegisterCommand)request.getSession().getAttribute("registerCommand");
        request.getSession().removeAttribute("registerCommand");

        // Get error message
        String errorMessage = (String)request.getSession().getAttribute("errorMessage");
        request.getSession().removeAttribute("errorMessage");

        request.setAttribute("registerCommand", registerCommand);
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}

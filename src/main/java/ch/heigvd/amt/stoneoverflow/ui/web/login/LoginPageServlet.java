package ch.heigvd.amt.stoneoverflow.ui.web.login;

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
        //todo: Improve page behavior to show either login / register on error
        String errorMessage = (String)request.getSession().getAttribute("errorMessage");
        request.getSession().removeAttribute("errorMessage");

        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}

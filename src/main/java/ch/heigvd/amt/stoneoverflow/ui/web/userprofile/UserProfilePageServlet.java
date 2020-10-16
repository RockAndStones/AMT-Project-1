package ch.heigvd.amt.stoneoverflow.ui.web.userprofile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserProfilePage", urlPatterns = "/profile")
public class UserProfilePageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String errorMessage = (String)request.getSession().getAttribute("errorMessage");
        request.getSession().removeAttribute("errorMessage");

        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }
}

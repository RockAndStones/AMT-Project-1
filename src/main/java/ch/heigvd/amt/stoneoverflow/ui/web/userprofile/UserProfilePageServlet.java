package ch.heigvd.amt.stoneoverflow.ui.web.userprofile;

import ch.heigvd.amt.gamification.api.dto.UserInfo;
import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserProfilePage", urlPatterns = "/profile")
public class UserProfilePageServlet extends HttpServlet {
    @Inject
    ServiceRegistry serviceRegistry;
    private GamificationFacade gamificationFacade;

    @Override
    public void init() throws ServletException {
        gamificationFacade = serviceRegistry.getGamificationFacade();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AuthenticatedUserDTO user = (AuthenticatedUserDTO) request.getSession().getAttribute("authenticatedUser");
        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        request.getSession().removeAttribute("errorMessage");

        String message = (String)request.getSession().getAttribute("message");
        request.getSession().removeAttribute("message");

        UserInfo userInfo = gamificationFacade.getUserInfo(user.getId().asString());
        if (userInfo == null) {
            userInfo = new UserInfo();
            request.setAttribute("isGamificationOn", false);
        } else request.setAttribute("isGamificationOn", true);
        request.setAttribute("userGameInfo", userInfo);

        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("message", message);
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }
}

package ch.heigvd.amt.StoneOverflow.ui.web.login;

import ch.heigvd.amt.StoneOverflow.application.ServiceRegistry;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.login.LoginCommand;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginCommandServlet", urlPatterns = "/loginCommand")
public class LoginCommandServlet extends HttpServlet {
    private IdentityManagementFacade identityManagementFacade = ServiceRegistry.getServiceRegistry().getIdentityManagementFacade();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginCommand loginCommand = LoginCommand.builder()
                .username(req.getParameter("username"))
                .plaintextPassword(req.getParameter("password"))
                .build();

        try {
            AuthenticatedUserDTO user = identityManagementFacade.login(loginCommand);
            req.getSession().setAttribute("authenticatedUser", user);

            //Read target servlet
            String targetServlet = (String)req.getSession().getAttribute("targetReq");
            req.getSession().removeAttribute("targetReq");

            String target = targetServlet == null ? "/home" : targetServlet;
            resp.sendRedirect(req.getContextPath() + target);
        } catch (LoginFailedException e) {
            req.getSession().setAttribute("errorMessage", "Invalid username / password");
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}

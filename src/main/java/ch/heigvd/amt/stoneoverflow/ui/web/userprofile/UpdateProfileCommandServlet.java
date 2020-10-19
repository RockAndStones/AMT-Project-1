package ch.heigvd.amt.stoneoverflow.ui.web.userprofile;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.updateprofile.UpdateProfileCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.updateprofile.UpdateProfileFailedException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateCommandServlet", urlPatterns = "/updateUser.do")
public class UpdateProfileCommandServlet extends HttpServlet {
    @Inject
    ServiceRegistry serviceRegistry;
    IdentityManagementFacade identityManagementFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        identityManagementFacade = serviceRegistry.getIdentityManagementFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AuthenticatedUserDTO user = (AuthenticatedUserDTO) req.getSession().getAttribute("authenticatedUser");
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .oldUser(user)
                .username(req.getParameter("username"))
                .email(req.getParameter("email"))
                .firstName(req.getParameter("firstName"))
                .lastName(req.getParameter("lastName"))
                .plaintextPassword(req.getParameter("password"))
                .plaintextPasswordConfirmation(req.getParameter("confirmPassword"))
                .build();

        try {
            AuthenticatedUserDTO updatedUser = identityManagementFacade.update(updateProfileCommand);
            req.getSession().setAttribute("authenticatedUser", updatedUser);
            req.getSession().setAttribute("message", "User information updated");
            resp.sendRedirect(req.getContextPath() + "/profile");
        } catch (UpdateProfileFailedException e) {
            req.getSession().setAttribute("errorMessage", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/profile");
        }
    }
}

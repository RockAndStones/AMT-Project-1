package ch.heigvd.amt.StoneOverflow.ui.web.login;

import ch.heigvd.amt.StoneOverflow.application.ServiceRegistry;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.register.RegistrationFailedException;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.register.RegisterCommand;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterCommandServlet", urlPatterns = "/registerCommand")
public class RegisterCommandServlet extends HttpServlet {
    @Inject
    ServiceRegistry serviceRegistry;
    IdentityManagementFacade identityManagementFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        identityManagementFacade = serviceRegistry.getIdentityManagementFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //todo: Add email, first name, last name to register form
        RegisterCommand registerCommand = RegisterCommand.builder()
                .username(req.getParameter("username"))
                .email("Email placeholder " + Math.random())
                .firstName("First name placeholder")
                .lastName("Last name placeholder")
                .plaintextPassword(req.getParameter("password"))
                .build();

        try {
            identityManagementFacade.register(registerCommand);
            //Forward request to login command. !! Only possible because username and password field name match !!
            req.getRequestDispatcher("/loginCommand").forward(req, resp);
        } catch (RegistrationFailedException e) {
            e.printStackTrace();
        }
    }
}

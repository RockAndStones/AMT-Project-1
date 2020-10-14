package ch.heigvd.amt.stoneoverflow.ui.web.question;

import ch.heigvd.amt.stoneoverflow.application.question.AddQuestionCommand;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SubmitQuestionCommandServlet", urlPatterns = "/submitQuestion.do")
public class AddQuestionCommandServlet extends HttpServlet {
    @Inject
    ServiceRegistry serviceRegistry;
    QuestionFacade questionFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        questionFacade = serviceRegistry.getQuestionFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthenticatedUserDTO user = (AuthenticatedUserDTO)req.getSession().getAttribute("authenticatedUser");
        AddQuestionCommand command = AddQuestionCommand.builder()
                .creatorId(user.getId())
                .creator(user.getUsername())
                .title(req.getParameter("title"))
                .description(req.getParameter("description")).build();
        questionFacade.addQuestion(command);
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}

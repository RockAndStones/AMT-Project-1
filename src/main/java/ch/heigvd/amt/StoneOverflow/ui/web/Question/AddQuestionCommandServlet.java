package ch.heigvd.amt.StoneOverflow.ui.web.Question;

import ch.heigvd.amt.StoneOverflow.application.Question.AddQuestionCommand;
import ch.heigvd.amt.StoneOverflow.application.Question.QuestionFacade;
import ch.heigvd.amt.StoneOverflow.application.ServiceRegistry;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.login.AuthenticatedUserDTO;

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
        //todo: verify getSession ?
        AuthenticatedUserDTO user = (AuthenticatedUserDTO)req.getSession().getAttribute("authenticatedUser");
        AddQuestionCommand command = AddQuestionCommand.builder()
                .creator(user.getUsername())
                .title(req.getParameter("title"))
                .description(req.getParameter("description")).build();
        questionFacade.addQuestion(command);
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}

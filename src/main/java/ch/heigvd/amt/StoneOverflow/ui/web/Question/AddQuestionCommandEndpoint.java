package ch.heigvd.amt.StoneOverflow.ui.web.Question;

import ch.heigvd.amt.StoneOverflow.application.Question.AddQuestionCommand;
import ch.heigvd.amt.StoneOverflow.application.Question.QuestionFacade;
import ch.heigvd.amt.StoneOverflow.application.ServiceRegistry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SubmitQuestionCommandHandler", urlPatterns = "/submitQuestion.do")
public class AddQuestionCommandEndpoint extends HttpServlet {
    private ServiceRegistry serviceRegistry = ServiceRegistry.getServiceRegistry();
    private QuestionFacade questionFacade = serviceRegistry.getQuestionFacade();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AddQuestionCommand command = AddQuestionCommand.builder()
                .creator("anonymous")//Later req.getParameter("username")
                .title(req.getParameter("title"))
                .description(req.getParameter("description")).build();
        questionFacade.addQuestion(command);
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}

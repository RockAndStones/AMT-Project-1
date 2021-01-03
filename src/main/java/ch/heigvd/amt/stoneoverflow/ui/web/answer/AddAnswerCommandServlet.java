package ch.heigvd.amt.stoneoverflow.ui.web.answer;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.answer.AddAnswerCommand;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SubmitAnswerCommandServlet", urlPatterns = "/submitAnswer.do")
public class AddAnswerCommandServlet extends HttpServlet {
    @Inject
    ServiceRegistry serviceRegistry;
    AnswerFacade answerFacade;
    GamificationFacade gamificationFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        answerFacade = serviceRegistry.getAnswerFacade();
        gamificationFacade = serviceRegistry.getGamificationFacade();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthenticatedUserDTO user = (AuthenticatedUserDTO)req.getSession().getAttribute("authenticatedUser");
        String questionUUID = req.getParameter("questionUUID");

        AddAnswerCommand command = AddAnswerCommand.builder()
                .answerTo(new QuestionId(questionUUID))
                .creatorId(user.getId())
                .creator(user.getUsername())
                .description(req.getParameter("description")).build();
        answerFacade.addAnswer(command);

        gamificationFacade.addReplyAsync(user.getId().asString(), null);
        gamificationFacade.stonerProgressAsync(user.getId().asString(), null);

        resp.sendRedirect(req.getContextPath() + "/questionDetails?questionUUID=" + questionUUID);
    }
}

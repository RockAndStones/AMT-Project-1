package ch.heigvd.amt.stoneoverflow.ui.web.comment;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.comment.AddCommentCommand;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SubmitCommentCommandServlet", urlPatterns = "/submitComment.do")
public class AddCommentCommandServlet extends HttpServlet {
    @Inject
    ServiceRegistry serviceRegistry;
    CommentFacade commentFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        commentFacade = serviceRegistry.getCommentFacade();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthenticatedUserDTO user = (AuthenticatedUserDTO)req.getSession().getAttribute("authenticatedUser");

        String questionUUID = req.getParameter("questionUUID");
        Id targetId = null;

        if(req.getParameter("targetType").equals("answer")){
            targetId = new AnswerId(req.getParameter("targetUUID"));
        } else {
            targetId = new QuestionId(questionUUID);
        }

        AddCommentCommand command = AddCommentCommand.builder()
                .commentTo(targetId)
                .creatorId(user.getId())
                .creator(user.getUsername())
                .content(req.getParameter("commentContent")).build();
        commentFacade.addComment(command);

        resp.sendRedirect(req.getContextPath() + "/questionDetails?questionUUID=" + questionUUID);
    }
}

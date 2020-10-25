package ch.heigvd.amt.stoneoverflow.ui.web.vote;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.vote.AddVoteCommand;
import ch.heigvd.amt.stoneoverflow.application.vote.UpdateVoteCommand;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteDTO;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteFacade;
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import ch.heigvd.amt.stoneoverflow.domain.vote.VoteId;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "VoteCommandServlet", urlPatterns = "/vote.do")
public class VoteCommandServlet extends HttpServlet {
    @Inject
    ServiceRegistry serviceRegistry;
    VoteFacade voteFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        voteFacade = serviceRegistry.getVoteFacade();
    }

    private void redirectToQuestionDetails(HttpServletRequest req, HttpServletResponse resp, String questionUUID) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/questionDetails?voted=y&questionUUID=" + questionUUID);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AuthenticatedUserDTO user = (AuthenticatedUserDTO)req.getSession().getAttribute("authenticatedUser");
        String questionUUID   = req.getParameter("questionUUID");
        String voteUUID       = req.getParameter("voteUUID");
        String voteTypeString = req.getParameter("voteType");
        Vote.VoteType voteType = null;

        // Cast voteType to enum value and redirect to questionDetails if it cannot be cast
        switch (voteTypeString) {
            case "UP":
                voteType = Vote.VoteType.UP;
                break;
            case "DOWN":
                voteType = Vote.VoteType.DOWN;
                break;
            default:
                redirectToQuestionDetails(req, resp, questionUUID);
                break;
        }

        // Get target ID and try to get the vote from the repo
        VoteDTO vote = null;
        Id targetId;
        UserMessageType userMessageType;
        if (req.getParameter("targetType").equals("answer")) {
            targetId = new AnswerId(req.getParameter("targetUUID"));
            userMessageType = UserMessageType.QUESTION;
        } else {
            targetId = new QuestionId(questionUUID);
            userMessageType = UserMessageType.ANSWER;
        }
        if (!voteUUID.isEmpty())
            vote = voteFacade.getVote(new VoteId(voteUUID), userMessageType);

        if (vote == null) {
            // If no vote found from repo, create a new vote
            AddVoteCommand command = AddVoteCommand.builder()
                    .votedBy(user.getId())
                    .votedObject(targetId)
                    .voteType(voteType).build();
            voteFacade.addVote(command);
        } else {
            // Otherwise
            // Check ownership of the vote
            if (!user.getId().asString().equals(vote.getVotedBy()))
                redirectToQuestionDetails(req, resp, questionUUID);

            // Update the vote
            if (voteType == vote.getVoteType()) {
                // If voteType are identical, remove vote from repository
                voteFacade.remove(new VoteId(vote.getUuid()));
            } else {
                // Otherwise change the voteType
                voteFacade.changeVote(UpdateVoteCommand.builder()
                        .id(new VoteId(voteUUID))
                        .votedBy(user.getId())
                        .votedObject(targetId)
                        .voteType(voteType).build());
            }
        }

        redirectToQuestionDetails(req, resp, questionUUID);
    }
}

package ch.heigvd.amt.stoneoverflow.ui.web.vote;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
import ch.heigvd.amt.stoneoverflow.application.vote.AddVoteCommand;
import ch.heigvd.amt.stoneoverflow.application.vote.UpdateVoteCommand;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteDTO;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteFacade;
import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
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
    QuestionFacade questionFacade;
    AnswerFacade answerFacade;
    VoteFacade voteFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        questionFacade = serviceRegistry.getQuestionFacade();
        answerFacade = serviceRegistry.getAnswerFacade();
        voteFacade = serviceRegistry.getVoteFacade();
    }

    private void redirectToQuestionDetails(HttpServletRequest req, HttpServletResponse resp, String questionUUID) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/questionDetails?voted=y&questionUUID=" + questionUUID);
    }

    private synchronized void applyVote(HttpServletRequest req, HttpServletResponse resp, Id targetId, UserId userId, Vote.VoteType voteType, UserMessageType userMessageType, String questionUUID) throws IOException {
        VoteDTO vote = voteFacade.getVote(targetId, userId, userMessageType);
        if (vote == null) {
            // If no vote found from repo, create a new vote
            AddVoteCommand command = AddVoteCommand.builder()
                    .votedBy(userId)
                    .votedObject(targetId)
                    .voteType(voteType).build();
            voteFacade.addVote(command);
        } else {
            // Otherwise
            // Check ownership of the vote
            if (!userId.asString().equals(vote.getVotedBy()))
                redirectToQuestionDetails(req, resp, questionUUID);

            // Update the vote
            if (voteType == vote.getVoteType()) {
                // If voteType are identical, remove vote from repository
                voteFacade.remove(new VoteId(vote.getUuid()));
            } else {
                // Otherwise change the voteType
                voteFacade.changeVote(UpdateVoteCommand.builder()
                        .id(new VoteId(vote.getUuid()))
                        .votedBy(userId)
                        .votedObject(targetId)
                        .voteType(voteType).build());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AuthenticatedUserDTO user = (AuthenticatedUserDTO)req.getSession().getAttribute("authenticatedUser");
        String questionUUID   = req.getParameter("questionUUID");
        String voteTypeString = req.getParameter("voteType");

        // Check if targetID is valid
        try {
            QuestionId id = new QuestionId(req.getParameter("targetUUID"));
        } catch (IllegalArgumentException ex) {
            redirectToQuestionDetails(req, resp, questionUUID);
        }

        // Cast voteType to enum value and redirect to questionDetails if it cannot be cast
        Vote.VoteType voteType = null;
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

        // Identify target and redirect if cannot be identified
        Id targetId;
        UserMessageType userMessageType;
        if (req.getParameter("targetType").equals("answer")) {
            targetId = new AnswerId(req.getParameter("targetUUID"));
            userMessageType = UserMessageType.QUESTION;
            // Check if targetUUID is valid
            if (answerFacade.getAnswer((AnswerId) targetId) == null)
                redirectToQuestionDetails(req, resp, questionUUID);
        } else {
            targetId = new QuestionId(req.getParameter("targetUUID"));
            userMessageType = UserMessageType.ANSWER;
            // Check if targetUUID is valid
            if (questionFacade.getQuestion((QuestionId) targetId) == null)
                redirectToQuestionDetails(req, resp, questionUUID);
        }

        // Apply the vote
        applyVote(req, resp, targetId, user.getId(), voteType, userMessageType, questionUUID);

        redirectToQuestionDetails(req, resp, questionUUID);
    }
}

package ch.heigvd.amt.stoneoverflow.ui.web.question;

import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuerySortBy;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuery;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuerySortBy;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.pagination.PaginationDTO;
import ch.heigvd.amt.stoneoverflow.application.pagination.PaginationFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswersDTO;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentFacade;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteFacade;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "questionDetailsServlet", urlPatterns = "/questionDetails")
public class QuestionDetailsPageServlet extends HttpServlet {
    @Inject
    ServiceRegistry serviceRegistry;

    private QuestionFacade   questionFacade;
    private AnswerFacade     answerFacade;
    private CommentFacade    commentFacade;
    private VoteFacade       voteFacade;
    private PaginationFacade paginationFacade;


    @Override
    public void init() throws ServletException {
        super.init();
        questionFacade   = serviceRegistry.getQuestionFacade();
        answerFacade     = serviceRegistry.getAnswerFacade();
        commentFacade    = serviceRegistry.getCommentFacade();
        voteFacade       = serviceRegistry.getVoteFacade();
        paginationFacade = serviceRegistry.getPaginationFacade();

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AuthenticatedUserDTO user = (AuthenticatedUserDTO) req.getSession().getAttribute("authenticatedUser");

        // Get param questionUUID and check if he is set
        String questionUUIDAsString = req.getParameter("questionUUID");
        if (questionUUIDAsString == null)
            resp.sendRedirect(req.getContextPath() + "/");

        // Set a QuestionId of requested question
        QuestionId questionId;
        try {
            questionId = new QuestionId(questionUUIDAsString);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // Get QuestionDTO from repository
        QuestionsDTO.QuestionDTO questionDTO = questionFacade.getQuestion(questionId);
        if (questionDTO == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        // Set number of answers of the question
        questionDTO.setNbAnswers(answerFacade.getNumberOfAnswers(questionId));

        // Set the pagination of current question
        PaginationDTO paginationDTO = paginationFacade.settingAnswerPagination(req.getParameter("page"), questionDTO.getNbAnswers());

        // Get answers & comments of the question
        questionDTO.setNbVotes(voteFacade.getNumberOfVotes(questionId));
        if (user != null)
            questionDTO.setVoteDTO(voteFacade.getVote(questionId, user.getId(), UserMessageType.QUESTION));
        questionDTO.setAnswers(answerFacade.getAnswers(AnswerQuery.builder()
                .answerTo(questionId)
                .sortBy(AnswerQuerySortBy.VOTES).build(), paginationDTO.getStartItem(), paginationDTO.getLimit()).getAnswers());
        questionDTO.setComments(commentFacade.getComments(CommentQuery.builder()
                .commentTo(questionId)
                .sortBy(CommentQuerySortBy.DATE).build()).getComments());

        // Get comments of each answers
        for (AnswersDTO.AnswerDTO answer : questionDTO.getAnswers()) {
            AnswerId answerId = new AnswerId(answer.getUuid());
            answer.setNbVotes(voteFacade.getNumberOfVotes(answerId));
            if (user != null)
                answer.setVoteDTO(voteFacade.getVote(answerId, user.getId(), UserMessageType.ANSWER));
            answer.setComments(commentFacade.getComments(CommentQuery.builder()
                    .commentTo(new AnswerId(answer.getUuid()))
                    .userMessageType(UserMessageType.ANSWER)
                    .sortBy(CommentQuerySortBy.DATE).build()).getComments());
        }

        req.setAttribute("question", questionDTO);
        req.setAttribute("pagination", paginationDTO);
        req.getRequestDispatcher("WEB-INF/views/questionDetails.jsp").forward(req, resp);
    }
}

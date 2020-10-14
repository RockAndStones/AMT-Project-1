package ch.heigvd.amt.stoneoverflow.ui.web.question;

import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswersDTO;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentFacade;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;

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

    private QuestionFacade questionFacade;
    private AnswerFacade   answerFacade;
    private CommentFacade  commentFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        questionFacade = serviceRegistry.getQuestionFacade();
        answerFacade   = serviceRegistry.getAnswerFacade();
        commentFacade  = serviceRegistry.getCommentFacade();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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

        // Get answers & comments of the question
        questionDTO.setAnswers(answerFacade.getAnswersFromQuestion(AnswerQuery.builder()
                .answerTo(questionId)
                .byDate(true).build()).getAnswers());
        questionDTO.setComments(commentFacade.getComments(CommentQuery.builder()
                .commentTo(questionId)
                .byDate(true).build()).getComments());

        // Get comments of each answers
        for (AnswersDTO.AnswerDTO answer : questionDTO.getAnswers()) {
            answer.setComments(commentFacade.getComments(CommentQuery.builder()
                    .commentTo(new QuestionId(answer.getUuid()))
                    .byDate(true).build()).getComments());

        }

        req.setAttribute("question", questionDTO);
        req.getRequestDispatcher("WEB-INF/views/questionDetails.jsp").forward(req, resp);
    }
}

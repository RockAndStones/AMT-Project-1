package ch.heigvd.amt.stoneoverflow.ui.web.question;

import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswersDTO;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentFacade;
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

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String questionUUID = req.getParameter("questionUUID");

        if (questionUUID == null)
            resp.sendRedirect(req.getContextPath() + "/");

        // Get question from repository
        QuestionsDTO.QuestionDTO questionDTO = questionFacade.getQuestion(new QuestionId(questionUUID));
        // Get answers & comments of the question
        AnswerQuery answerQuery = AnswerQuery.builder()
                .answerTo(new QuestionId(questionDTO.getUuid()))
                .byDate(true).build();
        questionDTO.setAnswers(answerFacade.getAnswersFromQuestion(answerQuery).getAnswers());
        questionDTO.setComments(commentFacade.getCommentsFromQuestion(new QuestionId(questionDTO.getUuid())).getComments());

        // Get comments of each answers
        for (AnswersDTO.AnswerDTO answer : questionDTO.getAnswers()) {
            answer.setComments(commentFacade.getCommentsFromAnswer(new AnswerId(answer.getUuid())).getComments());

        }

        req.setAttribute("question", questionDTO);
        req.getRequestDispatcher("WEB-INF/views/questionDetails.jsp").forward(req, resp);
    }
}

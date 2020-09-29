package ch.heigvd.amt.StoneOverflow.ui.web.Question;

import ch.heigvd.amt.StoneOverflow.application.Question.QuestionQuery;
import ch.heigvd.amt.StoneOverflow.application.Question.QuestionsDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "addQuestionHandler", urlPatterns = "/newQuestion")
public class AddQuestionPageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/views/addQuestion.jsp").forward(req, resp);
    }
}

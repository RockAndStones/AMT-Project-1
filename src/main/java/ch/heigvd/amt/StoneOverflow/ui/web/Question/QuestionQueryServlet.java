package ch.heigvd.amt.StoneOverflow.ui.web.Question;

import ch.heigvd.amt.StoneOverflow.application.Question.QuestionFacade;
import ch.heigvd.amt.StoneOverflow.application.Question.QuestionQuery;
import ch.heigvd.amt.StoneOverflow.application.Question.QuestionsDTO;
import ch.heigvd.amt.StoneOverflow.application.ServiceRegistry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="QuestionsPageServlet", urlPatterns =  {"/","/home"})
public class QuestionQueryServlet extends HttpServlet {
    private ServiceRegistry serviceRegistry;
    private QuestionFacade questionFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        serviceRegistry = ServiceRegistry.getServiceRegistry();
        questionFacade  = serviceRegistry.getQuestionFacade();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuestionsDTO questionsDTO = questionFacade.getQuestions(QuestionQuery.builder()
                .sqlSearch(false)
                .build());
        req.setAttribute("questions", questionsDTO);
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
    }
}

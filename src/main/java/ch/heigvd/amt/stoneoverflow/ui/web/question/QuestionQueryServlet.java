package ch.heigvd.amt.stoneoverflow.ui.web.question;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="QuestionsPageServlet", urlPatterns =  {"", "/home"})
public class QuestionQueryServlet extends HttpServlet {
    @Inject
    ServiceRegistry serviceRegistry;

    private QuestionFacade questionFacade;

    @Override
    public void init() throws ServletException {
        super.init();
        questionFacade = serviceRegistry.getQuestionFacade();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuestionsDTO questionsDTO = questionFacade.getQuestions(QuestionQuery.builder()
                .byNbVotes(true)
                .build());
        req.setAttribute("questions", questionsDTO);
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
    }
}
package ch.heigvd.amt.stoneoverflow.ui.web.question;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuerySortBy;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
        if (req.getParameter("pageSize") != null){
            req.getSession().removeAttribute("records-limits");

            req.setAttribute("records-limits", req.getParameter("pageSize"));
        }

        int limit = req.getParameter("records-limits") != null ? Integer.parseInt(req.getParameter("records-limits")) : 10;
        int page = req.getParameter("page") != null ? Integer.parseInt(req.getParameter("page")) : 1;
        int startQuestion = (page - 1) * limit;
        int size = questionFacade.getNumberOfQuestions();
        int totalPages = (int) Math.ceil(size / limit);

        QuestionQuery query = QuestionQuery.builder()
                .sortBy(QuestionQuerySortBy.VOTES)
                .build();

        String searchQuery = req.getParameter("s");
        if (searchQuery != null)
            query.setSearchCondition(searchQuery);

        QuestionsDTO questionsDTO = questionFacade.getQuestions(query, startQuestion, limit);
        req.setAttribute("questions", questionsDTO);
        req.setAttribute("nbQuestions", size);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("startQuestion", startQuestion);
        req.setAttribute("lastQuestion", startQuestion + limit);
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
    }
}

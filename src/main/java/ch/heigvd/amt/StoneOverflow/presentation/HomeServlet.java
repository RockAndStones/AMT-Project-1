package ch.heigvd.amt.StoneOverflow.presentation;

import ch.heigvd.amt.StoneOverflow.model.Question;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = "/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Question> questions = new ArrayList<>();
        questions.add(Question.builder().title("Title")
                                        .description("My test")
                                        .creator("Toto")
                                        .nbVotes(2)
                                        .build());
        req.setAttribute("questions", questions);
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);

    }
}

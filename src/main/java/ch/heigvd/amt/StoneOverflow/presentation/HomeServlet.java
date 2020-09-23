package ch.heigvd.amt.StoneOverflow.presentation;

import ch.heigvd.amt.StoneOverflow.model.Question;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = "/home")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Question> questions = new ArrayList<>();

        questions.add(Question.builder().title("Is it real life ??")
                                        .description("Well, you real ????")
                                        .creator("SwagMan McSwagenstein")
                                        .nbVotes(2)
                                        .build());

        questions.add(Question.builder().title("Do you even lift bro ?!")
                .description("Start lifting weights today, lift women tomorrow !")
                .creator("Ricardo")
                .nbVotes(1038)
                .build());

        questions.add(Question.builder().title("How can we pass parameters to an already built Vue JS app ?")
                .description("We have a Vue app that connects to a Web Service and get's some data. The web service URL is different, depending on the location we install the app on.\n" +
                        "\n" +
                        "I first thought of using .env files, but later I realized these files get injected into the minified .js files.\n" +
                        "\n" +
                        "Having this in my main.js was very convenient in the case of .env files:\n" +
                        "\n" +
                        "Vue.prototype.ApiBaseUrl = process.env.VUE_APP_APIBASEURL\n" +
                        "Vue.prototype.PrintDocsFolder = process.env.VUE_APP_PRINTDOCSFOLDER\n" +
                        "Vue.prototype.TicketPrintWSocket = process.env.VUE_APP_TICKETPRINTWSOCKET   \n" +
                        "\n" +
                        "The app is already built. I don't want to build the app for each of the hundred locations we have to deploy to. I'm not sure about the \"official\" approach for this.\n" +
                        "\n" +
                        "Is there any out of the box solution in Vue that can allow this configuration? Basically we need to have a file in the root folder of the built app, and read values for our Vue.prototype.VARIABLES.\n" +
                        "\n" +
                        "We are using vue-cli 3.")
                .creator("Jack Casas")
                .nbVotes(6)
                .build());

        req.setAttribute("questions", questions);
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);

    }
}

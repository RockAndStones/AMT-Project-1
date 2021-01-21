package ch.heigvd.amt.stoneoverflow.ui.web.history;


import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.history.HistoryFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@WebServlet(name = "HistoryPageServlet", urlPatterns =  "history")
public class HistoryPageServlet extends HttpServlet {
    @Inject
    private ServiceRegistry serviceRegistry;
    private HistoryFacade historyFacade;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        historyFacade = serviceRegistry.getHistoryFacade();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthenticatedUserDTO user = (AuthenticatedUserDTO) req.getSession().getAttribute("authenticatedUser");
        Collection<Map<Object, Object>> historyUser = historyFacade.getHistoryUser(user.getId());

        req.setAttribute("history", new Gson().toJson(historyUser));

        req.getRequestDispatcher("/WEB-INF/views/history.jsp").forward(req, resp);
    }
}

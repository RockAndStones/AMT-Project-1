package ch.heigvd.amt.stoneoverflow.ui.web.statistics;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.statistics.StatisticsDTO;
import ch.heigvd.amt.stoneoverflow.application.statistics.StatisticsFacade;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "StatisticsPageServlet", urlPatterns =  "statistics")
public class StatisticsPageServlet extends HttpServlet {
    @Inject
    private ServiceRegistry serviceRegistry;
    private StatisticsFacade statisticsFacade;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        statisticsFacade = serviceRegistry.getStatisticsFacade();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StatisticsDTO statistics = statisticsFacade.getGlobalStatistics();

        req.setAttribute("statistics", statistics);
        req.getRequestDispatcher("/WEB-INF/views/statistics.jsp").forward(req, resp);
    }
}

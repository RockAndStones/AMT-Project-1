package ch.heigvd.amt.stoneoverflow.ui.web.statistics;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.stoneoverflow.application.statistics.BadgesRankingsDTO;
import ch.heigvd.amt.stoneoverflow.application.statistics.PointsRankingsDTO;
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
    private GamificationFacade gamificationFacade;
    private IdentityManagementFacade identityManagementFacade;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        statisticsFacade = serviceRegistry.getStatisticsFacade();
        gamificationFacade = serviceRegistry.getGamificationFacade();
        identityManagementFacade = serviceRegistry.getIdentityManagementFacade();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StatisticsDTO statistics = statisticsFacade.getGlobalStatistics();

        int pointsPage = 0;
        try {
            pointsPage = Integer.parseInt(req.getParameter("pointsPage")) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid points page argument");
        }

        int badgesPage = 0;
        try {
            badgesPage = Integer.parseInt(req.getParameter("badgesPage")) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid badge page argument");
        }

        PointsRankingsDTO pointsRankings = statisticsFacade.getPointsRankings(pointsPage, 10);
        BadgesRankingsDTO badgesRankings = statisticsFacade.getBadgesRankings(badgesPage, 10);

        req.setAttribute("isGamificationOn", badgesRankings != null && pointsRankings != null);
        req.setAttribute("badgesRank", badgesRankings);
        req.setAttribute("pointsRank", pointsRankings);
        req.setAttribute("pagination", badgesRankings.getPagination());

        req.setAttribute("statistics", statistics);
        req.getRequestDispatcher("/WEB-INF/views/statistics.jsp").forward(req, resp);
    }
}

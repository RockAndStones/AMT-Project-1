package ch.heigvd.amt.stoneoverflow.ui.web.statistics;

import ch.heigvd.amt.gamification.api.dto.BadgesRanking;
import ch.heigvd.amt.gamification.api.dto.PaginatedBadgesRankings;
import ch.heigvd.amt.gamification.api.dto.PaginatedPointsRankings;
import ch.heigvd.amt.gamification.api.dto.PointsRanking;
import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.stoneoverflow.application.statistics.StatisticsDTO;
import ch.heigvd.amt.stoneoverflow.application.statistics.StatisticsFacade;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

import static ch.heigvd.amt.stoneoverflow.application.gamification.EventType.NEW_QUESTION;

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

        PaginatedBadgesRankings badgesRankings = gamificationFacade.getBadgesRankings(0, 10);
        PaginatedPointsRankings pointsRankings = gamificationFacade.getPointsRankings(0, 10);
        PaginatedPointsRankings pointsQuestionsRankings = gamificationFacade.getPointsRankings(NEW_QUESTION, 0, 10);

        HashMap<String, Integer> badgesRank  = new HashMap<>();
        HashMap<String, Double>  pointsRank  = new HashMap<>();
        HashMap<String, Double>  pointsQRank = new HashMap<>();

        getUsernameFromId(badgesRankings, badgesRank);
        getUsernameFromId(pointsRankings, pointsRank);
        getUsernameFromId(pointsQuestionsRankings, pointsQRank);

        req.setAttribute("isGamificationOn", badgesRankings != null && pointsRankings != null);
        req.setAttribute("badgesRank", badgesRank);
        req.setAttribute("pointsRank", pointsRank);
        req.setAttribute("pointsQRank", pointsQRank);

        req.setAttribute("statistics", statistics);
        req.getRequestDispatcher("/WEB-INF/views/statistics.jsp").forward(req, resp);
    }

    private void getUsernameFromId(PaginatedBadgesRankings badgesRankings, HashMap<String, Integer> badgesRank) {
        String username;
        if (badgesRankings != null && badgesRankings.getData() != null) {
            for (BadgesRanking rank : badgesRankings.getData()) {
                username = identityManagementFacade.getUsername(new UserId(rank.getUserId()));
                if (username != null) {
                    badgesRank.put(username, rank.getBadges());
                }
            }
        }
    }

    private void getUsernameFromId(PaginatedPointsRankings pointsRankings, HashMap<String, Double> pointsRank) {
        String username;
        if (pointsRankings != null && pointsRankings.getData() != null) {
            for (PointsRanking rank : pointsRankings.getData()) {
                username = identityManagementFacade.getUsername(new UserId(rank.getUserId()));
                if (username != null) {
                    pointsRank.put(username, rank.getPoints());
                }
            }
        }
    }
}

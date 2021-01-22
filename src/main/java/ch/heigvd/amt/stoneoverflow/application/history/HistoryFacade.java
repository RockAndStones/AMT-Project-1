package ch.heigvd.amt.stoneoverflow.application.history;

import ch.heigvd.amt.gamification.api.dto.PointsProgression;
import ch.heigvd.amt.gamification.api.dto.PointsProgressionData;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;

import java.util.*;

public class HistoryFacade {

    private final GamificationFacade gamificationFacade;

    public HistoryFacade(GamificationFacade gamificationFacade) {
        this.gamificationFacade = gamificationFacade;
    }

    /**
     * Will get the complete history for one user
     * @param userId the id of the user
     * @return a Collection of map containing the object on the y axis (points) and the object that will be the label
     */
    public Collection<Map<Object,Object>> getHistoryUser(UserId userId){
        return prepareGraph(gamificationFacade.getUserHistory(userId.asString()));
    }

    /**
     * Will get the complete history for one user
     * @param userId the id of the user
     * @return a Collection of map containing the object on the y axis (points) and the object that will be the label
     */
    public Collection<Map<Object,Object>> getHistoryUserPointScale(UserId userId, int pointScaleId){
        return prepareGraph(gamificationFacade.getUserHistoryByPointScale(userId.asString(), pointScaleId));
    }

    private Collection<Map<Object,Object>> prepareGraph(PointsProgression pointsProgression){
        // Adapted source : https://canvasjs.com/jsp-charts/line-chart/
        Map<Object,Object> map;
        List<Map<Object,Object>> list = new ArrayList<>();
        if(pointsProgression != null && pointsProgression.getData() != null) {
            int total = 0;
            for (PointsProgressionData pointsProgressionData : pointsProgression.getData()) {
                map = new HashMap<>();
                map.put("label", new DateDTO(Date.from(pointsProgressionData.getTimestamp().toInstant())).dateFormatted());
                total += pointsProgressionData.getPoints().intValue();
                map.put("y", total);
                list.add(map);
            }
        }
        return list;
    }
}

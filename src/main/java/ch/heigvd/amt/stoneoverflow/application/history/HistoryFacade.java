package ch.heigvd.amt.stoneoverflow.application.history;

import ch.heigvd.amt.gamification.api.dto.PointsProgression;
import ch.heigvd.amt.gamification.api.dto.PointsProgressionData;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteFacade;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.IUserRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.domain.vote.IVoteRepository;

import java.util.*;

public class HistoryFacade {

    private final GamificationFacade gamificationFacade;

    public HistoryFacade(GamificationFacade gamificationFacade) {
        this.gamificationFacade = gamificationFacade;
    }


    public Collection<Map<Object,Object>> getHistoryUser(UserId userId){
        Map<Object,Object> map;
        List<Map<Object,Object>> list = new ArrayList<>();
        PointsProgression pointsProgression = gamificationFacade.getUserHistory(userId.asString());
        if(pointsProgression.getData() != null) {
            for (PointsProgressionData pointsProgressionData : pointsProgression.getData()) {
                map = new HashMap<>();
                map.put("label", new DateDTO(Date.from(pointsProgressionData.getTimestamp().toInstant())).dateFormatted());
                map.put("y", pointsProgressionData.getPoints().intValue());
                list.add(map);
            }
        }
        return list;
    }
}

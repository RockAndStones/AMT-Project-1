package ch.heigvd.amt.stoneoverflow.application.statistics;

import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserStatisticsDTO {
    private UserId id;
    private String username;
    private int nbQuestions;
}

package ch.heigvd.amt.stoneoverflow.application.statistics;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatisticsDTO {
    int nbUsers;
    int nbQuestions;
    int nbViews;
}

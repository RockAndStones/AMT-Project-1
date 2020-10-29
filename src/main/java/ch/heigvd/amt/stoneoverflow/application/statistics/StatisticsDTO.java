package ch.heigvd.amt.stoneoverflow.application.statistics;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

@Builder
@Getter
public class StatisticsDTO {
    int nbUsers;
    int nbQuestions;
    int nbViews;
    int nbVotes;
    Collection<UserStatisticsDTO> mostActiveUsers;
    QuestionsDTO mostVotedQuestions;
}

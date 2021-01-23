package ch.heigvd.amt.stoneoverflow.application.statistics;

import ch.heigvd.amt.stoneoverflow.application.pagination.PaginationDTO;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class PointsRankingsDTO {
    @Builder
    @Getter
    @EqualsAndHashCode
    public static class PointsRankingDTO {
        String username;
        double points;
    }

    List<PointsRankingDTO> rankings;
    PaginationDTO pagination;
}

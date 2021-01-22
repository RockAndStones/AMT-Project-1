package ch.heigvd.amt.stoneoverflow.application.statistics;

import ch.heigvd.amt.stoneoverflow.application.pagination.PaginationDTO;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class BadgesRankingsDTO {
    @Builder
    @Getter
    @EqualsAndHashCode
    public static class BadgesRankingDTO {
        String username;
        int badges;
    }

    List<BadgesRankingDTO> rankings;
    PaginationDTO pagination;
}

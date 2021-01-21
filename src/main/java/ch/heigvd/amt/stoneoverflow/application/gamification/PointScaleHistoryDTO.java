package ch.heigvd.amt.stoneoverflow.application.gamification;

import ch.heigvd.amt.stoneoverflow.application.comment.CommentsDTO;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class PointScaleHistoryDTO {
    @Builder
    @Getter
    @EqualsAndHashCode
    public static class PointScaleDTO {
        private int  id;
        private String  name;
    }

    @Singular
    private List<PointScaleHistoryDTO.PointScaleDTO> pointscales;
}

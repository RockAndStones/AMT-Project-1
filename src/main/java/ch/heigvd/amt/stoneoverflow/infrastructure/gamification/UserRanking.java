package ch.heigvd.amt.stoneoverflow.infrastructure.gamification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRanking {
    private String userId;
    private double points;
}

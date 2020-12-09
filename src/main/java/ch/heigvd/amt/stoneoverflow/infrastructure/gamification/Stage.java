package ch.heigvd.amt.stoneoverflow.infrastructure.gamification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Stage {
    private double points;
    private Badge badge;
}

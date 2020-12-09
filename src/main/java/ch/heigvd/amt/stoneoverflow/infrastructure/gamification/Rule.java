package ch.heigvd.amt.stoneoverflow.infrastructure.gamification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Rule {
    private String badgeName;
    private String description;
    private String eventType;
    private String name;
    private int pointScaleId;
    private double pointsToAdd;
}

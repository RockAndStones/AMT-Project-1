package ch.heigvd.amt.stoneoverflow.infrastructure.gamification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class User {
    private String userAppId;
    private List<Badge> badges;
}

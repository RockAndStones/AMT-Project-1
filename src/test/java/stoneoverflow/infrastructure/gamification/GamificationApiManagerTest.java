package stoneoverflow.infrastructure.gamification;

import ch.heigvd.amt.stoneoverflow.infrastructure.gamification.Badge;
import ch.heigvd.amt.stoneoverflow.infrastructure.gamification.GamificationApiManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamificationApiManagerTest {
    GamificationApiManager gApiManager = new GamificationApiManager("unitTests-" +  RandomStringUtils.random(10, true, true));

    @Test
    public void shouldCreateBadge(){
        assertEquals(201, gApiManager.createBadge(  RandomStringUtils.random(10, true, true),
                                                            RandomStringUtils.random(10, true, true)));
    }
}

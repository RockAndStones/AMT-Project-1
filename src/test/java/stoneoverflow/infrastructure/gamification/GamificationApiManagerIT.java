package stoneoverflow.infrastructure.gamification;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.ApiResponse;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.NewApplication;
import ch.heigvd.amt.stoneoverflow.infrastructure.gamification.GamificationApiManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.util.Properties;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GamificationApiManagerIT {
    GamificationApiManager gApiManager = new GamificationApiManager("unitTests-" +  RandomStringUtils.random(10, true, true));

    private static final DefaultApi api = new DefaultApi();

    @BeforeAll
    static void initApi() throws IOException {
        Properties properties = new Properties();
        properties.load(GamificationApiManagerIT.class.getClassLoader().getResourceAsStream("environment.properties"));
        String url = properties.getProperty("ch.heigvd.amt.gamification.server.url");
        api.getApiClient().setBasePath(url);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void testApi() throws ApiException {
        NewApplication app = new NewApplication();
        app.setName(RandomStringUtils.random(10, true, true));
        ApiResponse<Void> resp = api.createApplicationWithHttpInfo(app);
        assertEquals(201, resp.getStatusCode());
    }

//    @Test
//    public void shouldCreateBadge(){
//        assertEquals(201, gApiManager.createBadge(  RandomStringUtils.random(10, true, true),
//                                                            RandomStringUtils.random(10, true, true)));
//    }
}

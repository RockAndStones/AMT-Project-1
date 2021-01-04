package ch.heigvd.amt.stoneoverflow.application.gamification;

import ch.heigvd.amt.gamification.ApiCallback;
import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.dto.*;
import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Arquillian.class)
public class GamificationFacadeIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private AuthenticatedUserDTO testUser;
    private GamificationFacade gamificationFacade;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "com.squareup.okhttp3")
                .addPackages(true, "com.google.gson")
                .addPackages(true, "io.gsonfire")
                .addPackages(true, "okhttp3")
                .addPackages(true, "okio")
                .addPackages(true, "org.springframework.security.crypto.bcrypt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt.BCrypt")
                .addAsResource("environment.properties");
        return archive;
    }

    @Before
    public void init() throws LoginFailedException {
        System.out.println("init-1"); //todo: debug
        this.testUser = serviceRegistry.getIdentityManagementFacade().login(LoginCommand.builder()
                .username("test")
                .plaintextPassword("test")
                .build());
        System.out.println("init-2");//todo: debug
        this.gamificationFacade = new GamificationFacade("unitTests-" +  RandomStringUtils.random(10, true, true));
        System.out.println("init-3");//todo: debug
        this.gamificationFacade.stonerProgressAsync(testUser.getId().asString(), newApiCallbackVoidFailOnFailure());
    }

    @Test
    public void shouldBeInstantiate() {
        System.out.println(gamificationFacade);
        assertTrue(gamificationFacade.isInstantiate());
    }

    @Test
    public void shouldAddQuestionAsync() {
        gamificationFacade.addQuestionAsync(testUser.getId().asString(), newApiCallbackVoidFailOnFailure());
    }

    @Test
    public void shouldAddReplyAsync() {
        gamificationFacade.addReplyAsync(testUser.getId().asString(), newApiCallbackVoidFailOnFailure());
    }

    @Test
    public void shouldAddCommentAsync() {
        gamificationFacade.addCommentAsync(testUser.getId().asString(), newApiCallbackVoidFailOnFailure());
    }

    @Test
    public void shouldAddAndRemoveVoteAsync() {
        gamificationFacade.addVoteAsync(testUser.getId().asString(), newApiCallbackVoidFailOnFailure());
        gamificationFacade.removeVoteAsync(testUser.getId().asString(), newApiCallbackVoidFailOnFailure());
    }

    @Test
    public void shouldProgressAndRegressStonerAsync() {
        gamificationFacade.stonerProgressAsync(testUser.getId().asString(), newApiCallbackVoidFailOnFailure());
        gamificationFacade.stonerRegressAsync(testUser.getId().asString(), newApiCallbackVoidFailOnFailure());
    }

    @Test
    public void shouldGetRightTestUserInfo() {
        UserInfo testUserInfo = gamificationFacade.getUserInfo(testUser.getId().asString());
        assertNotNull(testUserInfo);
        assertEquals(Integer.valueOf(8), testUserInfo.getPoints());
        assertNotNull(testUserInfo.getBadges());
        assertEquals(5, testUserInfo.getBadges().size());
    }

    @Test
    public void shouldGetRightBadgesRankings() {
        PaginatedBadgesRankings badgesRankings = gamificationFacade.getBadgesRankings(0, 10);
        assertNotNull(badgesRankings);
        assertNotNull(badgesRankings.getData());
        assertTrue(badgesRankings.getData().contains(new BadgesRanking()
                .userId(testUser.getId().asString())
                .points(8)));
    }

    @Test
    public void shouldGetRightPointsRankings() {
        PaginatedPointsRankings pointsRankings = gamificationFacade.getPointsRankings(0, 10);
        assertNotNull(pointsRankings);
        assertNotNull(pointsRankings.getData());
        assertTrue(pointsRankings.getData().contains(new PointsRanking()
                .userId(testUser.getId().asString())
                .points(5d)));
    }

    private ApiCallback<Void> newApiCallbackVoidFailOnFailure() {
        return new ApiCallback<Void>() {
            @Override
            public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
                fail();
            }
            @Override
            public void onSuccess(Void result, int statusCode, Map<String, List<String>> responseHeaders) {
                System.out.println("IT test action success!");//todo: debug
            }
            @Override
            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) { }
            @Override
            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) { }
        };
    }

}

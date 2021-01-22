package ch.heigvd.amt.stoneoverflow.application.history;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.application.gamification.PointScaleHistoryDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class HistoryFacadeIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private AuthenticatedUserDTO testUser;
    private HistoryFacade        historyFacade;
    private GamificationFacade   gamificationFacade;

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
        testUser = serviceRegistry.getIdentityManagementFacade().login(LoginCommand.builder()
                .username("test")
                .plaintextPassword("test")
                .build());
        gamificationFacade = new GamificationFacade("unitTests-GamificationFacadeIT");
        historyFacade = new HistoryFacade(gamificationFacade);
    }

    @Test
    public void shouldGetHistoryUser() {
        Collection<Map<Object, Object>> history = historyFacade.getHistoryUser(testUser.getId());
        assertNotNull(history);
        assertNotEquals(0, history.size());
    }

    @Test
    public void shouldGetHistoryUserPointScale() {
        PointScaleHistoryDTO pointScaleHistoryDTO = gamificationFacade.getPointScalesHistory();
        assertNotNull(pointScaleHistoryDTO);
        Collection<Map<Object, Object>> history = historyFacade.getHistoryUserPointScale(
                testUser.getId(),
                pointScaleHistoryDTO.getPointscales().get(0).getId());
        assertNotNull(history);
        assertNotEquals(0, history.size());
    }
}

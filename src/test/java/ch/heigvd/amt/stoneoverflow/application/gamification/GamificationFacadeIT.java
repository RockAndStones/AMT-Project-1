package ch.heigvd.amt.stoneoverflow.application.gamification;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;

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
                .addPackages(true, "ch.heigvd.amt.gamification")
                .addClass(ch.heigvd.amt.gamification.Configuration.class)
                .addPackages(true, "com.squareup.okhttp3")
                .addPackages(true, "com.google.gson")
                .addPackages(true, "io.gsonfire")
                .addPackages(true, "okhttp3")
                .addPackages(true, "okio")
                .addPackages(true, "org.springframework.security.crypto.bcrypt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt.BCrypt")
                .addAsResource(new File("src/main/resources/environment.properties"), "environment.properties");
        return archive;
    }

    @Before
    public void init() {
        gamificationFacade = new GamificationFacade("unitTests-" +  RandomStringUtils.random(10, true, true));
    }

    @Test
    public void testApi() {

    }

}

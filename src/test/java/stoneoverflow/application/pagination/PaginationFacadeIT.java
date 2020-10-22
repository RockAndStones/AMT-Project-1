package stoneoverflow.application.pagination;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.stoneoverflow.application.pagination.PaginationDTO;
import ch.heigvd.amt.stoneoverflow.application.pagination.PaginationFacade;
import ch.heigvd.amt.stoneoverflow.application.question.AddQuestionCommand;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class PaginationFacadeIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private AuthenticatedUserDTO testUser;
    private PaginationFacade paginationFacade;
    private QuestionFacade questionFacade;
    private DateDTO questionDate;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackage("org.springframework.security.crypto.bcrypt");
        return archive;
    }

    @Before
    public void init() throws LoginFailedException {
        testUser = serviceRegistry.getIdentityManagementFacade().login(LoginCommand.builder()
                .username("test")
                .plaintextPassword("test")
                .build());

        this.questionFacade = serviceRegistry.getQuestionFacade();

        paginationFacade = serviceRegistry.getPaginationFacade();
        this.questionDate = new DateDTO(new Date(System.currentTimeMillis()));
    }

    @Test
    public void shouldSetPaginationMultiplePages() {
        // Add questions in repository
        AddQuestionCommand questionCommand = AddQuestionCommand.builder()
                .creator(testUser.getUsername())
                .creatorId(testUser.getId())
                .date(questionDate)
                .build();

        for(int i = 0; i < 11; i++) {
            questionFacade.addQuestion(questionCommand);
        }


        PaginationDTO resultPagination = paginationFacade.settingPagination("2");

        PaginationDTO expectedPagination = PaginationDTO.builder()
                .limit(5)
                .allQuestions(serviceRegistry.getQuestionFacade().getNumberOfQuestions())
                .startQuestion(5)
                .lastQuestion(10)
                .startPage(1)
                .lastPage(3)
                .currentPage(2)
                .totalPages(3).build();

        assertEquals(expectedPagination.getLimit(), resultPagination.getLimit());
        assertEquals(expectedPagination.getAllQuestions(), resultPagination.getAllQuestions());
        assertEquals(expectedPagination.getStartQuestion(), resultPagination.getStartQuestion());
        assertEquals(expectedPagination.getLastQuestion(), resultPagination.getLastQuestion());
        assertEquals(expectedPagination.getStartPage(), resultPagination.getStartPage());
        assertEquals(expectedPagination.getLastPage(), resultPagination.getLastPage());
        assertEquals(expectedPagination.getCurrentPage(), resultPagination.getCurrentPage());
        assertEquals(expectedPagination.getTotalPages(), resultPagination.getTotalPages());
    }

    @Test
    public void shouldSetInitialPagination() {
        PaginationDTO resultPagination = paginationFacade.settingPagination("1");

        PaginationDTO expectedPagination = PaginationDTO.builder()
                .limit(5)
                .allQuestions(serviceRegistry.getQuestionFacade().getNumberOfQuestions())
                .startQuestion(0)
                .lastQuestion(5)
                .startPage(1)
                .lastPage(3)
                .currentPage(1)
                .totalPages(3).build();

        assertEquals(expectedPagination.getLimit(), resultPagination.getLimit());
        assertEquals(expectedPagination.getAllQuestions(), resultPagination.getAllQuestions());
        assertEquals(expectedPagination.getStartQuestion(), resultPagination.getStartQuestion());
        assertEquals(expectedPagination.getLastQuestion(), resultPagination.getLastQuestion());
        assertEquals(expectedPagination.getStartPage(), resultPagination.getStartPage());
        assertEquals(expectedPagination.getLastPage(), resultPagination.getLastPage());
        assertEquals(expectedPagination.getCurrentPage(), resultPagination.getCurrentPage());
        assertEquals(expectedPagination.getTotalPages(), resultPagination.getTotalPages());
    }
}

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

    // Should be equal to the question per page stored in the class PaginationFacade
    private static final int QUESTION_PER_PAGE = 10;

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



        int totalPages = (int) Math.ceil((double) questionFacade.getNumberOfQuestions() / (double) QUESTION_PER_PAGE);
        int lastPage = Math.min(QUESTION_PER_PAGE, totalPages);
        int lastItem = Math.min(20, questionFacade.getNumberOfQuestions());
        PaginationDTO resultPagination = paginationFacade.settingQuestionPagination("2");
        PaginationDTO expectedPagination = PaginationDTO.builder()
                .limit(QUESTION_PER_PAGE)
                .itemRepoSize(serviceRegistry.getQuestionFacade().getNumberOfQuestions())
                .startItem(10)
                .lastItem(lastItem)
                .startPage(1)
                .lastPage(lastPage)
                .currentPage(2)
                .totalPages(totalPages).build();

        assertEquals(expectedPagination.getLimit(), resultPagination.getLimit());
        assertEquals(expectedPagination.getItemRepoSize(), resultPagination.getItemRepoSize());
        assertEquals(expectedPagination.getStartItem(), resultPagination.getStartItem());
        assertEquals(expectedPagination.getLastItem(), resultPagination.getLastItem());
        assertEquals(expectedPagination.getStartPage(), resultPagination.getStartPage());
        assertEquals(expectedPagination.getLastPage(), resultPagination.getLastPage());
        assertEquals(expectedPagination.getCurrentPage(), resultPagination.getCurrentPage());
        assertEquals(expectedPagination.getTotalPages(), resultPagination.getTotalPages());
    }

    @Test
    public void shouldSetInitialPagination() {
        PaginationDTO resultPagination = paginationFacade.settingQuestionPagination("1");

        int totalPages = (int) Math.ceil((double) questionFacade.getNumberOfQuestions() / (double) QUESTION_PER_PAGE);
        int lastPage = Math.min(QUESTION_PER_PAGE, totalPages);

        PaginationDTO expectedPagination = PaginationDTO.builder()
                .limit(QUESTION_PER_PAGE)
                .itemRepoSize(serviceRegistry.getQuestionFacade().getNumberOfQuestions())
                .startItem(0)
                .lastItem(10)
                .startPage(1)
                .lastPage(lastPage)
                .currentPage(1)
                .totalPages(totalPages).build();

        assertEquals(expectedPagination.getLimit(), resultPagination.getLimit());
        assertEquals(expectedPagination.getItemRepoSize(), resultPagination.getItemRepoSize());
        assertEquals(expectedPagination.getStartItem(), resultPagination.getStartItem());
        assertEquals(expectedPagination.getLastItem(), resultPagination.getLastItem());
        assertEquals(expectedPagination.getStartPage(), resultPagination.getStartPage());
        assertEquals(expectedPagination.getLastPage(), resultPagination.getLastPage());
        assertEquals(expectedPagination.getCurrentPage(), resultPagination.getCurrentPage());
        assertEquals(expectedPagination.getTotalPages(), resultPagination.getTotalPages());
    }
}

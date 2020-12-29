package ch.heigvd.amt.stoneoverflow.application;

import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.*;
import ch.heigvd.amt.stoneoverflow.application.pagination.PaginationFacade;
import ch.heigvd.amt.stoneoverflow.application.question.AddQuestionCommand;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.stoneoverflow.application.statistics.StatisticsFacade;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteFacade;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.IUserRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.domain.vote.IVoteRepository;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class ServiceRegistry {
    @Inject @Named("JdbcQuestionRepository")
    IQuestionRepository questionRepository;

    @Inject @Named("JdbcUserRepository")
    IUserRepository userRepository;

    @Inject @Named("JdbcAnswerRepository")
    IAnswerRepository answerRepository;

    @Inject @Named("JdbcCommentRepository")
    ICommentRepository commentRepository;

    @Inject @Named("JdbcVoteRepository")
    IVoteRepository voteRepository;

    @Getter DefaultApi               gamificationApi;
    @Getter IdentityManagementFacade identityManagementFacade;
    @Getter QuestionFacade           questionFacade;
    @Getter AnswerFacade             answerFacade;
    @Getter CommentFacade            commentFacade;
    @Getter VoteFacade               voteFacade;
    @Getter StatisticsFacade         statisticsFacade;
    @Getter PaginationFacade         paginationFacade;

    @PostConstruct
    private void initDefaultValues() {
        try {
            initGamificationApi();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initFacades();
        if(userRepository.getRepositorySize() > 0) {
            return;
        }
        populateRepositories();
    }

    /**
     * Initialize facades.
     */
    private void initFacades() {
        identityManagementFacade = new IdentityManagementFacade(userRepository);
        questionFacade           = new QuestionFacade(questionRepository);
        answerFacade             = new AnswerFacade(answerRepository);
        commentFacade            = new CommentFacade(commentRepository);
        voteFacade               = new VoteFacade(voteRepository);
        statisticsFacade         = new StatisticsFacade(questionRepository, userRepository, commentRepository, answerRepository, voteRepository, questionFacade, voteFacade);
        paginationFacade         = new PaginationFacade(questionRepository, answerRepository);
    }

    /**
     * Populate StoneOverflow's repositories with default values.
     */
    private void populateRepositories() {
        // Add default users
        User u1 = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("John")
                .lastName("Smith")
                .plaintextPassword("test")
                .build();

        User u2 = User.builder()
                .username("rocky")
                .email("rocky@strongerthanyou.com")
                .firstName("Sylvester")
                .lastName("Stallone")
                .plaintextPassword("balboa")
                .build();

        User uE2e = User.builder()
                .username("e2eTester")
                .email("e2e@test.com")
                .firstName("John")
                .lastName("Smith")
                .plaintextPassword("Abcdef7!")
                .build();

        User uE2eVoter = User.builder()
                .username("e2eVoter")
                .email("e2eVoter@test.com")
                .firstName("Voter")
                .lastName("McVote")
                .plaintextPassword("Abcdef7!")
                .build();

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(uE2e); // e2e testing
        userRepository.save(uE2eVoter); // e2e testing

        // Add default questions
        questionFacade.addQuestion(AddQuestionCommand.builder()
                .title("Is it real life ??")
                .description("Well, you real ????")
                .creatorId(u1.getId())
                .creator(u1.getUsername())
                .nbViews(new AtomicInteger(44))
                .build());

        questionFacade.addQuestion(AddQuestionCommand.builder()
                .title("Do you even lift bro ?!")
                .description("Start lifting weights today, lift women tomorrow !")
                .creatorId(u1.getId())
                .creator(u1.getUsername())
                .nbViews(new AtomicInteger(40))
                .build());

        questionFacade.addQuestion(AddQuestionCommand.builder()
                .title("How can we pass parameters to an already built Vue JS app ?")
                .description("We have a Vue app that connects to a Web Service and get's some data. The web service URL is different, depending on the location we install the app on.\n" +
                        "\n" +
                        "I first thought of using .env files, but later I realized these files get injected into the minified .js files.\n" +
                        "\n" +
                        "Having this in my main.js was very convenient in the case of .env files:\n" +
                        "\n" +
                        "Vue.prototype.ApiBaseUrl = process.env.VUE_APP_APIBASEURL\n" +
                        "Vue.prototype.PrintDocsFolder = process.env.VUE_APP_PRINTDOCSFOLDER\n" +
                        "Vue.prototype.TicketPrintWSocket = process.env.VUE_APP_TICKETPRINTWSOCKET   \n" +
                        "\n" +
                        "The app is already built. I don't want to build the app for each of the hundred locations we have to deploy to. I'm not sure about the \"official\" approach for this.\n" +
                        "\n" +
                        "Is there any out of the box solution in Vue that can allow this configuration? Basically we need to have a file in the root folder of the built app, and read values for our Vue.prototype.VARIABLES.\n" +
                        "\n" +
                        "We are using vue-cli 3.")
                .creatorId(u1.getId())
                .creator(u1.getUsername())
                .nbViews(new AtomicInteger(44))
                .build());

        Question q1 = Question.builder()
                .title("Is there any herb to get your dog high like catnip?")
                .description("The question is all about the title :)")
                .creatorId(u2.getId())
                .creator(u2.getUsername())
                .nbViews(new AtomicInteger(0))
                .build();

        Question qE2e = Question.builder()
                .title("E2e testing question")
                .description("E2e testing question description")
                .creatorId(uE2e.getId())
                .creator(uE2e.getUsername())
                .nbViews(new AtomicInteger(0))
                .build();

        questionRepository.save(q1);
        questionRepository.save(qE2e); // e2e testing

        // Add default answers
        Answer a1 = Answer.builder()
                .answerTo(q1.getId())
                .description("Yes there is. It's called anise ;)")
                .creatorId(u1.getId())
                .creator(u1.getUsername()).build();

        Answer a2 = Answer.builder()
                .answerTo(q1.getId())
                .description("Is this questions a cake?")
                .creatorId(u1.getId())
                .creator("IAmALieBecauseIMayBeACakeInsideAndIAmScaredAboutThat").build();

        Answer aE2e = Answer.builder()
                .answerTo(qE2e.getId())
                .description("E2e testing answer")
                .creatorId(uE2e.getId())
                .creator(uE2e.getUsername()).build();

        answerRepository.save(a1);
        answerRepository.save(a2);
        answerRepository.save(aE2e);

        // Add default comments
        Comment c1 = Comment.builder()
                .commentTo(q1.getId())
                .description("Excellent question sir.")
                .creatorId(u1.getId())
                .creator(u1.getUsername()).build();

        Comment c2 = Comment.builder()
                .commentTo(a1.getId())
                .description("It's also called dog nip by the way.")
                .creatorId(u1.getId())
                .creator(u1.getUsername()).build();

        Comment c3 = Comment.builder()
                .commentTo(a1.getId())
                .description("Yeah it's anise, I've tried it with my dog. But since this event my dog stopped moving but it was fun I would say.")
                .creatorId(u2.getId())
                .creator(u2.getUsername()).build();

        Comment c4 = Comment.builder()
                .commentTo(a2.getId())
                .description("D*fuck is wrong with you buddy?")
                .creatorId(u2.getId())
                .creator(u2.getUsername()).build();

        Comment cE2e = Comment.builder()
                .commentTo(aE2e.getId())
                .description("E2e testing comment to answer")
                .creatorId(uE2e.getId())
                .creator(uE2e.getUsername()).build();

        Comment c2E2e = Comment.builder()
                .commentTo(qE2e.getId())
                .description("E2e testing comment to question")
                .creatorId(uE2e.getId())
                .creator(uE2e.getUsername()).build();

        commentRepository.save(c1);
        commentRepository.save(c2);
        commentRepository.save(c3);
        commentRepository.save(c4);
        commentRepository.save(cE2e); // e2e testing
        commentRepository.save(c2E2e); // e2e testing

        Vote v1 = Vote.builder()
                .votedBy(u1.getId())
                .votedObject(q1.getId())
                .voteType(Vote.VoteType.UP).build();

        Vote v2 = Vote.builder()
                .votedBy(u1.getId())
                .votedObject(a2.getId())
                .voteType(Vote.VoteType.DOWN).build();

        Vote v3 = Vote.builder()
                .votedBy(u2.getId())
                .votedObject(a2.getId())
                .voteType(Vote.VoteType.DOWN).build();

        Vote v4 = Vote.builder()
                .votedBy(u2.getId())
                .votedObject(a1.getId())
                .voteType(Vote.VoteType.UP).build();

        voteRepository.save(v1);
        voteRepository.save(v2);
        voteRepository.save(v3);
        voteRepository.save(v4);

        // Place the E2E question in the front page by giving it the biggest upvote count
        voteRepository.save(Vote.builder()
                .votedBy(u1.getId())
                .votedObject(qE2e.getId())
                .voteType(Vote.VoteType.UP).build());
        voteRepository.save(Vote.builder()
                .votedBy(u2.getId())
                .votedObject(qE2e.getId())
                .voteType(Vote.VoteType.UP).build());
        voteRepository.save(Vote.builder()
                .votedBy(uE2eVoter.getId())
                .votedObject(qE2e.getId())
                .voteType(Vote.VoteType.UP).build());
    }

    /**
     * Initialize the gamification functionality.
     * Set the application, the badges, the point scales, & the rules in the gamification engine
     * if application is not set.
     * @throws IOException Threw if environment.properties file cannot be loaded.
     */
    private void initGamificationApi() throws IOException {
        Properties properties = new Properties();
        properties.load(ServiceRegistry.class.getClassLoader().getResourceAsStream("environment.properties"));
        gamificationApi = new DefaultApi();
        gamificationApi.getApiClient().setBasePath(properties.getProperty("ch.heigvd.amt.gamification.server.url"));

        String appName = properties.getProperty("ch.heigvd.amt.gamification.app.name");

        Application app = null;
        try {
            app = gamificationApi.getApplication(appName);
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                System.out.println("Application '" + appName + "' does not exist");
            } else {
                e.printStackTrace();
            }
        }

        if (app == null) {
            NewApplication newApp = new NewApplication();
            newApp.setName(appName);
            try {
                gamificationApi.createApplicationWithHttpInfo(newApp);
                app = gamificationApi.getApplication(appName);
                gamificationApi.getApiClient().setApiKey(app.getApiKey());
            } catch (ApiException e) {
                e.printStackTrace();
            }

            // Create badges
            Badge[] questionBadges = new Badge[4];
            questionBadges[0] = Badge("First question", "You asked your first question ! Congrats !");
            questionBadges[1] = Badge("Pebble questionner", "We see you're getting used to ask questions. Keep going !");
            questionBadges[2] = Badge("Rock questionner", "Almost a stonfessional in the asking game ?!");
            questionBadges[3] = Badge("Mountain questionner", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

            Badge[] replyBadges = new Badge[4];
            replyBadges[0] = Badge("First reply", "You replied for the first time ! Congrats !");
            replyBadges[1] = Badge("Earth replier", "We see you're getting used to reply questions. Keep going !");
            replyBadges[2] = Badge("Cobblestone replier", "Almost a stonfessional in the replying game ?!");
            replyBadges[3] = Badge("Mineral replier", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

            Badge[] commentBadges = new Badge[4];
            commentBadges[0] = Badge("First comment", "You wrote your first comment ! Congrats !");
            commentBadges[1] = Badge("Sand commenter", "We see you're getting used to commenting. Keep going !");
            commentBadges[2] = Badge("Gravel commenter", "Almost a stonfessional in the commenting game ?!");
            commentBadges[3] = Badge("Crag commenter", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

            Badge[] voteBadges = new Badge[4];
            voteBadges[0] = Badge("First vote", "You voted for the first time ! Congrats !");
            voteBadges[1] = Badge("Rubble voter", "We see you're getting used to voting. Keep going !");
            voteBadges[2] = Badge("Boulder voter", "Almost a stonfessional in the voting game ?!");
            voteBadges[3] = Badge("Peak voter", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

            Badge[] stonerBadges = new Badge[4];
            stonerBadges[0] = Badge("Newcomer", "Welcome to the StoneOverflow family !");
            stonerBadges[1] = Badge("Rookie", "");
            stonerBadges[2] = Badge("Confirmed", "");
            stonerBadges[3] = Badge("Veteran", "");

            // Create point scales
            PointScale questionPointScale = PointScale(Arrays.asList(
                    Stage(1d, questionBadges[0]),
                    Stage(5d, questionBadges[1]),
                    Stage(10d, questionBadges[2]),
                    Stage(20d, questionBadges[3])));

            PointScale replyPointScale = PointScale(Arrays.asList(
                    Stage(1d, replyBadges[0]),
                    Stage(5d, replyBadges[1]),
                    Stage(10d, replyBadges[2]),
                    Stage(20d, replyBadges[3])));

            PointScale commentPointScale = PointScale(Arrays.asList(
                    Stage(1d, commentBadges[0]),
                    Stage(5d, commentBadges[1]),
                    Stage(10d, commentBadges[2]),
                    Stage(20d, commentBadges[3])));

            PointScale votePointScale = PointScale(Arrays.asList(
                    Stage(1d, voteBadges[0]),
                    Stage(5d, voteBadges[1]),
                    Stage(10d, voteBadges[2]),
                    Stage(20d, voteBadges[3])));

            PointScale stonerPointScale = PointScale(Arrays.asList(
                    Stage(1d, stonerBadges[0]),
                    Stage(5d, stonerBadges[1]),
                    Stage(10d, stonerBadges[2]),
                    Stage(20d, stonerBadges[3])));

            //todo: create rules

            // Send badges, point scales, & rules to the api
            try {
                createBadges(questionBadges, replyBadges, commentBadges, voteBadges, stonerBadges);
                createPointScales(questionPointScale, replyPointScale, commentPointScale, votePointScale, stonerPointScale);
            } catch (ApiException exception) {
                //todo: handle exception
                exception.printStackTrace();
            }

        }
    }

    /**
     * Constructor of Badge.
     * @param name
     * @param description
     * @return an instance of Badge.
     */
    private Badge Badge(String name, String description) {
        Badge badge = new Badge();
        badge.name(name);
        badge.description(description);
        return badge;
    }

    /**
     * Constructor of Stage.
     * @param points
     * @param badge
     * @return an instance of Stage.
     */
    private Stage Stage(Double points, Badge badge) {
        Stage stage = new Stage();
        stage.points(points);
        stage.badge(badge);
        return stage;
    }

    /**
     * Constructor of PointScale.
     * @param stages
     * @return an instance of PointScale.
     */
    private PointScale PointScale(List<Stage> stages) {
        PointScale pointScale = new PointScale();
        pointScale.stages(stages);
        return pointScale;
    }

    /**
     * Constructor of Rule.
     * @param name
     * @param description
     * @param eventType
     * @param pointsToAdd
     * @param badgeName
     * @param pointScaleId
     * @return an instance of Rule.
     */
    private Rule Rule(String name, String description, String eventType, Double pointsToAdd, String badgeName, Integer pointScaleId) {
        //todo: implement method
        return null;
    }

    /**
     * Create provided badges in the gamification engine.
     * @param badges
     * @throws ApiException Threw if API communication return an error.
     */
    private void createBadges(Badge[]... badges) throws ApiException {
        for (Badge[] badgesX : badges) {
            for (Badge badge : badgesX) {
                gamificationApi.createBadge(badge);
            }
        }
    }

    /**
     * Create provided point scales in the gamification engine.
     * @param pointScales
     * @throws ApiException Threw if API communication return an error.
     */
    private void createPointScales(PointScale... pointScales) throws ApiException {
        for (PointScale pointScale : pointScales) {
            gamificationApi.createPointScale(pointScale);
        }
    }

    /**
     * Create provided rules in the gamification engine.
     * @param rules
     * @throws ApiException Threw if API communication return an error.
     */
    private void createRules(Rule... rules) throws ApiException {
        //todo: implement method
    }
}

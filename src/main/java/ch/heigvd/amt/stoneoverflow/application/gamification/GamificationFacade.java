package ch.heigvd.amt.stoneoverflow.application.gamification;

import ch.heigvd.amt.gamification.ApiCallback;
import ch.heigvd.amt.gamification.ApiException;
import ch.heigvd.amt.gamification.ApiResponse;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.*;
import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static ch.heigvd.amt.stoneoverflow.application.gamification.EventType.*;

public class GamificationFacade {

    private final String RULE           = "Rule";
    private final String ENV_PROPERTIES = "environment.properties";

    private DefaultApi gamificationApi;

    /**
     * Initialize the gamification functionality.
     * Set the application, the badges, the point scales, & the rules in the gamification engine
     * if application is not set.
     */
    public GamificationFacade(String appName) {
        Properties properties = new Properties();
        try {
            properties.load(ServiceRegistry.class.getClassLoader().getResourceAsStream(ENV_PROPERTIES));
        } catch (IOException e) {
            System.out.println(ENV_PROPERTIES + " file could not be loaded. GamificationFacade instantiation aborted.");
            e.printStackTrace();
        }
        gamificationApi = new DefaultApi();
        gamificationApi.getApiClient().setBasePath(properties.getProperty("ch.heigvd.amt.gamification.server.url"));

        if (appName == null)
            appName = properties.getProperty("ch.heigvd.amt.gamification.app.name");

        // Check if application is already set in the gamification engine
        Application app = null;
        try {
            app = gamificationApi.getApplication(appName);
            gamificationApi.getApiClient().setApiKey(app.getApiKey());
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                System.out.println("Application '" + appName + "' does not exist");
            } else {
                e.printStackTrace();
            }
        }

        // If not, generate application, badges, pointscales and rules
        if (app == null) {
            NewApplication newApp = new NewApplication();
            newApp.setName(appName);

            try {
                gamificationApi.createApplicationWithHttpInfo(newApp);
                app = gamificationApi.getApplication(appName);
            } catch (ApiException e) {
                closeGamificationApi(e);
                return;
            }

            gamificationApi.getApiClient().setApiKey(app.getApiKey());

            // Create badges
            Badge[] questionBadges = new Badge[4];
            questionBadges[0] = GamificationHelper.createBadge("First question", "You asked your first question ! Congrats !");
            questionBadges[1] = GamificationHelper.createBadge("Pebble questionner", "We see you're getting used to ask questions. Keep going !");
            questionBadges[2] = GamificationHelper.createBadge("Rock questionner", "Almost a stonfessional in the asking game ?!");
            questionBadges[3] = GamificationHelper.createBadge("Mountain questionner", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

            BadgeName[] questionBadgesName = new BadgeName[4];
            questionBadgesName[0] = GamificationHelper.createBadgeName("First question");
            questionBadgesName[1] = GamificationHelper.createBadgeName("Pebble questionner");
            questionBadgesName[2] = GamificationHelper.createBadgeName("Rock questionner");
            questionBadgesName[3] = GamificationHelper.createBadgeName("Mountain questionner");

            Badge[] replyBadges = new Badge[4];
            replyBadges[0] = GamificationHelper.createBadge("First reply", "You replied for the first time ! Congrats !");
            replyBadges[1] = GamificationHelper.createBadge("Earth replier", "We see you're getting used to reply questions. Keep going !");
            replyBadges[2] = GamificationHelper.createBadge("Cobblestone replier", "Almost a stonfessional in the replying game ?!");
            replyBadges[3] = GamificationHelper.createBadge("Mineral replier", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

            BadgeName[] replyBadgesName = new BadgeName[4];
            replyBadgesName[0] = GamificationHelper.createBadgeName("First reply");
            replyBadgesName[1] = GamificationHelper.createBadgeName("Earth replier");
            replyBadgesName[2] = GamificationHelper.createBadgeName("Cobblestone replier");
            replyBadgesName[3] = GamificationHelper.createBadgeName("Mineral replier");

            Badge[] commentBadges = new Badge[4];
            commentBadges[0] = GamificationHelper.createBadge("First comment", "You wrote your first comment ! Congrats !");
            commentBadges[1] = GamificationHelper.createBadge("Sand commenter", "We see you're getting used to commenting. Keep going !");
            commentBadges[2] = GamificationHelper.createBadge("Gravel commenter", "Almost a stonfessional in the commenting game ?!");
            commentBadges[3] = GamificationHelper.createBadge("Crag commenter", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

            BadgeName[] commentBadgesName = new BadgeName[4];
            commentBadgesName[0] = GamificationHelper.createBadgeName("First comment");
            commentBadgesName[1] = GamificationHelper.createBadgeName("Sand commenter");
            commentBadgesName[2] = GamificationHelper.createBadgeName("Gravel commenter");
            commentBadgesName[3] = GamificationHelper.createBadgeName("Crag commenter");

            Badge[] voteBadges = new Badge[4];
            voteBadges[0] = GamificationHelper.createBadge("First vote", "You voted for the first time ! Congrats !");
            voteBadges[1] = GamificationHelper.createBadge("Rubble voter", "We see you're getting used to voting. Keep going !");
            voteBadges[2] = GamificationHelper.createBadge("Boulder voter", "Almost a stonfessional in the voting game ?!");
            voteBadges[3] = GamificationHelper.createBadge("Peak voter", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

            BadgeName[] voteBadgesName = new BadgeName[4];
            voteBadgesName[0] = GamificationHelper.createBadgeName("First vote");
            voteBadgesName[1] = GamificationHelper.createBadgeName("Rubble voter");
            voteBadgesName[2] = GamificationHelper.createBadgeName("Boulder voter");
            voteBadgesName[3] = GamificationHelper.createBadgeName("Peak voter");

            Badge[] stonerBadges = new Badge[4];
            stonerBadges[0] = GamificationHelper.createBadge("Newcomer", "Welcome to the StoneOverflow family !");
            stonerBadges[1] = GamificationHelper.createBadge("Rookie", "We see you're getting used to StoneOverflow. Keep going !");
            stonerBadges[2] = GamificationHelper.createBadge("Confirmed", "Almost a stonfessional in the StoneOverflow game ?!");
            stonerBadges[3] = GamificationHelper.createBadge("Veteran", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

            BadgeName[] stonerBadgesName = new BadgeName[4];
            stonerBadgesName[0] = GamificationHelper.createBadgeName("Newcomer");
            stonerBadgesName[1] = GamificationHelper.createBadgeName("Rookie");
            stonerBadgesName[2] = GamificationHelper.createBadgeName("Confirmed");
            stonerBadgesName[3] = GamificationHelper.createBadgeName("Veteran");

            // Add all badges
            try {
                createBadges(questionBadges, replyBadges, commentBadges, voteBadges, stonerBadges);
            } catch (ApiException e) {
                closeGamificationApi(e);
                return;
            }

            // Create point scales
            PointScale questionPointScale = GamificationHelper.createPointScale(Arrays.asList(
                    GamificationHelper.createStage(1d, questionBadgesName[0]),
                    GamificationHelper.createStage(5d, questionBadgesName[1]),
                    GamificationHelper.createStage(10d, questionBadgesName[2]),
                    GamificationHelper.createStage(20d, questionBadgesName[3])));

            PointScale replyPointScale = GamificationHelper.createPointScale(Arrays.asList(
                    GamificationHelper.createStage(1d, replyBadgesName[0]),
                    GamificationHelper.createStage(5d, replyBadgesName[1]),
                    GamificationHelper.createStage(10d, replyBadgesName[2]),
                    GamificationHelper.createStage(20d, replyBadgesName[3])));

            PointScale commentPointScale = GamificationHelper.createPointScale(Arrays.asList(
                    GamificationHelper.createStage(1d, commentBadgesName[0]),
                    GamificationHelper.createStage(5d, commentBadgesName[1]),
                    GamificationHelper.createStage(10d, commentBadgesName[2]),
                    GamificationHelper.createStage(20d, commentBadgesName[3])));

            PointScale votePointScale = GamificationHelper.createPointScale(Arrays.asList(
                    GamificationHelper.createStage(1d, voteBadgesName[0]),
                    GamificationHelper.createStage(5d, voteBadgesName[1]),
                    GamificationHelper.createStage(10d, voteBadgesName[2]),
                    GamificationHelper.createStage(20d, voteBadgesName[3])));

            PointScale stonerPointScale = GamificationHelper.createPointScale(Arrays.asList(
                    GamificationHelper.createStage(1d, stonerBadgesName[0]),
                    GamificationHelper.createStage(25d, stonerBadgesName[1]),
                    GamificationHelper.createStage(80d, stonerBadgesName[2]),
                    GamificationHelper.createStage(120d, stonerBadgesName[3])));

            // Add all point scale
            String[] pointScaleIds = new String[5];
            try {
                pointScaleIds[0] = getLastFieldOfLocationHeader(createPointScale(questionPointScale));
                pointScaleIds[1] = getLastFieldOfLocationHeader(createPointScale(replyPointScale));
                pointScaleIds[2] = getLastFieldOfLocationHeader(createPointScale(commentPointScale));
                pointScaleIds[3] = getLastFieldOfLocationHeader(createPointScale(votePointScale));
                pointScaleIds[4] = getLastFieldOfLocationHeader(createPointScale(stonerPointScale));
            } catch (ApiException e) {
                closeGamificationApi(e);
                return;
            }

            // Create rules
            Rule newQuestionRule = GamificationHelper.createRule(NEW_QUESTION.name + RULE,
                    "New question rule to apply when a user create a question.",
                    NEW_QUESTION.name,
                    1d,
                    null,
                    Integer.parseInt(pointScaleIds[0]));

            Rule newReplyRule = GamificationHelper.createRule(NEW_REPLY.name + RULE,
                    "New reply rule to apply when a user respond to a question.",
                    NEW_REPLY.name,
                    1d,
                    null,
                    Integer.parseInt(pointScaleIds[1]));

            Rule newCommentRule = GamificationHelper.createRule(NEW_COMMENT.name + RULE,
                    "New comment rule to apply when a user comment a question or a reply.",
                    NEW_COMMENT.name,
                    1d,
                    null,
                    Integer.parseInt(pointScaleIds[2]));

            Rule newVoteRule = GamificationHelper.createRule(NEW_VOTE.name + RULE,
                    "New vote rule to apply when a user up/down vote a question or a reply.",
                    NEW_VOTE.name,
                    1d,
                    null,
                    Integer.parseInt(pointScaleIds[3]));

            Rule removeVoteRule = GamificationHelper.createRule(REMOVE_VOTE.name + RULE,
                    "Remove vote rule to apply when a user remove his own vote from a question or a reply.",
                    REMOVE_VOTE.name,
                    -1d,
                    null,
                    Integer.parseInt(pointScaleIds[3]));

            Rule stonerProgressRule = GamificationHelper.createRule(STONER_PROGRESS.name + RULE,
                    "Stoner progress rule to apply when a user progress in the stoner game.",
                    STONER_PROGRESS.name,
                    1d,
                    null,
                    Integer.parseInt(pointScaleIds[4]));

            Rule stonerRegressRule = GamificationHelper.createRule(STONER_REGRESS.name + RULE,
                    "Stoner regress rule to apply when a user regress in the stoner game.",
                    STONER_REGRESS.name,
                    -1d,
                    null,
                    Integer.parseInt(pointScaleIds[4]));

            // Add all rules
            try {
                createRules(newQuestionRule, newReplyRule, newCommentRule, newVoteRule, removeVoteRule,
                        stonerProgressRule, stonerRegressRule);
            } catch (ApiException e) {
                closeGamificationApi(e);
            }
        }
    }

    /**
     * Check if the gamification engine communication is ready.
     * @return True if ready.
     */
    public Boolean isInstantiate() {
        return gamificationApi != null;
    }

    /**
     * Add a question in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void addQuestionAsync(String userId, ApiCallback<Void> callback) {
        if (isInstantiate()) {
            try {
                newEventAsync(userId, NEW_QUESTION, callback);
            } catch (ApiException apiException) {
                apiException.printStackTrace();
            }
        }
    }

    /**
     * Add a reply in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void addReplyAsync(String userId, ApiCallback<Void> callback) {
        if (isInstantiate()) {
            try {
                newEventAsync(userId, NEW_REPLY, callback);
            } catch (ApiException apiException) {
                apiException.printStackTrace();
            }
        }
    }

    /**
     * Add a comment in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void addCommentAsync(String userId, ApiCallback<Void> callback) {
        if (isInstantiate()) {
            try {
                newEventAsync(userId, NEW_COMMENT, callback);
            } catch (ApiException apiException) {
                apiException.printStackTrace();
            }
        }
    }

    /**
     * Add a reply in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void addVoteAsync(String userId, ApiCallback<Void> callback) {
        if (isInstantiate()) {
            try {
                newEventAsync(userId, NEW_VOTE, callback);
            } catch (ApiException apiException) {
                apiException.printStackTrace();
            }
        }
    }

    /**
     * Add a vote in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void removeVoteAsync(String userId, ApiCallback<Void> callback) {
        if (isInstantiate()) {
            try {
                newEventAsync(userId, REMOVE_VOTE, callback);
            } catch (ApiException apiException) {
                apiException.printStackTrace();
            }
        }
    }

    /**
     * Progress in the stoner game toward the given user in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void stonerProgressAsync(String userId, ApiCallback<Void> callback) {
        if (isInstantiate()) {
            try {
                newEventAsync(userId, STONER_PROGRESS, callback);
            } catch (ApiException apiException) {
                apiException.printStackTrace();
            }
        }
    }

    /**
     * Regress in the stoner game toward the given user in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void stonerRegressAsync(String userId, ApiCallback<Void> callback) {
        if (isInstantiate()) {
            try {
                newEventAsync(userId, STONER_REGRESS, callback);
            } catch (ApiException apiException) {
                apiException.printStackTrace();
            }
        }
    }

    /**
     * Get user info (points & badges).
     * @param userId The userId of the requested user.
     * @return an instance of UserInfo.
     */
    public UserInfo getUserInfo(String userId) {
        if (isInstantiate()) {
            try {
                return gamificationApi.getUser(userId);
            } catch (ApiException apiException) {
                apiException.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Get the paginated badges rankings.
     * @param page The requested page.
     * @param pageSize Amount of badges per page.
     * @return an instance of PaginatedBadgesRankings.
     */
    public PaginatedBadgesRankings getBadgesRankings(Integer page, Integer pageSize)  {
        if (isInstantiate()) {
            try {
                return gamificationApi.getRankingsByTotalBadges(page, pageSize);
            } catch (ApiException apiException) {
                apiException.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Get the paginated points rankings.
     * @param page The requested page.
     * @param pageSize Amount of badges per page.
     * @return an instance of PaginatedPointsRankings.
     */
    public  PaginatedPointsRankings getPointsRankings(Integer page, Integer pageSize)  {
        if (isInstantiate()) {
            try {
                System.out.println(page);
                System.out.println(pageSize);
                return gamificationApi.getRankingsByTotalPoints(page, pageSize);
            } catch (ApiException apiException) {
                System.out.println(apiException.getCode());
                apiException.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Add an Event in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param eventType Type of event to add.
     * @param callback API callback to handle api result.
     * @throws ApiException threw if an error occurred with the gamification engine.
     */
    private void newEventAsync(String userId, EventType eventType, ApiCallback<Void> callback) throws ApiException {

        if (callback == null) {
            callback = new ApiCallback<Void>() {
                @Override
                public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) { }
                @Override
                public void onSuccess(Void result, int statusCode, Map<String, List<String>> responseHeaders) { }
                @Override
                public void onUploadProgress(long bytesWritten, long contentLength, boolean done) { }
                @Override
                public void onDownloadProgress(long bytesRead, long contentLength, boolean done) { }
            };
        }

        gamificationApi.createEventAsync(GamificationHelper.createEvent(
                userId,
                OffsetDateTime.now(),
                eventType.name),
                callback);
    }

    /**
     * Create provided badges in the gamification engine.
     * @param badges Badges list.
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
     * Create provided point scale in the gamification engine.
     * @param pointScale PointScale to create.
     * @throws ApiException Threw if API communication return an error.
     * @return The ApiResponse to parse with [processApiResponse] method.
     */
    private ApiResponse<Void> createPointScale(PointScale pointScale) throws ApiException {
        return gamificationApi.createPointScaleWithHttpInfo(pointScale);
    }

    /**
     * Create provided rules in the gamification engine.
     * @param rules Rules  list.
     * @throws ApiException Threw if API communication return an error.
     */
    private void createRules(Rule... rules) throws ApiException {
        for (Rule rule : rules) {
            gamificationApi.createRule(rule);
        }
    }

    /**
     * Process an [ApiResponse] and return the 'Location' header field.
     * @param apiResponse Response to process.
     * @return The 'Location' header field or null if field not present.
     */
    private String getLastFieldOfLocationHeader(ApiResponse apiResponse) throws ApiException {
        List<String> locationHeaderValues = (List<String>)apiResponse.getHeaders().get("Location");
        if (locationHeaderValues != null && locationHeaderValues.get(0) != null) {
            String[] tokens = locationHeaderValues.get(0).split("/");
            return tokens[tokens.length - 1];
        }
        throw new ApiException("One or many returned point scale id are null.");
    }

    /**
     * Close the gamification engine communication and print a error message.
     * @param apiException Exception which caused the closing action.
     */
    private void closeGamificationApi(ApiException apiException) {
        gamificationApi = null;
        System.out.println("An error occurred with the gamification engine:");
        apiException.printStackTrace();
    }
}

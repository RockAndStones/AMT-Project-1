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
            stonerBadges[1] = Badge("Rookie", "We see you're getting used to StoneOverflow. Keep going !");
            stonerBadges[2] = Badge("Confirmed", "Almost a stonfessional in the StoneOverflow game ?!");
            stonerBadges[3] = Badge("Veteran", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

            // Add all badges
            try {
                createBadges(questionBadges, replyBadges, commentBadges, voteBadges, stonerBadges);
            } catch (ApiException e) {
                closeGamificationApi(e);
                return;
            }

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
                    Stage(25d, stonerBadges[1]),
                    Stage(80d, stonerBadges[2]),
                    Stage(120d, stonerBadges[3])));

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
            Rule newQuestionRule = Rule(NEW_QUESTION.name + RULE,
                    "New question rule to apply when a user create a question.",
                    NEW_QUESTION.name,
                    1d,
                    null,
                    Integer.parseInt(pointScaleIds[0]));

            Rule newReplyRule = Rule(NEW_REPLY.name + RULE,
                    "New reply rule to apply when a user respond to a question.",
                    NEW_REPLY.name,
                    1d,
                    null,
                    Integer.parseInt(pointScaleIds[1]));

            Rule newCommentRule = Rule(NEW_COMMENT.name + RULE,
                    "New comment rule to apply when a user comment a question or a reply.",
                    NEW_COMMENT.name,
                    1d,
                    null,
                    Integer.parseInt(pointScaleIds[2]));

            Rule newVoteRule = Rule(NEW_VOTE.name + RULE,
                    "New vote rule to apply when a user up/down vote a question or a reply.",
                    NEW_VOTE.name,
                    1d,
                    null,
                    Integer.parseInt(pointScaleIds[3]));

            Rule removeVoteRule = Rule(REMOVE_VOTE.name + RULE,
                    "Remove vote rule to apply when a user remove his own vote from a question or a reply.",
                    REMOVE_VOTE.name,
                    -1d,
                    null,
                    Integer.parseInt(pointScaleIds[3]));

            Rule stonerProgressRule = Rule(STONER_PROGRESS.name + RULE,
                    "Stoner progress rule to apply when a user progress in the stoner game.",
                    STONER_PROGRESS.name,
                    1d,
                    null,
                    Integer.parseInt(pointScaleIds[4]));

            Rule stonerRegressRule = Rule(STONER_REGRESS.name + RULE,
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
                return gamificationApi.getRankingsByTotalPoints(page, pageSize);
            } catch (ApiException apiException) {
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

        gamificationApi.createEventAsync(Event(
                userId,
                OffsetDateTime.now(),
                eventType.name,
                null),
                callback);
    }

    /**
     * Constructor of Badge.
     * @param name        Badge's attribute.
     * @param description Badge's attribute.
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
     * @param points Stage's attribute.
     * @param badge  Stage's attribute.
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
     * @param stages PointScale's attribute.
     * @return an instance of PointScale.
     */
    private PointScale PointScale(List<Stage> stages) {
        PointScale pointScale = new PointScale();
        pointScale.stages(stages);
        return pointScale;
    }

    /**
     * Constructor of Rule.
     * @param name         Rule's attribute.
     * @param description  Rule's attribute.
     * @param eventType    Rule's attribute.
     * @param pointsToAdd  Rule's attribute.
     * @param badgeName    Rule's attribute.
     * @param pointScaleId Rule's attribute.
     * @return an instance of Rule.
     */
    private Rule Rule(String name, String description, String eventType, Double pointsToAdd, String badgeName, Integer pointScaleId) {
        Rule rule = new Rule();
        rule.name(name);
        rule.description(description);
        rule.eventType(eventType);
        rule.pointsToAdd(pointsToAdd);
        rule.badgeName(badgeName);
        rule.pointScaleId(pointScaleId);
        return rule;
    }

    /**
     * Constructor of Event.
     * @param userAppId       Event's attribute.
     * @param timestamp       Event's attribute.
     * @param eventType       Event's attribute.
     * @param eventProperties Event's attribute.
     * @return an instance of Event.
     */
    private Event Event(String userAppId, OffsetDateTime timestamp, String eventType, Object eventProperties) {
        Event event = new Event();
        event.userAppId(userAppId);
        event.timestamp(timestamp);
        event.eventType(eventType);
        event.eventProperties(eventProperties);
        return event;
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

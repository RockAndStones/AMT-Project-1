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

    public static final String RULE            = "Rule";
    private static final String ENV_PROPERTIES = "environment.properties";

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
            Badge[] questionBadges = GamificationHelper.getAppInitQuestionBadges();
            Badge[] replyBadges = GamificationHelper.getAppInitReplyBadges();
            Badge[] commentBadges = GamificationHelper.getAppInitCommentBadges();
            Badge[] voteBadges = GamificationHelper.getAppInitVoteBadges();
            Badge[] stonerBadges = GamificationHelper.getAppInitStonerBadges();

            BadgeName[] questionBadgesName = GamificationHelper.getBadgeNamesFromBadges(questionBadges);
            BadgeName[] replyBadgesName = GamificationHelper.getBadgeNamesFromBadges(replyBadges);
            BadgeName[] commentBadgesName = GamificationHelper.getBadgeNamesFromBadges(commentBadges);
            BadgeName[] voteBadgesName = GamificationHelper.getBadgeNamesFromBadges(voteBadges);
            BadgeName[] stonerBadgesName = GamificationHelper.getBadgeNamesFromBadges(stonerBadges);

            // Add all badges
            try {
                createBadges(questionBadges, replyBadges, commentBadges, voteBadges, stonerBadges);
            } catch (ApiException e) {
                closeGamificationApi(e);
                return;
            }

            PointScale[] pointScales = GamificationHelper.getAppInitPointScales(questionBadgesName,
                    replyBadgesName,
                    commentBadgesName,
                    voteBadgesName,
                    stonerBadgesName);

            // Add all point scale
            String[] pointScaleIds = new String[pointScales.length];
            try {
                for (int i = 0; i < pointScales.length; i++) {
                    pointScaleIds[i] = getLastFieldOfLocationHeader(createPointScale(pointScales[i]));
                }
            } catch (ApiException e) {
                closeGamificationApi(e);
                return;
            }

            Rule[] rules = GamificationHelper.getAppInitRules(pointScaleIds);
            // Add all rules
            try {
                createRules(rules);
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
        newEventAsync(userId, NEW_QUESTION, callback);
    }

    /**
     * Add a reply in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void addReplyAsync(String userId, ApiCallback<Void> callback) {
        newEventAsync(userId, NEW_REPLY, callback);
    }

    /**
     * Add a comment in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void addCommentAsync(String userId, ApiCallback<Void> callback) {
        newEventAsync(userId, NEW_COMMENT, callback);
    }

    /**
     * Add a reply in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void addVoteAsync(String userId, ApiCallback<Void> callback) {
        newEventAsync(userId, NEW_VOTE, callback);
    }

    /**
     * Add a vote in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void removeVoteAsync(String userId, ApiCallback<Void> callback) {
        newEventAsync(userId, REMOVE_VOTE, callback);
    }

    /**
     * Progress in the stoner game toward the given user in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void stonerProgressAsync(String userId, ApiCallback<Void> callback) {
        newEventAsync(userId, STONER_PROGRESS, callback);
    }

    /**
     * Regress in the stoner game toward the given user in the gamification engine.
     * @param userId The userId of the user who performed the action.
     * @param callback API callback to handle api result.
     */
    public void stonerRegressAsync(String userId, ApiCallback<Void> callback) {
        newEventAsync(userId, STONER_REGRESS, callback);
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
     */
    public void newEventAsync(String userId, EventType eventType, ApiCallback<Void> callback) {
        if (!isInstantiate())
            return;

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

        try {
            gamificationApi.createEventAsync(GamificationHelper.createEvent(
                    userId,
                    OffsetDateTime.now(),
                    eventType.name),
                    callback);
        } catch (ApiException apiException) {
            apiException.printStackTrace();
        }
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

package ch.heigvd.amt.stoneoverflow.infrastructure.gamification;

import ch.heigvd.amt.gamification.*;
import ch.heigvd.amt.gamification.api.DefaultApi;
import ch.heigvd.amt.gamification.api.dto.*;

import java.util.List;

public class GamificationApiManager {

    private enum Endpoint {
        APPLICATIONS("applications"),
        BADGES("badges"),
        EVENTS("events"),
        POINT_SCALES("pointscales"),
        RANKINGS("rankings"),
        RULES("rules"),
        USERS("users");

        public final String value;

        Endpoint(String value) {
            this.value = value;
        }
    }

    private boolean hasApiKey = false;

    private DefaultApi api;

    public GamificationApiManager(String appName) throws ApiException {

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://localhost:8081");

        this.api = new DefaultApi(apiClient);

        NewApplication app = new NewApplication();
        app.setName(appName);
        ApiResponse<Void> resp = api.createApplicationWithHttpInfo(app);

        System.out.println("api response: " + resp.getStatusCode());

        /*
        // Setting a default base url and default headers
        Unirest.config()
                .defaultBaseUrl("http://localhost:8081/")
                .addDefaultHeader("accept", "application/json")
                .addDefaultHeader("Content-Type", "application/json");

        try {
            // Registering the application to the API
            int appCreationStatusCode = createApplication(appName);

            if(appCreationStatusCode == 201){
                // Retrieving the API key
                Application app = getApplication(appName);

                // Adding the API key in the default headers
                Unirest.config().addDefaultHeader("X-API-KEY", app.getApiKey());
                gotApiKey = true;
            } else {
                // Kill all Unirest threads
                Unirest.shutDown();
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        */
    }

    public int createApplication(String name){
        /*
        HttpResponse createApplicationResponse
                = Unirest.post(Endpoint.APPLICATIONS.value)
                .body(new Application(name, null))
                .asEmpty();

        return createApplicationResponse.getStatus();
         */
        return 0;
    }

    public Application getApplication(String name){
        /*
        HttpResponse<Application> applicationResponse
                = Unirest.get(Endpoint.APPLICATIONS.value + "/" + name)
                .asObject(Application.class);

        return applicationResponse.getBody();

          */
        return null;
    }

    public List<Badge> getBadges(){
        /*
        if(!hasApiKey){ return null; }

        HttpResponse<List<Badge>> badgesResponse =
                Unirest.get(Endpoint.BADGES.value)
                        .asObject(new GenericType<List<Badge>>() {});

        return badgesResponse.getBody();

         */
        return null;
    }

    public int createBadge(String description, String name){
        /*
        if(!hasApiKey){ return 0; }

        HttpResponse createBadgeResponse
                = Unirest.post(Endpoint.BADGES.value)
                .body(new Badge(description, name))
                .asEmpty();

        return createBadgeResponse.getStatus();
         */
        return 0;
    }

    public Badge getBadge(String name){
        /*
        if(!hasApiKey){ return null; }

        HttpResponse<Badge> badgeResponse =
                Unirest.get(Endpoint.BADGES.value + "/" + name)
                        .asObject(Badge.class);

        return badgeResponse.getBody();

         */
        return null;
    }

    public int putBadge(String description, String name){

        //if(!hasApiKey){ return 0; }

        /* TODO Add the Usable query field */
        /*
        HttpResponse putBadgeResponse
                = Unirest.put(Endpoint.BADGES.value)
                .body(new Badge(description, name))
                .asEmpty();

        return putBadgeResponse.getStatus();

         */
        return 0;
    }

    public int removeBadge(String name){
        /*
        if(!hasApiKey){ return 0; }

        HttpResponse removeBadgeResponse
                = Unirest.post(Endpoint.BADGES.value + "/" + name)
                .asEmpty();

        return removeBadgeResponse.getStatus();

         */
        return 0;
    }

}

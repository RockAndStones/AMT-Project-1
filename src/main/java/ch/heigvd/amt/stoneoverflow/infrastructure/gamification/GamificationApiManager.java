package ch.heigvd.amt.stoneoverflow.infrastructure.gamification;

import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

import java.util.List;


public class GamificationApiManager {
    private enum Endpoint {
        APPLICATIONS("applications"),
        BADGES("badges"),
        EVENTS("events"),
        POINTSCALES("pointscales"),
        RANKINGS("rankings"),
        RULES("rules"),
        USERS("users");


        public final String value;

        Endpoint(String value) {
            this.value = value;
        }
    }

    private boolean gotApiKey = false;

    public GamificationApiManager(String appName) {
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
    }

    public int createApplication(String name){
        HttpResponse createApplicationResponse
                = Unirest.post(Endpoint.APPLICATIONS.value)
                .body(new Application(name, null))
                .asEmpty();

        return createApplicationResponse.getStatus();
    }

    public Application getApplication(String name){
        HttpResponse<Application> applicationResponse
                = Unirest.get(Endpoint.APPLICATIONS.value + "/" + name)
                .asObject(Application.class);

        return applicationResponse.getBody();
    }

    public List<Badge> getBadges(){
        if(!gotApiKey){ return null; }

        HttpResponse<List<Badge>> badgesResponse =
                Unirest.get(Endpoint.BADGES.value)
                        .asObject(new GenericType<List<Badge>>() {});

        return badgesResponse.getBody();
    }

    public int createBadge(String description, String name){
        if(!gotApiKey){ return 0; }

        HttpResponse createBadgeResponse
                = Unirest.post(Endpoint.BADGES.value)
                .body(new Badge(description, name))
                .asEmpty();

        return createBadgeResponse.getStatus();
    }

    public Badge getBadge(String name){
        if(!gotApiKey){ return null; }

        HttpResponse<Badge> badgeResponse =
                Unirest.get(Endpoint.BADGES.value + "/" + name)
                        .asObject(Badge.class);

        return badgeResponse.getBody();
    }

    public int putBadge(String description, String name){
        if(!gotApiKey){ return 0; }

        /* TODO Add the Usable query field */
        HttpResponse putBadgeResponse
                = Unirest.put(Endpoint.BADGES.value)
                .body(new Badge(description, name))
                .asEmpty();

        return putBadgeResponse.getStatus();
    }

    public int removeBadge(String name){
        if(!gotApiKey){ return 0; }

        HttpResponse removeBadgeResponse
                = Unirest.post(Endpoint.BADGES.value + "/" + name)
                .asEmpty();

        return removeBadgeResponse.getStatus();
    }

}

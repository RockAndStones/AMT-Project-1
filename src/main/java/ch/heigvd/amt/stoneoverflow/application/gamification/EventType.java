package ch.heigvd.amt.stoneoverflow.application.gamification;

public enum EventType {
    NEW_QUESTION("newQuestion"),
    NEW_REPLY("newReply"),
    NEW_COMMENT("newComment"),
    NEW_VOTE("newVote"),
    REMOVE_VOTE("removeVote"),
    STONER_PROGRESS("stonerProgress"),
    STONER_REGRESS("stonerRegress");

    EventType(String eventName) {
        this.name = eventName;
    }

    public String name;
}

package ch.heigvd.amt.stoneoverflow.application.gamification;

public enum EventType {
    NEW_QUESTION("newQuestion"),
    NEW_REPLY("newReply"),
    NEW_COMMENT("newComment"),
    NEW_VOTE("newVote"),
    REMOVE_VOTE("removeVote"),
    NEW_STONER("newStoner");

    EventType(String eventName) {
        this.name = eventName;
    }

    public String name;
}

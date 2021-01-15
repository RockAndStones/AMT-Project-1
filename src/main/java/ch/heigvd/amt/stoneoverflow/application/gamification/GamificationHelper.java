package ch.heigvd.amt.stoneoverflow.application.gamification;

import ch.heigvd.amt.gamification.api.dto.*;

import java.time.OffsetDateTime;
import java.util.List;

public class GamificationHelper {
    /**
     * Constructor of Badge.
     * @param name        Badge's attribute.
     * @param description Badge's attribute.
     * @return an instance of Badge.
     */
    static Badge createBadge(String name, String description) {
        Badge badge = new Badge();
        badge.name(name);
        badge.description(description);
        return badge;
    }

    /**
     * Constructor of BadgeName.
     * @param name        BadgeName's attribute.
     * @return an instance of BadgeName.
     */
    static BadgeName createBadgeName(String name) {
        BadgeName badgeName = new BadgeName();
        badgeName.name(name);
        return badgeName;
    }

    /**
     * Constructor of Stage.
     * @param points Stage's attribute.
     * @param badge  Stage's attribute.
     * @return an instance of Stage.
     */
    static Stage createStage(Double points, BadgeName badge) {
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
    static PointScale createPointScale(String name, List<Stage> stages) {
        PointScale pointScale = new PointScale();
        pointScale.stages(stages);
        pointScale.name(name);
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
    static Rule createRule(String name, String description, String eventType, Double pointsToAdd, String badgeName, Integer pointScaleId) {
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
     * @return an instance of Event.
     */
    static Event createEvent(String userAppId, OffsetDateTime timestamp, String eventType) {
        Event event = new Event();
        event.userAppId(userAppId);
        event.timestamp(timestamp);
        event.eventType(eventType);
        return event;
    }
}

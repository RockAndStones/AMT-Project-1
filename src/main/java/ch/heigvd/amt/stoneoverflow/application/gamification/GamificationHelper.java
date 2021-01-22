package ch.heigvd.amt.stoneoverflow.application.gamification;

import ch.heigvd.amt.gamification.api.dto.*;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static ch.heigvd.amt.stoneoverflow.application.gamification.EventType.*;

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

    static Badge[] getAppInitQuestionBadges() {
        Badge[] questionBadges = new Badge[4];
        questionBadges[0] = GamificationHelper.createBadge("First question", "You asked your first question ! Congrats !");
        questionBadges[1] = GamificationHelper.createBadge("Pebble questionner", "We see you're getting used to ask questions. Keep going !");
        questionBadges[2] = GamificationHelper.createBadge("Rock questionner", "Almost a stonfessional in the asking game ?!");
        questionBadges[3] = GamificationHelper.createBadge("Mountain questionner", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

        return questionBadges;
    }

    static Badge[] getAppInitReplyBadges() {
        Badge[] replyBadges = new Badge[4];
        replyBadges[0] = GamificationHelper.createBadge("First reply", "You replied for the first time ! Congrats !");
        replyBadges[1] = GamificationHelper.createBadge("Earth replier", "We see you're getting used to reply questions. Keep going !");
        replyBadges[2] = GamificationHelper.createBadge("Cobblestone replier", "Almost a stonfessional in the replying game ?!");
        replyBadges[3] = GamificationHelper.createBadge("Mineral replier", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

        return replyBadges;
    }

    static Badge[] getAppInitCommentBadges() {
        Badge[] commentBadges = new Badge[4];
        commentBadges[0] = GamificationHelper.createBadge("First comment", "You wrote your first comment ! Congrats !");
        commentBadges[1] = GamificationHelper.createBadge("Sand commenter", "We see you're getting used to commenting. Keep going !");
        commentBadges[2] = GamificationHelper.createBadge("Gravel commenter", "Almost a stonfessional in the commenting game ?!");
        commentBadges[3] = GamificationHelper.createBadge("Crag commenter", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

        return commentBadges;
    }

    static Badge[] getAppInitVoteBadges() {
        Badge[] voteBadges = new Badge[4];
        voteBadges[0] = GamificationHelper.createBadge("First vote", "You voted for the first time ! Congrats !");
        voteBadges[1] = GamificationHelper.createBadge("Rubble voter", "We see you're getting used to voting. Keep going !");
        voteBadges[2] = GamificationHelper.createBadge("Boulder voter", "Almost a stonfessional in the voting game ?!");
        voteBadges[3] = GamificationHelper.createBadge("Peak voter", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

        return voteBadges;
    }

    static Badge[] getAppInitStonerBadges() {
        Badge[] stonerBadges = new Badge[4];
        stonerBadges[0] = GamificationHelper.createBadge("Newcomer", "Welcome to the StoneOverflow family !");
        stonerBadges[1] = GamificationHelper.createBadge("Rookie", "We see you're getting used to StoneOverflow. Keep going !");
        stonerBadges[2] = GamificationHelper.createBadge("Confirmed", "Almost a stonfessional in the StoneOverflow game ?!");
        stonerBadges[3] = GamificationHelper.createBadge("Veteran", "Management is proud of your efforts ! You're a true Stone member ! Rock and Stone !");

        return stonerBadges;
    }

    static BadgeName[] getBadgeNamesFromBadges(Badge[] badges) {
        BadgeName[] badgesName = new BadgeName[badges.length];
        for (int i = 0; i < badges.length; i++) {
            badgesName[i] = createBadgeName(badges[i].getName());
        }

        return badgesName;
    }


    static PointScale[] getAppInitPointScales(BadgeName[] questionBadgesName,
                                              BadgeName[] replyBadgesName,
                                              BadgeName[] commentBadgesName,
                                              BadgeName[] voteBadgesName,
                                              BadgeName[] stonerBadgesName) {
        // Create point scales
        PointScale questionPointScale = GamificationHelper.createPointScale("Question PointScale", Arrays.asList(
                GamificationHelper.createStage(1d, questionBadgesName[0]),
                GamificationHelper.createStage(5d, questionBadgesName[1]),
                GamificationHelper.createStage(10d, questionBadgesName[2]),
                GamificationHelper.createStage(20d, questionBadgesName[3])));

        PointScale replyPointScale = GamificationHelper.createPointScale("Reply PointScale", Arrays.asList(
                GamificationHelper.createStage(1d, replyBadgesName[0]),
                GamificationHelper.createStage(5d, replyBadgesName[1]),
                GamificationHelper.createStage(10d, replyBadgesName[2]),
                GamificationHelper.createStage(20d, replyBadgesName[3])));

        PointScale commentPointScale = GamificationHelper.createPointScale("Comment PointScale", Arrays.asList(
                GamificationHelper.createStage(1d, commentBadgesName[0]),
                GamificationHelper.createStage(5d, commentBadgesName[1]),
                GamificationHelper.createStage(10d, commentBadgesName[2]),
                GamificationHelper.createStage(20d, commentBadgesName[3])));

        PointScale votePointScale = GamificationHelper.createPointScale("Vote PointScale", Arrays.asList(
                GamificationHelper.createStage(1d, voteBadgesName[0]),
                GamificationHelper.createStage(5d, voteBadgesName[1]),
                GamificationHelper.createStage(10d, voteBadgesName[2]),
                GamificationHelper.createStage(20d, voteBadgesName[3])));

        PointScale stonerPointScale = GamificationHelper.createPointScale("Stoner PointScale", Arrays.asList(
                GamificationHelper.createStage(1d, stonerBadgesName[0]),
                GamificationHelper.createStage(25d, stonerBadgesName[1]),
                GamificationHelper.createStage(80d, stonerBadgesName[2]),
                GamificationHelper.createStage(120d, stonerBadgesName[3])));

        return new PointScale[] { questionPointScale, replyPointScale, commentPointScale, votePointScale, stonerPointScale };
    }

    static Rule[] getAppInitRules(String[] pointScaleIds) {
        // Create rules
        Rule newQuestionRule = GamificationHelper.createRule(NEW_QUESTION.name + GamificationFacade.RULE,
                "New question rule to apply when a user create a question.",
                NEW_QUESTION.name,
                1d,
                null,
                Integer.parseInt(pointScaleIds[0]));

        Rule newReplyRule = GamificationHelper.createRule(NEW_REPLY.name + GamificationFacade.RULE,
                "New reply rule to apply when a user respond to a question.",
                NEW_REPLY.name,
                1d,
                null,
                Integer.parseInt(pointScaleIds[1]));

        Rule newCommentRule = GamificationHelper.createRule(NEW_COMMENT.name + GamificationFacade.RULE,
                "New comment rule to apply when a user comment a question or a reply.",
                NEW_COMMENT.name,
                1d,
                null,
                Integer.parseInt(pointScaleIds[2]));

        Rule newVoteRule = GamificationHelper.createRule(NEW_VOTE.name + GamificationFacade.RULE,
                "New vote rule to apply when a user up/down vote a question or a reply.",
                NEW_VOTE.name,
                1d,
                null,
                Integer.parseInt(pointScaleIds[3]));

        Rule removeVoteRule = GamificationHelper.createRule(REMOVE_VOTE.name + GamificationFacade.RULE,
                "Remove vote rule to apply when a user remove his own vote from a question or a reply.",
                REMOVE_VOTE.name,
                -1d,
                null,
                Integer.parseInt(pointScaleIds[3]));

        Rule stonerProgressRule = GamificationHelper.createRule(STONER_PROGRESS.name + GamificationFacade.RULE,
                "Stoner progress rule to apply when a user progress in the stoner game.",
                STONER_PROGRESS.name,
                1d,
                null,
                Integer.parseInt(pointScaleIds[4]));

        Rule stonerRegressRule = GamificationHelper.createRule(STONER_REGRESS.name + GamificationFacade.RULE,
                "Stoner regress rule to apply when a user regress in the stoner game.",
                STONER_REGRESS.name,
                -1d,
                null,
                Integer.parseInt(pointScaleIds[4]));

        return new Rule[] { newQuestionRule, newReplyRule, newCommentRule, newVoteRule, removeVoteRule, stonerProgressRule, stonerRegressRule };
    }
}

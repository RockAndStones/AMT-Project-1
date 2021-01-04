package ch.heigvd.amt.stoneoverflow.application.vote;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.answer.AddAnswerCommand;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswersDTO;
import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.stoneoverflow.application.question.AddQuestionCommand;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
import ch.heigvd.amt.stoneoverflow.application.vote.AddVoteCommand;
import ch.heigvd.amt.stoneoverflow.application.vote.UpdateVoteCommand;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteDTO;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteFacade;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.comment.CommentId;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import ch.heigvd.amt.stoneoverflow.domain.vote.VoteId;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class VoteFacadeIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private AuthenticatedUserDTO testUser;
    private QuestionFacade       questionFacade;
    private AnswerFacade         answerFacade;
    private VoteFacade           voteFacade;


    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addAsDirectory("target/generated-sources/openapi")
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "ch.heigvd.amt.gamification")
                .addClass(ch.heigvd.amt.gamification.Configuration.class)
                .addPackages(true, "com.squareup.okhttp3")
                .addPackages(true, "com.google.gson")
                .addPackages(true, "io.gsonfire")
                .addPackages(true, "okhttp3")
                .addPackages(true, "okio")
                .addPackages(true, "org.springframework.security.crypto.bcrypt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt.BCrypt")
                .addAsResource(new File("src/main/resources/environment.properties"), "environment.properties");
        return archive;
    }

    @Before
    public void init() throws LoginFailedException  {

        this.questionFacade = serviceRegistry.getQuestionFacade();
        this.answerFacade   = serviceRegistry.getAnswerFacade();
        this.voteFacade     = serviceRegistry.getVoteFacade();

        this.testUser = serviceRegistry.getIdentityManagementFacade().login(LoginCommand.builder()
                .username("test")
                .plaintextPassword("test")
                .build());
    }

    private QuestionId addQuestion() {
        return questionFacade.addQuestion(AddQuestionCommand.builder()
                .creatorId(testUser.getId()).build());
    }

    private AnswerId addAnswer(QuestionId answerTo) {
        return answerFacade.addAnswer(AddAnswerCommand.builder()
                .answerTo(answerTo)
                .creatorId(testUser.getId()).build());
    }

    @Test
    public void shouldGetNumbersOfVotesOfAnswer() {
        QuestionId questionId1 = addQuestion();
        QuestionId questionId2 = addQuestion();

        int nbVotesBeforeQ1 = voteFacade.getNumberOfVotes(questionId1);
        int nbVotesBeforeQ2 = voteFacade.getNumberOfVotes(questionId2);
        voteFacade.addVote(AddVoteCommand.builder()
                .votedBy(testUser.getId())
                .votedObject(questionId1).build());
        voteFacade.addVote(AddVoteCommand.builder()
                .votedBy(testUser.getId())
                .votedObject(questionId2)
                .voteType(Vote.VoteType.DOWN).build());

       assertEquals(nbVotesBeforeQ1+1, voteFacade.getNumberOfVotes(questionId1));
       assertEquals(nbVotesBeforeQ2-1, voteFacade.getNumberOfVotes(questionId2));
    }

    @Test
    public void shouldGetNumbersOfVotesOfQuestion() {
        QuestionId questionId = addQuestion();
        AnswerId answerId1 = addAnswer(questionId);
        AnswerId answerId2 = addAnswer(questionId);

        int nbVotesBeforeQ1 = voteFacade.getNumberOfVotes(answerId1);
        int nbVotesBeforeQ2 = voteFacade.getNumberOfVotes(answerId2);
        voteFacade.addVote(AddVoteCommand.builder()
                .votedBy(testUser.getId())
                .votedObject(answerId1).build());
        voteFacade.addVote(AddVoteCommand.builder()
                .votedBy(testUser.getId())
                .votedObject(answerId2)
                .voteType(Vote.VoteType.DOWN).build());

        assertEquals(nbVotesBeforeQ1+1, voteFacade.getNumberOfVotes(answerId1));
        assertEquals(nbVotesBeforeQ2-1, voteFacade.getNumberOfVotes(answerId2));
    }

    @Test
    public void shouldGetVoteFromVotedObjectIdAndUserIdOrFromVoteId() {
        QuestionId questionId = addQuestion();

        voteFacade.addVote(AddVoteCommand.builder()
                .votedObject(questionId)
                .votedBy(testUser.getId()).build());

        VoteDTO voteDTO = voteFacade.getVote(questionId, testUser.getId(), UserMessageType.QUESTION);

        assertEquals(voteDTO, voteFacade.getVote(new VoteId(voteDTO.getUuid()), UserMessageType.QUESTION));
    }

    @Test
    public void shouldRemoveVote() {
        QuestionId questionId = addQuestion();

        int nbVotesBefore = voteFacade.getNumberOfVotes(questionId);
        voteFacade.addVote(AddVoteCommand.builder()
                .votedBy(testUser.getId())
                .votedObject(questionId).build());
        VoteId voteId = new VoteId(voteFacade.getVote(questionId, testUser.getId(), UserMessageType.QUESTION).getUuid());

        assertEquals(nbVotesBefore+1, voteFacade.getNumberOfVotes(questionId));
        voteFacade.remove(voteId);
        assertEquals(nbVotesBefore, voteFacade.getNumberOfVotes(questionId));
    }

    @Test
    public void shouldChangeVote() {
        QuestionId questionId = addQuestion();

        int nbVotesBefore = voteFacade.getNumberOfVotes(questionId);
        voteFacade.addVote(AddVoteCommand.builder()
                .votedBy(testUser.getId())
                .votedObject(questionId).build());
        VoteId voteId = new VoteId(voteFacade.getVote(questionId, testUser.getId(), UserMessageType.QUESTION).getUuid());

        assertEquals(nbVotesBefore+1, voteFacade.getNumberOfVotes(questionId));
        voteFacade.changeVote(UpdateVoteCommand.builder()
                .id(voteId)
                .votedBy(testUser.getId())
                .votedObject(questionId)
                .voteType(Vote.VoteType.DOWN).build());
        assertEquals(nbVotesBefore-1, voteFacade.getNumberOfVotes(questionId));
    }
}

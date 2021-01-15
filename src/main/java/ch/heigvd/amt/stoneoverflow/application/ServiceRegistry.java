package ch.heigvd.amt.stoneoverflow.application;

import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.application.pagination.PaginationFacade;
import ch.heigvd.amt.stoneoverflow.application.question.AddQuestionCommand;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.stoneoverflow.application.statistics.StatisticsFacade;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteFacade;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.IUserRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.domain.vote.IVoteRepository;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class ServiceRegistry {

    @Inject @Named("JdbcQuestionRepository")
    IQuestionRepository questionRepository;

    @Inject @Named("JdbcUserRepository")
    IUserRepository userRepository;

    @Inject @Named("JdbcAnswerRepository")
    IAnswerRepository answerRepository;

    @Inject @Named("JdbcCommentRepository")
    ICommentRepository commentRepository;

    @Inject @Named("JdbcVoteRepository")
    IVoteRepository voteRepository;

    @Getter GamificationFacade       gamificationFacade;
    @Getter IdentityManagementFacade identityManagementFacade;
    @Getter QuestionFacade           questionFacade;
    @Getter AnswerFacade             answerFacade;
    @Getter CommentFacade            commentFacade;
    @Getter VoteFacade               voteFacade;
    @Getter StatisticsFacade         statisticsFacade;
    @Getter PaginationFacade         paginationFacade;

    @PostConstruct
    private void initDefaultValues() {
        initFacades();
        if(userRepository.getRepositorySize() > 0) {
            return;
        }
        populateRepositories();
    }

    /**
     * Initialize facades.
     */
    private void initFacades() {
        identityManagementFacade = new IdentityManagementFacade(userRepository, gamificationFacade);
        questionFacade           = new QuestionFacade(questionRepository, gamificationFacade);
        answerFacade             = new AnswerFacade(answerRepository, gamificationFacade);
        commentFacade            = new CommentFacade(commentRepository, gamificationFacade);
        voteFacade               = new VoteFacade(voteRepository, gamificationFacade);
        statisticsFacade         = new StatisticsFacade(questionRepository, userRepository, commentRepository, answerRepository, voteRepository, questionFacade, voteFacade);
        paginationFacade         = new PaginationFacade(questionRepository, answerRepository);
        gamificationFacade       = new GamificationFacade(null);
    }

    /**
     * Populate StoneOverflow's repositories with default values.
     */
    private void populateRepositories() {
        // Add default users
        User u1 = User.builder()
                .username("test")
                .email("test@test.com")
                .firstName("John")
                .lastName("Smith")
                .plaintextPassword("test")
                .build();

        User u2 = User.builder()
                .username("rocky")
                .email("rocky@strongerthanyou.com")
                .firstName("Sylvester")
                .lastName("Stallone")
                .plaintextPassword("balboa")
                .build();

        User uE2e = User.builder()
                .username("e2eTester")
                .email("e2e@test.com")
                .firstName("John")
                .lastName("Smith")
                .plaintextPassword("Abcdef7!")
                .build();

        User uE2eVoter = User.builder()
                .username("e2eVoter")
                .email("e2eVoter@test.com")
                .firstName("Voter")
                .lastName("McVote")
                .plaintextPassword("Abcdef7!")
                .build();

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(uE2e); // e2e testing
        userRepository.save(uE2eVoter); // e2e testing

        // Add default questions
        questionFacade.addQuestion(AddQuestionCommand.builder()
                .title("Is it real life ??")
                .description("Well, you real ????")
                .creatorId(u1.getId())
                .creator(u1.getUsername())
                .nbViews(new AtomicInteger(44))
                .build());

        questionFacade.addQuestion(AddQuestionCommand.builder()
                .title("Do you even lift bro ?!")
                .description("Start lifting weights today, lift women tomorrow !")
                .creatorId(u1.getId())
                .creator(u1.getUsername())
                .nbViews(new AtomicInteger(40))
                .build());

        questionFacade.addQuestion(AddQuestionCommand.builder()
                .title("How can we pass parameters to an already built Vue JS app ?")
                .description("We have a Vue app that connects to a Web Service and get's some data. The web service URL is different, depending on the location we install the app on.\n" +
                        "\n" +
                        "I first thought of using .env files, but later I realized these files get injected into the minified .js files.\n" +
                        "\n" +
                        "Having this in my main.js was very convenient in the case of .env files:\n" +
                        "\n" +
                        "Vue.prototype.ApiBaseUrl = process.env.VUE_APP_APIBASEURL\n" +
                        "Vue.prototype.PrintDocsFolder = process.env.VUE_APP_PRINTDOCSFOLDER\n" +
                        "Vue.prototype.TicketPrintWSocket = process.env.VUE_APP_TICKETPRINTWSOCKET   \n" +
                        "\n" +
                        "The app is already built. I don't want to build the app for each of the hundred locations we have to deploy to. I'm not sure about the \"official\" approach for this.\n" +
                        "\n" +
                        "Is there any out of the box solution in Vue that can allow this configuration? Basically we need to have a file in the root folder of the built app, and read values for our Vue.prototype.VARIABLES.\n" +
                        "\n" +
                        "We are using vue-cli 3.")
                .creatorId(u1.getId())
                .creator(u1.getUsername())
                .nbViews(new AtomicInteger(44))
                .build());

        Question q1 = Question.builder()
                .title("Is there any herb to get your dog high like catnip?")
                .description("The question is all about the title :)")
                .creatorId(u2.getId())
                .creator(u2.getUsername())
                .nbViews(new AtomicInteger(0))
                .build();

        Question qE2e = Question.builder()
                .title("E2e testing question")
                .description("E2e testing question description")
                .creatorId(uE2e.getId())
                .creator(uE2e.getUsername())
                .nbViews(new AtomicInteger(0))
                .build();

        questionRepository.save(q1);
        questionRepository.save(qE2e); // e2e testing

        // Add default answers
        Answer a1 = Answer.builder()
                .answerTo(q1.getId())
                .description("Yes there is. It's called anise ;)")
                .creatorId(u1.getId())
                .creator(u1.getUsername()).build();

        Answer a2 = Answer.builder()
                .answerTo(q1.getId())
                .description("Is this questions a cake?")
                .creatorId(u1.getId())
                .creator("IAmALieBecauseIMayBeACakeInsideAndIAmScaredAboutThat").build();

        Answer aE2e = Answer.builder()
                .answerTo(qE2e.getId())
                .description("E2e testing answer")
                .creatorId(uE2e.getId())
                .creator(uE2e.getUsername()).build();

        answerRepository.save(a1);
        answerRepository.save(a2);
        answerRepository.save(aE2e);

        // Add default comments
        Comment c1 = Comment.builder()
                .commentTo(q1.getId())
                .description("Excellent question sir.")
                .creatorId(u1.getId())
                .creator(u1.getUsername()).build();

        Comment c2 = Comment.builder()
                .commentTo(a1.getId())
                .description("It's also called dog nip by the way.")
                .creatorId(u1.getId())
                .creator(u1.getUsername()).build();

        Comment c3 = Comment.builder()
                .commentTo(a1.getId())
                .description("Yeah it's anise, I've tried it with my dog. But since this event my dog stopped moving but it was fun I would say.")
                .creatorId(u2.getId())
                .creator(u2.getUsername()).build();

        Comment c4 = Comment.builder()
                .commentTo(a2.getId())
                .description("D*fuck is wrong with you buddy?")
                .creatorId(u2.getId())
                .creator(u2.getUsername()).build();

        Comment cE2e = Comment.builder()
                .commentTo(aE2e.getId())
                .description("E2e testing comment to answer")
                .creatorId(uE2e.getId())
                .creator(uE2e.getUsername()).build();

        Comment c2E2e = Comment.builder()
                .commentTo(qE2e.getId())
                .description("E2e testing comment to question")
                .creatorId(uE2e.getId())
                .creator(uE2e.getUsername()).build();

        commentRepository.save(c1);
        commentRepository.save(c2);
        commentRepository.save(c3);
        commentRepository.save(c4);
        commentRepository.save(cE2e); // e2e testing
        commentRepository.save(c2E2e); // e2e testing

        Vote v1 = Vote.builder()
                .votedBy(u1.getId())
                .votedObject(q1.getId())
                .voteType(Vote.VoteType.UP).build();

        Vote v2 = Vote.builder()
                .votedBy(u1.getId())
                .votedObject(a2.getId())
                .voteType(Vote.VoteType.DOWN).build();

        Vote v3 = Vote.builder()
                .votedBy(u2.getId())
                .votedObject(a2.getId())
                .voteType(Vote.VoteType.DOWN).build();

        Vote v4 = Vote.builder()
                .votedBy(u2.getId())
                .votedObject(a1.getId())
                .voteType(Vote.VoteType.UP).build();

        voteRepository.save(v1);
        voteRepository.save(v2);
        voteRepository.save(v3);
        voteRepository.save(v4);

        // Place the E2E question in the front page by giving it the biggest upvote count
        voteRepository.save(Vote.builder()
                .votedBy(u1.getId())
                .votedObject(qE2e.getId())
                .voteType(Vote.VoteType.UP).build());
        voteRepository.save(Vote.builder()
                .votedBy(u2.getId())
                .votedObject(qE2e.getId())
                .voteType(Vote.VoteType.UP).build());
        voteRepository.save(Vote.builder()
                .votedBy(uE2eVoter.getId())
                .votedObject(qE2e.getId())
                .voteType(Vote.VoteType.UP).build());
    }
}

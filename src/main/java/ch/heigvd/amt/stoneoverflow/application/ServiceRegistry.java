package ch.heigvd.amt.stoneoverflow.application;

import ch.heigvd.amt.stoneoverflow.application.answer.AddAnswerCommand;
import ch.heigvd.amt.stoneoverflow.application.comment.AddCommentCommand;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.application.history.HistoryFacade;
import ch.heigvd.amt.stoneoverflow.application.pagination.PaginationFacade;
import ch.heigvd.amt.stoneoverflow.application.question.AddQuestionCommand;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.stoneoverflow.application.statistics.StatisticsFacade;
import ch.heigvd.amt.stoneoverflow.application.vote.AddVoteCommand;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteFacade;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.comment.CommentId;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
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
    @Getter HistoryFacade            historyFacade;

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
        gamificationFacade       = new GamificationFacade(null);
        identityManagementFacade = new IdentityManagementFacade(userRepository, gamificationFacade);
        questionFacade           = new QuestionFacade(questionRepository, gamificationFacade);
        answerFacade             = new AnswerFacade(answerRepository, gamificationFacade);
        commentFacade            = new CommentFacade(commentRepository, gamificationFacade);
        voteFacade               = new VoteFacade(voteRepository, gamificationFacade);
        statisticsFacade         = new StatisticsFacade(questionRepository, userRepository, commentRepository, answerRepository, voteRepository, questionFacade, voteFacade, gamificationFacade);
        paginationFacade         = new PaginationFacade(questionRepository, answerRepository);
        historyFacade            = new HistoryFacade(gamificationFacade);
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

        QuestionId q1Id = questionFacade.addQuestion(AddQuestionCommand.builder()
                .title("Is there any herb to get your dog high like catnip?")
                .description("The question is all about the title :)")
                .creatorId(u2.getId())
                .creator(u2.getUsername())
                .nbViews(new AtomicInteger(0))
                .build());

        Question qE2e = Question.builder()
                .title("E2e testing question")
                .description("E2e testing question description")
                .creatorId(uE2e.getId())
                .creator(uE2e.getUsername())
                .nbViews(new AtomicInteger(0))
                .build();

        questionRepository.save(qE2e); // e2e testing

        // Add default answers
        AnswerId a1Id = answerFacade.addAnswer(AddAnswerCommand.builder()
                .answerTo(q1Id)
                .description("Yes there is. It's called anise ;)")
                .creatorId(u1.getId())
                .creator(u1.getUsername()).build());

        AnswerId a2Id = answerFacade.addAnswer(AddAnswerCommand.builder()
                .answerTo(q1Id)
                .description("Is this questions a cake?")
                .creatorId(u1.getId())
                .creator("IAmALieBecauseIMayBeACakeInsideAndIAmScaredAboutThat").build());

        Answer aE2e = Answer.builder()
                .answerTo(qE2e.getId())
                .description("E2e testing answer")
                .creatorId(uE2e.getId())
                .creator(uE2e.getUsername()).build();

        answerRepository.save(aE2e);

        // Add default comments
        commentFacade.addComment(AddCommentCommand.builder()
                .commentTo(q1Id)
                .content("Excellent question sir.")
                .creatorId(u1.getId())
                .creator(u1.getUsername()).build());

        commentFacade.addComment(AddCommentCommand.builder()
                .commentTo(a1Id)
                .content("It's also called dog nip by the way.")
                .creatorId(u1.getId())
                .creator(u1.getUsername()).build());

        commentFacade.addComment(AddCommentCommand.builder()
                .commentTo(a1Id)
                .content("Yeah it's anise, I've tried it with my dog. But since this event my dog stopped moving but it was fun I would say.")
                .creatorId(u2.getId())
                .creator(u2.getUsername()).build());

        commentFacade.addComment(AddCommentCommand.builder()
                .commentTo(a2Id)
                .content("D*fuck is wrong with you buddy?")
                .creatorId(u2.getId())
                .creator(u2.getUsername()).build());

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

        commentRepository.save(cE2e); // e2e testing
        commentRepository.save(c2E2e); // e2e testing

        voteFacade.addVote(AddVoteCommand.builder()
                .votedBy(u1.getId())
                .votedObject(q1Id)
                .voteType(Vote.VoteType.UP).build());

        voteFacade.addVote(AddVoteCommand.builder()
                .votedBy(u1.getId())
                .votedObject(a2Id)
                .voteType(Vote.VoteType.DOWN).build());

        voteFacade.addVote(AddVoteCommand.builder()
                .votedBy(u2.getId())
                .votedObject(a2Id)
                .voteType(Vote.VoteType.DOWN).build());

        voteFacade.addVote(AddVoteCommand.builder()
                .votedBy(u2.getId())
                .votedObject(a1Id)
                .voteType(Vote.VoteType.UP).build());

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

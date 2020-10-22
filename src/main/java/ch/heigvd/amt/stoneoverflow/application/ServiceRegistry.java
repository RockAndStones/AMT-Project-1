package ch.heigvd.amt.stoneoverflow.application;

import ch.heigvd.amt.stoneoverflow.application.pagination.PaginationFacade;
import ch.heigvd.amt.stoneoverflow.application.question.AddQuestionCommand;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerFacade;
import ch.heigvd.amt.stoneoverflow.application.comment.CommentFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.stoneoverflow.application.statistics.StatisticsFacade;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.comment.Comment;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.IUserRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class ServiceRegistry {
    @Inject @Named("InMemoryQuestionRepository")
    IQuestionRepository questionRepository;

    @Inject @Named("InMemoryUserRepository")
    IUserRepository userRepository;

    @Inject @Named("InMemoryAnswerRepository")
    IAnswerRepository answerRepository;

    @Inject @Named("InMemoryCommentRepository")
    ICommentRepository commentRepository;

    @Getter IdentityManagementFacade identityManagementFacade;
    @Getter QuestionFacade questionFacade;
    @Getter AnswerFacade  answerFacade;
    @Getter CommentFacade commentFacade;
    @Getter StatisticsFacade statisticsFacade;
    @Getter PaginationFacade paginationFacade;

    @PostConstruct
    private void initDefaultValues() {
        identityManagementFacade = new IdentityManagementFacade(userRepository);
        questionFacade           = new QuestionFacade(questionRepository);
        answerFacade             = new AnswerFacade(answerRepository);
        commentFacade            = new CommentFacade(commentRepository);
        statisticsFacade         = new StatisticsFacade(questionRepository, userRepository, commentRepository, answerRepository);
        paginationFacade         = new PaginationFacade(questionRepository);

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

        userRepository.save(u1);
        userRepository.save(u2);

        // Add default questions
        questionFacade.addQuestion(AddQuestionCommand.builder()
                .title("Is it real life ??")
                .description("Well, you real ????")
                .creatorId(u1.getId())
                .creator("SwagMan McSwagenstein")
                .nbVotes(2)
                .build());

        questionFacade.addQuestion(AddQuestionCommand.builder()
                .title("Do you even lift bro ?!")
                .description("Start lifting weights today, lift women tomorrow !")
                .creatorId(u1.getId())
                .creator("Ricardo")
                .nbVotes(1038)
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
                .creator("Jack Casas")
                .nbVotes(6)
                .build());

        Question q1 = Question.builder()
                .title("Is there any herb to get your dog high like catnip?")
                .description("The question is all about the title :)")
                .creatorId(u2.getId())
                .creator(u2.getUsername())
                .nbVotes(601).build();

        questionRepository.save(q1);

        // Add default answers
        Answer a1 = Answer.builder()
                .answerTo(q1.getId())
                .description("Yes there is. It's called anise ;)")
                .creatorId(u1.getId())
                .creator(u1.getUsername())
                .nbVotes(542).build();

        Answer a2 = Answer.builder()
                .answerTo(q1.getId())
                .description("Is this questions a cake?")
                .creatorId(u1.getId())
                .creator("IAmALieBecauseIMayBeACakeInsideAndIAmScaredAboutThat")
                .nbVotes(-4).build();

        answerRepository.save(a1);
        answerRepository.save(a2);

        // Add default comments
        Comment c1 = Comment.builder()
                .commentTo(q1.getId())
                .content("Excellent question sir.")
                .creatorId(new UserId())
                .creator("Anonymous1EvenIfYouCannotBeAnonymousInAComment").build();

        Comment c2 = Comment.builder()
                .commentTo(a1.getId())
                .content("It's also called dog nip by the way.")
                .creatorId(u1.getId())
                .creator(u1.getUsername()).build();

        Comment c3 = Comment.builder()
                .commentTo(a1.getId())
                .content("Yeah it's anise, I've tried it with my dog. But since this event my dog stopped moving but it was fun I would say.")
                .creatorId(new UserId())
                .creator("Anonymous2EvenIfYouCannotBeAnonymousInAComment").build();

        Comment c4 = Comment.builder()
                .commentTo(a2.getId())
                .content("D*fuck is wrong with you buddy?")
                .creatorId(new UserId())
                .creator("Anonymous3EvenIfYouCannotBeAnonymousInAComment").build();

        commentRepository.save(c1);
        commentRepository.save(c2);
        commentRepository.save(c3);
        commentRepository.save(c4);
    }
}

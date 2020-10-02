package ch.heigvd.amt.stoneoverflow.application;

import ch.heigvd.amt.stoneoverflow.application.question.AddQuestionCommand;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegisterCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegistrationFailedException;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.IUserRepository;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryQuestionRepository;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryUserRepository;

public class ServiceRegistry {
    private static ServiceRegistry singleton;

    private static IQuestionRepository questionRepository;
    private static IUserRepository userRepository;
    private static QuestionFacade questionFacade;
    private static IdentityManagementFacade identityManagementFacade;

    public static ServiceRegistry getServiceRegistry(){
        if (singleton == null){
            singleton = new ServiceRegistry();
        }
        return singleton;
    }

    private ServiceRegistry(){
        singleton = this;
        questionRepository = new InMemoryQuestionRepository();
        userRepository = new InMemoryUserRepository();
        questionFacade = new QuestionFacade(questionRepository);
        identityManagementFacade = new IdentityManagementFacade(userRepository);

        initializeDefaultValues();
    }

    private void initializeDefaultValues() {
        //Create default account
        try {
            identityManagementFacade.register(RegisterCommand.builder()
                    .username("test")
                    .email("test@test.com")
                    .firstName("John")
                    .lastName("Smith")
                    .plaintextPassword("test")
                    .build());
        } catch (RegistrationFailedException e) {
            e.printStackTrace();
        }

        //Add default questions
        questionFacade.addQuestion(AddQuestionCommand.builder()
                .title("Is it real life ??")
                .description("Well, you real ????")
                .creator("SwagMan McSwagenstein")
                .nbVotes(2)
                .build());

        questionFacade.addQuestion(AddQuestionCommand.builder()
                .title("Do you even lift bro ?!")
                .description("Start lifting weights today, lift women tomorrow !")
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
                .creator("Jack Casas")
                .nbVotes(6)
                .build());
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }

    public IdentityManagementFacade getIdentityManagementFacade() {
        return identityManagementFacade;
    }
}

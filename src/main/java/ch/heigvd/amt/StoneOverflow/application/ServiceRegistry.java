package ch.heigvd.amt.StoneOverflow.application;

import ch.heigvd.amt.StoneOverflow.application.Question.QuestionFacade;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.StoneOverflow.domain.Question.IQuestionRepository;
import ch.heigvd.amt.StoneOverflow.domain.user.IUserRepository;
import ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory.InMemoryQuestionRepository;
import ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory.InMemoryUserRepository;

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
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }

    public IdentityManagementFacade getIdentityManagementFacade() {
        return identityManagementFacade;
    }
}

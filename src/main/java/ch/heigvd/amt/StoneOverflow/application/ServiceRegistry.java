package ch.heigvd.amt.StoneOverflow.application;

import ch.heigvd.amt.StoneOverflow.application.Question.QuestionFacade;
import ch.heigvd.amt.StoneOverflow.domain.Question.IQuestionRepository;
import ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory.InMemoryQuestionRepository;

public class ServiceRegistry {
    private static ServiceRegistry singleton;

    private static IQuestionRepository questionRepository;
    private static QuestionFacade questionFacade;

    public static ServiceRegistry getServiceRegistry(){
        if (singleton == null){
            singleton = new ServiceRegistry();
        }
        return singleton;
    }

    private ServiceRegistry(){
        singleton = this;
        questionRepository = new InMemoryQuestionRepository();
        questionFacade = new QuestionFacade(questionRepository);
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }
}

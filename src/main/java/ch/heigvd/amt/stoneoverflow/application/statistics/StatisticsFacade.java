package ch.heigvd.amt.stoneoverflow.application.statistics;

import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.IUserRepository;

public class StatisticsFacade {
    private final IQuestionRepository questionRepository;
    private final IUserRepository userRepository;
    private final ICommentRepository commentRepository;
    private final IAnswerRepository answerRepository;

    public StatisticsFacade(IQuestionRepository questionRepository, IUserRepository userRepository, ICommentRepository commentRepository, IAnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.answerRepository = answerRepository;
    }

    public StatisticsDTO getGlobalStatistics() {
        return StatisticsDTO.builder()
                .nbQuestions(questionRepository.getRepositorySize())
                .nbUsers(userRepository.getRepositorySize())
                .nbViews(0)
                .build();
    }
}

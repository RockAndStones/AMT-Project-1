package ch.heigvd.amt.stoneoverflow.application.statistics;

import ch.heigvd.amt.stoneoverflow.application.question.QuestionFacade;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuerySortBy;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO;
import ch.heigvd.amt.stoneoverflow.application.vote.VoteFacade;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.comment.ICommentRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.IUserRepository;
import ch.heigvd.amt.stoneoverflow.domain.vote.IVoteRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;


public class StatisticsFacade {
    private final IQuestionRepository questionRepository;
    private final IUserRepository userRepository;
    private final ICommentRepository commentRepository;
    private final IAnswerRepository answerRepository;
    private final IVoteRepository voteRepository;
    private final QuestionFacade questionFacade;
    private final VoteFacade voteFacade;

    public StatisticsFacade(IQuestionRepository questionRepository, IUserRepository userRepository, ICommentRepository commentRepository, IAnswerRepository answerRepository, IVoteRepository voteRepository, QuestionFacade questionFacade, VoteFacade voteFacade) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.answerRepository = answerRepository;
        this.voteRepository = voteRepository;
        this.questionFacade = questionFacade;
        this.voteFacade = voteFacade;
    }

    int getTotalViews() {
        return questionRepository.findAll().stream().mapToInt(Question::getNbViewsAsInt).sum();
    }

    int getTotalVotes() {
        return questionRepository.findAll().stream()
                .mapToInt(q -> voteRepository.findNbVotes(q.getId(), UserMessageType.QUESTION))
                .sum();
    }

    QuestionsDTO getMostPopularQuestions(int limit) {
        return questionFacade.getQuestions(QuestionQuery.builder()
                .sortBy(QuestionQuerySortBy.VOTES)
                .build(),
                0, limit);
    }

    Collection<UserStatisticsDTO> getMostActiveUsers(int limit) {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(u -> questionRepository.findByUser(u.getId()).size(), Comparator.reverseOrder()))
                .limit(limit)
                .map(q -> UserStatisticsDTO.builder()
                        .id(q.getId())
                        .username(q.getUsername())
                        .nbQuestions(questionRepository.findByUser(q.getId()).size())
                        .build())
                .collect(Collectors.toList());
    }

    public StatisticsDTO getGlobalStatistics() {

        QuestionsDTO popularQuestions = getMostPopularQuestions(5);
        popularQuestions.getQuestions()
                .forEach(q -> q.setNbVotes(voteFacade.getNumberOfVotes(new QuestionId(q.getUuid()))));

        return StatisticsDTO.builder()
                .nbQuestions(questionRepository.getRepositorySize())
                .nbUsers(userRepository.getRepositorySize())
                .nbViews(getTotalViews())
                .nbVotes(getTotalVotes())
                .mostActiveUsers(getMostActiveUsers(3))
                .mostVotedQuestions(popularQuestions)
                .build();
    }
}

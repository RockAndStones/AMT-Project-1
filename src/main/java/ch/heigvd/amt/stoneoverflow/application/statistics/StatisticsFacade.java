package ch.heigvd.amt.stoneoverflow.application.statistics;

import ch.heigvd.amt.gamification.api.dto.*;
import ch.heigvd.amt.stoneoverflow.application.gamification.EventType;
import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
import ch.heigvd.amt.stoneoverflow.application.pagination.PaginationDTO;
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
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.domain.vote.IVoteRepository;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class StatisticsFacade {
    private final IQuestionRepository questionRepository;
    private final IUserRepository userRepository;
    private final ICommentRepository commentRepository;
    private final IAnswerRepository answerRepository;
    private final IVoteRepository voteRepository;
    private final QuestionFacade questionFacade;
    private final VoteFacade voteFacade;
    private final GamificationFacade gamificationFacade;

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

    private PaginationDTO getPaginationDTO(Pagination pagination, int pageSize) {
        int totalPages = (int) Math.ceil((double) pagination.getNumberOfItems() / (double) pageSize);
        int currentPage = pagination.getPage();
        int startItem = currentPage * pageSize;
        int lastItem = Math.min(startItem + pageSize, pagination.getNumberOfItems().intValue());

        return PaginationDTO.builder()
                .limit(pageSize)
                .currentPage(pagination.getPage() + 1)
                .itemRepoSize(pagination.getNumberOfItems().intValue())
                .startPage(1)
                .lastPage(totalPages)
                .startItem(startItem)
                .lastItem(lastItem)
                .totalPages(totalPages)
                .build();
    }

    private PointsRankingsDTO getPointsRankingsDTO(int pageSize, PaginatedPointsRankings pointsRankings) {
        if (pointsRankings == null || pointsRankings.getData() == null || pointsRankings.getPagination() == null)
            return PointsRankingsDTO.builder()
                    .rankings(new LinkedList<>())
                    .build();

        List<PointsRankingsDTO.PointsRankingDTO> rankings = new LinkedList<>();
        for (PointsRanking rank : pointsRankings.getData()) {
            Optional<User> u = userRepository.findById(new UserId(rank.getUserId()));
            if (u.isPresent() && rank.getPoints() != null) {
                rankings.add(PointsRankingsDTO.PointsRankingDTO.builder()
                        .points(rank.getPoints())
                        .username(u.get().getUsername())
                        .build());
            }
        }

        return PointsRankingsDTO.builder()
                .rankings(rankings)
                .pagination(getPaginationDTO(pointsRankings.getPagination(), pageSize))
                .build();
    }

    public PointsRankingsDTO getPointsRankings(int page, int pageSize) {
        PaginatedPointsRankings pointsRankings = gamificationFacade.getPointsRankings(page, pageSize);
        return getPointsRankingsDTO(pageSize, pointsRankings);
    }

    public PointsRankingsDTO getPointsQuestionsRankings(EventType eventType, int page, int pageSize) {
        PaginatedPointsRankings pointsRankings = gamificationFacade.getPointsRankings(eventType, page, pageSize);
        return getPointsRankingsDTO(pageSize, pointsRankings);
    }

    public BadgesRankingsDTO getBadgesRankings(int page, int pageSize) {
        PaginatedBadgesRankings badgesRankings = gamificationFacade.getBadgesRankings(page, pageSize);
        if (badgesRankings == null || badgesRankings.getData() == null || badgesRankings.getPagination() == null)
            return BadgesRankingsDTO.builder()
                    .rankings(new LinkedList<>())
                    .build();

        List<BadgesRankingsDTO.BadgesRankingDTO> rankings = new LinkedList<>();
        for (BadgesRanking rank : badgesRankings.getData()) {
            Optional<User> u = userRepository.findById(new UserId(rank.getUserId()));
            if (u.isPresent() && rank.getBadges() != null) {
                rankings.add(BadgesRankingsDTO.BadgesRankingDTO.builder()
                        .badges(rank.getBadges())
                        .username(u.get().getUsername())
                        .build());
            }
        }

        return BadgesRankingsDTO.builder()
                .rankings(rankings)
                .pagination(getPaginationDTO(badgesRankings.getPagination(), pageSize))
                .build();
    }
}

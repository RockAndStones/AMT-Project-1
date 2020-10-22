package ch.heigvd.amt.stoneoverflow.application.pagination;

import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;

public class PaginationFacade {
    private final IQuestionRepository questionRepository;

    public PaginationFacade(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public PaginationDTO settingPagination(String paramPage, String records_limit) {
        int limit = records_limit != null ? Integer.parseInt(records_limit) : 5;
        int allQuestions = questionRepository.getRepositorySize();
        int totalPages = (int) Math.ceil((double) allQuestions / (double) limit);
        int page = paramPage != null && Integer.parseInt(paramPage) <= totalPages ?
                Integer.parseInt(paramPage) : 1;
        int startQuestion = (page - 1) * limit;

        return PaginationDTO.builder()
                .limit(limit)
                .allQuestions(allQuestions)
                .totalPages(totalPages)
                .currentPage(page)
                .lastQuestion(startQuestion + limit)
                .startQuestion(startQuestion).build();
    }
}

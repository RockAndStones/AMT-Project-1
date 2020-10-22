package ch.heigvd.amt.stoneoverflow.application.pagination;

import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;

public class PaginationFacade {
    private final IQuestionRepository questionRepository;
    private static final int QUESTION_PER_PAGE = 5;
    private static final int START_PAGE = 1;
    private static final int SHOWED_PAGES = 2;

    public PaginationFacade(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public PaginationDTO settingPagination(String paramPage, String records_limit) {
        int limit = records_limit != null ? Integer.parseInt(records_limit) : QUESTION_PER_PAGE;
        int allQuestions = questionRepository.getRepositorySize();
        int totalPages = (int) Math.ceil((double) allQuestions / (double) limit);
        int currentPage = paramPage != null && Integer.parseInt(paramPage) <= totalPages ?
                Integer.parseInt(paramPage) : START_PAGE;
        int startQuestion = (currentPage - 1) * limit;

        int lastQuestion = Math.min(startQuestion + limit, allQuestions);

        return PaginationDTO.builder()
                .limit(limit)
                .allQuestions(allQuestions)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .startQuestion(startQuestion + 1)
                .lastQuestion(lastQuestion)
                .startPage(getStartingPage(currentPage, totalPages))
                .lastPage(getLastPage(currentPage, totalPages))
                .build();
    }

    private int getStartingPage(int currentPage, int totalPages){
        // Cannot have a page below 1
        int startingPage = Math.max(currentPage - SHOWED_PAGES, START_PAGE);
        if(startingPage >= totalPages){
            startingPage = totalPages;
        }
        return startingPage;
    }

    private int getLastPage(int currentPage, int totalPages){
        // Cannot have a page higher than the last page
        int startingPage = Math.min(currentPage + SHOWED_PAGES, totalPages);
        if(startingPage <= START_PAGE){
            startingPage = START_PAGE;
        }
        return startingPage;
    }
}

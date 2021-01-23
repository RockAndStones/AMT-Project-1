package ch.heigvd.amt.stoneoverflow.application.pagination;

import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;

public class PaginationFacade {

    private final IQuestionRepository questionRepository;
    private final IAnswerRepository answerRepository;

    // Number of question showed per page
    private static final int QUESTION_PER_PAGE = 10;
    // Number of answer showed per page
    private static final int ANSWER_PER_PAGE = 10;
    // The starting page
    private static final int START_PAGE = 1;
    // Number of pages showed after and before the current page
    private static final int SHOWED_PAGES = 2;
    // Number of pages showed at the beginning and end of the list of pages
    private static final int NB_INIT_PAGE_SHOWED = 4;

    public PaginationFacade(IQuestionRepository questionRepository, IAnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository   = answerRepository;
    }

    public static PaginationDTO settingPagination(int itemPerPage, int itemRepoSize, int page) {

        int totalPages = (int) Math.ceil((double) itemRepoSize / (double) itemPerPage);
        int currentPage = page <= totalPages ? page : START_PAGE;
        int startItem = (currentPage - 1) * itemPerPage;
        int lastItem = Math.min(startItem + itemPerPage, itemRepoSize);

        return PaginationDTO.builder()
                .limit(itemPerPage)
                .itemRepoSize(itemRepoSize)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .startItem(startItem)
                .lastItem(lastItem)
                .startPage(getStartingPage(currentPage, totalPages))
                .lastPage(getLastPage(currentPage, totalPages))
                .build();
    }

    private int pageFromString(String paramPage) {
        int page = START_PAGE;
        if (paramPage != null)
            page = Integer.parseInt(paramPage);

        return page;
    }

    public PaginationDTO settingQuestionPagination(String paramPage) {
        return settingPagination(QUESTION_PER_PAGE, questionRepository.getRepositorySize(), pageFromString(paramPage));
    }

    public PaginationDTO settingAnswerPagination(String paramPage, int nbAnswers) {
        return settingPagination(ANSWER_PER_PAGE, nbAnswers, pageFromString(paramPage));
    }

    private static int getStartingPage(int currentPage, int totalPages){
        // Cannot have a page below 1
        int startingPage = Math.max(currentPage - SHOWED_PAGES, START_PAGE);
        if(startingPage >= totalPages){
            startingPage = totalPages;
        }
        // Show only the first five page before moving the pagination design
        if(currentPage >= START_PAGE && currentPage < START_PAGE + NB_INIT_PAGE_SHOWED){
            startingPage = START_PAGE;
        } else if(currentPage > totalPages - NB_INIT_PAGE_SHOWED && currentPage <= totalPages){
            startingPage = totalPages - NB_INIT_PAGE_SHOWED;
        }

        return startingPage;
    }

    private static int getLastPage(int currentPage, int totalPages){
        // Cannot have a page higher than the last page
        int lastPage = Math.min(currentPage + SHOWED_PAGES, totalPages);
        if(lastPage <= START_PAGE){
            lastPage = START_PAGE;
        }
        // Show only the first five page before moving the pagination design
        if(currentPage < START_PAGE + NB_INIT_PAGE_SHOWED){
            lastPage = Math.min(START_PAGE + NB_INIT_PAGE_SHOWED, totalPages);
        } else if(currentPage > totalPages - NB_INIT_PAGE_SHOWED && currentPage <= totalPages){
            lastPage = totalPages;
        }
        return lastPage;
    }
}

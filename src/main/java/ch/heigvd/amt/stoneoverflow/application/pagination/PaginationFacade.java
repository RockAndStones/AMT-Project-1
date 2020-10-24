package ch.heigvd.amt.stoneoverflow.application.pagination;

import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.answer.IAnswerRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.IQuestionRepository;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;

public class PaginationFacade {

    private final IQuestionRepository questionRepository;
    private final IAnswerRepository answerRepository;

    // Number of question showed per page
    private static final int QUESTION_PER_PAGE = 5;
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

    private PaginationDTO settingPagination(int itemPerPage, int itemRepoSize, String paramPage) {

        int totalPages = (int) Math.ceil((double) itemRepoSize / (double) itemPerPage);
        int currentPage = paramPage != null && Integer.parseInt(paramPage) <= totalPages ?
                Integer.parseInt(paramPage) : START_PAGE;
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

    public PaginationDTO settingQuestionPagination(String paramPage) {
        /*
        int allQuestions = questionRepository.getRepositorySize();
        int totalPages = (int) Math.ceil((double) allQuestions / (double) QUESTION_PER_PAGE);
        int currentPage = paramPage != null && Integer.parseInt(paramPage) <= totalPages ?
                Integer.parseInt(paramPage) : START_PAGE;
        int startQuestion = (currentPage - 1) * QUESTION_PER_PAGE;
        int lastQuestion = Math.min(startQuestion + QUESTION_PER_PAGE, allQuestions);

        return PaginationDTO.builder()
                .limit(QUESTION_PER_PAGE)
                .itemRepoSize(allQuestions)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .startItem(startQuestion)
                .lastItem(lastQuestion)
                .startPage(getStartingPage(currentPage, totalPages))
                .lastPage(getLastPage(currentPage, totalPages))
                .build();
        */

        return settingPagination(QUESTION_PER_PAGE, questionRepository.getRepositorySize(), paramPage);
    }

    public PaginationDTO settingAnswerPagination(String paramPage, int nbAnswers) {
        return settingPagination(ANSWER_PER_PAGE, nbAnswers, paramPage);
    }

    private int getStartingPage(int currentPage, int totalPages){
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

    private int getLastPage(int currentPage, int totalPages){
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

package ch.heigvd.amt.stoneoverflow.application.question;

import lombok.Getter;

public enum QuestionQuerySortBy {
    DATE("date"),
    VOTES("nbVotes"),
    VIEWS("nbViews");

    @Getter
    private final String sqlFieldName;

    QuestionQuerySortBy(String sqlFieldName) {
        this.sqlFieldName = sqlFieldName;
    }
}

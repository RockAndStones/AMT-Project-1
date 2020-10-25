package ch.heigvd.amt.stoneoverflow.application.answer;

import lombok.Getter;

public enum AnswerQuerySortBy {
    DATE("date"),
    VOTES("nbVotes");

    @Getter
    private final String sqlFieldName;

    AnswerQuerySortBy(String sqlFieldName) {
        this.sqlFieldName = sqlFieldName;
    }
}

package ch.heigvd.amt.stoneoverflow.application.comment;

import lombok.Getter;

public enum CommentQuerySortBy {
    DATE("date");

    @Getter
    private final String sqlFieldName;

    CommentQuerySortBy(String sqlFieldName) {
        this.sqlFieldName = sqlFieldName;
    }
}

package ch.heigvd.amt.stoneoverflow.infrastructure.gamification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Pagination {
    private int numberOfItems;
    private int page;
    private String previous;
    private String next;
}

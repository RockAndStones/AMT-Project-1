package ch.heigvd.amt.StoneOverflow.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Question {
    String title;
    String description;
    String creator;
    int nbVotes;
}

package ch.heigvd.amt.StoneOverflow.application.answer;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class AnswersDTO {

    @Builder
    @Getter
    @EqualsAndHashCode
    public static class AnswerDTO {
        private String description;
        private String creator;
        private int    nbVotes;
        private Date   date;
    }

    @Singular
    private List<AnswerDTO> answers;
}

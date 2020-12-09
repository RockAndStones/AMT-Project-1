package ch.heigvd.amt.stoneoverflow.infrastructure.gamification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

@AllArgsConstructor
@Getter
public class Application {
    private String name;

    @JsonInclude(Include.NON_NULL)
    private String apiKey;
}


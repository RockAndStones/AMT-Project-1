package ch.heigvd.amt.StoneOverflow.domain.Question;

import ch.heigvd.amt.StoneOverflow.domain.Id;

import java.util.UUID;

public class QuestionId extends Id {

    public QuestionId(){
        super();
    }

    public QuestionId(String id){
        super(id);
    }

    public QuestionId(UUID id){
        super(id);
    }
}

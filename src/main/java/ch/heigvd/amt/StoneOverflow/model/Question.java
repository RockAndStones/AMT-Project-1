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

    public String shortDescription(){
        int maxLength = 64;
        String thereIsmore = "...";

        if(this.description.length() > maxLength){
            String shortDescription = this.description.substring(0, maxLength);
            shortDescription += thereIsmore;
            return shortDescription;
        } else{
            return this.description;
        }



    }
}

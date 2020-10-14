package ch.heigvd.amt.stoneoverflow.application.date;

import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;
import java.util.Date;

@EqualsAndHashCode
public class DateDTO extends Date {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd ''yy"); // '' = single quote
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public DateDTO(Date date) {
        super(date.getTime());
    }

    public String dateFormatted() {
        return dateFormat.format(this);
    }

    public String timeFormatted() {
        return timeFormat.format(this);
    }
}

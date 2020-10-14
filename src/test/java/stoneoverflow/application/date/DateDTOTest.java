package stoneoverflow.application.date;

import ch.heigvd.amt.stoneoverflow.application.date.DateDTO;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateDTOTest {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd ''yy"); // '' = single quote
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    @Test
    public void shouldCreateADateDTOFromDateObject() {
        Date date = new Date(System.currentTimeMillis());
        DateDTO dateDTO = new DateDTO(date);
        assertEquals(date.getTime(), dateDTO.getTime());
    }

    @Test
    public void shouldSendDateFormatted() {
        Date date = new Date(System.currentTimeMillis());
        DateDTO dateDTO = new DateDTO(date);
        assertEquals(dateFormat.format(date), dateDTO.dateFormatted());
    }

    @Test
    public void shouldSendTimeFormatted() {
        Date date = new Date(System.currentTimeMillis());
        DateDTO dateDTO = new DateDTO(date);
        assertEquals(timeFormat.format(date), dateDTO.timeFormatted());
    }
}

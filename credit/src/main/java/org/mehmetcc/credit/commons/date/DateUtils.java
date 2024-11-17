package org.mehmetcc.credit.commons.date;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DateUtils {
    public static LocalDateTime calculateNextThirtyDays(LocalDateTime time, Integer multiply) {
        return checkIfWeekend(LocalDateTime.now().plusDays(30 * multiply));
    }

    public static LocalDateTime checkIfWeekend(LocalDateTime time) {
        var day = time.getDayOfWeek();

        if (day == DayOfWeek.SATURDAY) return time.plusDays(2);
        else if (day == DayOfWeek.SUNDAY) return time.plusDays(1);
        else return time;
    }
}

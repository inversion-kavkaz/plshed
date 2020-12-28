package ru.inversion.plshed.utils;

import java.time.LocalDateTime;

/**
 * @author Dmitry Hvastunov
 * @created 21 Декабрь 2020 - 18:22
 * @project plshed
 */

public class DateUtils {

    public static LocalDateTime getNextDate(LocalDateTime dateTime, Long period, Long interval) {
        interval = (interval != null || interval == 0) ? interval : 1;
        LocalDateTime nextStart = dateTime;

        while (nextStart.isBefore(LocalDateTime.now())) {
            switch (period.intValue()) {
                case 0:
                    nextStart = nextStart.plusDays(1 * interval);
                    break;
                case 1:
                    nextStart = nextStart.plusHours(1 * interval);
                    break;
                case 2:
                    nextStart = nextStart.plusMinutes(1 * interval);
                    break;
            }
//            System.out.println("nextStart = " + nextStart);
        }
        return nextStart;
    }
}

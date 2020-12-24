package ru.inversion.plshed.utils;

import java.time.LocalDateTime;

/**
 * @author Dmitry Hvastunov
 * @created 21 Декабрь 2020 - 18:22
 * @project plshed
 */

public class DateUtils {

    public static LocalDateTime getNextDate(LocalDateTime dateTime, Long period, Long interval) {
        interval = interval != null ? interval : 1;
        LocalDateTime nextStart = dateTime;
        if (dateTime.isBefore(LocalDateTime.now())) {
            switch (period.intValue()) {
                case 0:
                    nextStart = dateTime.plusDays(1 * interval);
                    break;
                case 1:
                    nextStart = dateTime.plusHours(1 * interval);
                    break;
                case 2:
                    nextStart = dateTime.plusMinutes(1 * interval);
                    break;
            }
            nextStart = getNextDate(nextStart, period, interval);
        }
        return nextStart;
    }
}

package ru.inversion.plshed.utils;

import ru.inversion.dataset.XXIDataSet;

import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

/**
 * @author Dmitry Hvastunov
 * @created 16 Декабрь 2020 - 12:22
 * @project plshed
 */

public class dataSetUtils {

    public static <T> Stream<T> dataSetToStream(@NotNull XXIDataSet<T> dataSet) {
        return dataSet.getRows().stream();
    }

}

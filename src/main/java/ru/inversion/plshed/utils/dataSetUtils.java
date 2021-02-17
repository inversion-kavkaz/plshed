package ru.inversion.plshed.utils;

import javafx.scene.paint.Color;
import lovUtils.LovInterface;
import ru.inversion.dataset.DataSetException;
import ru.inversion.dataset.SQLDataSet;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.fx.form.controls.JInvTableColumn;
import ru.inversion.fx.form.controls.renderer.Colorizer;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.tc.TaskContext;

import javax.validation.constraints.NotNull;
import java.util.Optional;
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

    public static <K extends LovInterface, T,C> void convertTableValue(JInvTableColumn<T, C> lovTableColumn,
                                                                       Class<K> lovClass,
                                                                       TaskContext taskContext,
                                                                       Boolean setFilter
    ) throws DataSetException {
            SQLDataSet<K> lovDataSet = new SQLDataSet<>(taskContext, lovClass);
            lovDataSet.execute();

        if (setFilter)
            lovTableColumn.setLovClassName(lovDataSet.getRowClass().getCanonicalName());

        lovTableColumn.setCellRenderer((cell, o) -> {
            Optional<LovInterface> lovValue = (Optional<LovInterface>) lovDataSet.getRows().stream().filter(k -> k.getKey() == o).findAny();
            cell.setText(lovValue.isPresent() ? lovValue.get().getValue().toString() : String.valueOf(o));
            cell.addColor(c -> (c.getPojo() != null && ((PIkpTasks)c.getPojo()).getFTASKRUN() == 1) ? new Colorizer(null, Color.DARKGREEN, Colorizer.TextStyle.BOLD) : null);
        });
    }

}

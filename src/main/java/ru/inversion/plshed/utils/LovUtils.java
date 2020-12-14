package ru.inversion.plshed.utils;

import ru.inversion.bicomp.stringconverter.DataSetStringConverter;
import ru.inversion.dataset.DataSetException;
import ru.inversion.dataset.SQLDataSet;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.fx.form.controls.JInvComboBox;
import ru.inversion.fx.form.controls.JInvTableColumn;
import ru.inversion.fx.form.controls.renderer.JInvTableCell;
import ru.inversion.fx.form.lov.JInvEntityLov;
import ru.inversion.tc.TaskContext;

import java.util.Optional;

/**
 * @author Dmitry Hvastunov
 * @created 14 Декабрь 2020 - 10:02
 * @project plshed
 */

public class LovUtils {

    /**
     * Метод заменяет данные в ячейках основной таблицы на данные лов таблицы
     * лов таблица должна быть имплемнтировать LovInterface из утилит
     * lovTableColumn - колонка таблицы которую необходимо заменить
     * lovDataSet - датасет лов таблицы
     * lovClass - Класс лов таблицы
     * taskContext - Контекст приложения
     * setFilter - Добавляет лов класс в фильтр тоже
     * возвращает - void
     */
    public static <K extends LovInterface, T,C> void convertTableValue(JInvTableColumn<T, C> lovTableColumn,
                                                                       //XXIDataSet<K> lovDataSet,
                                                                       Class<K> lovClass,
                                                                       TaskContext taskContext,
                                                                       Boolean setFilter
    ) throws DataSetException {
        SQLDataSet<K> lovDataSet = new SQLDataSet<>(taskContext, lovClass);
        lovDataSet.execute();

//        lovDataSet.setTaskContext(taskContext);
//        lovDataSet.setRowClass(lovClass);
//        lovDataSet.executeQuery();

        if (setFilter)
            lovTableColumn.setLovClassName(lovDataSet.getRowClass().getCanonicalName());

        lovTableColumn.setCellRenderer((cell, o) -> {
            Optional<LovInterface> lovValue = (Optional<LovInterface>) lovDataSet.getRows().stream().filter(k -> k.getKey() == o).findAny();
            if (lovValue.isPresent())
                cell.setText(lovValue.get().getValue().toString());
        });
    }


    public static <T extends LovInterface, K> JInvComboBox initCombobox(TaskContext taskContext,
                                                                        JInvComboBox<K, String> comboBox,
                                                                        Class<T> entityClass
    ) throws DataSetException {
        SQLDataSet<T> sqlDataSet = new SQLDataSet<>(taskContext, entityClass);
        sqlDataSet.execute();
        DataSetStringConverter<T, K> sc = new DataSetStringConverter<>(sqlDataSet, t -> (K) t.getKey(), t -> (String) t.getValue());
        comboBox.setConverter(sc);
        comboBox.getItems().addAll(sc.keySet());
        return comboBox;
    }

}

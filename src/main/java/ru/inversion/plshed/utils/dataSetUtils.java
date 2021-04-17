package ru.inversion.plshed.utils;

import javafx.scene.paint.Color;
import lovUtils.LovInterface;
import org.slf4j.Logger;
import ru.inversion.dataset.DataSetException;
import ru.inversion.dataset.SQLDataSet;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.fx.form.controls.JInvTableColumn;
import ru.inversion.fx.form.controls.renderer.Colorizer;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.tc.TaskContext;

import javax.tools.*;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static ru.inversion.plshed.model.CompileSourceInMemory.checkCodeResult;

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

    public static Class getClassFromString(String classString, String className, Logger logger) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        if(compiler == null) {
            Class<?> javacTool = null;
            try {
                javacTool = Class.forName("com.sun.tools.javac.api.JavacTool");
                Method create = javacTool.getMethod("create");
                compiler = (JavaCompiler) create.invoke(null);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if(compiler == null)
            return null;
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();


        JavaFileObject file = new JavaSourceFromString(String.format(className), classString);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);
        boolean success = task.call();
        for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
            checkCodeResult += diagnostic + "\n";
        }
        if(success)
            checkCodeResult = "Success: " + success;
        else
            return null;

        logger.error(checkCodeResult);

        MyClassLoader loader = new MyClassLoader();
        return loader.getClassFromFile(new File(String.format("%s.class",className)));
    }

    static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:/" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    static class MyClassLoader extends ClassLoader {

        public Class getClassFromFile(File f) {
            byte[] raw = new byte[(int) f.length()];
            InputStream in = null;
            try {
                in = new FileInputStream(f);
                in.read(raw);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return defineClass(null, raw, 0, raw.length);
        }
    }


}

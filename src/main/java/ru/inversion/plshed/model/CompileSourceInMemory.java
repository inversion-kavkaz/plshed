package ru.inversion.plshed.model;

import org.slf4j.Logger;
import ru.inversion.fx.form.ViewContext;
import ru.inversion.tc.TaskContext;

import javax.tools.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

//import javax.tools.*;

/**
 * @author Dmitry Hvastunov
 * @created 30 Декабрь 2020 - 9:00
 * @project plshed
 */

public class CompileSourceInMemory {

    private static Logger logger;
    private static Connection connection;
    public static String checkCodeResult ;
    public static ViewContext viewContext;
    public static TaskContext taskContext;



    public static Object runCode(List<String> codeList, Object preEventResult, Logger log,Connection con, ViewContext vc, TaskContext tc) throws IOException {
        logger = log;
        connection = con;
        viewContext = vc;
        taskContext = tc;

        boolean success = testCode(codeList);

        if (success)
            return startTask(preEventResult);
        return null;
    }

    private static boolean testCode(List<String> codeList) {
        checkCodeResult = "";
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        if(compiler == null) {
            Class<?> javacTool = null;
            try {
                javacTool = Class.forName("com.sun.tools.javac.api.JavacTool");
                Method create = javacTool.getMethod("create");
                compiler = (JavaCompiler) create.invoke(null);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if(compiler == null){
            logger.error("compiler is null");
            checkCodeResult = "compiler is null";
            return false;
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StringBuilder builder = new StringBuilder();
        codeList.forEach(s -> builder.append(s).append("\n"));
        logger.debug(builder.toString());
        JavaFileObject file = new JavaSourceFromString("CustomClass", builder.toString());
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

        boolean success = task.call();
        for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
            checkCodeResult += diagnostic + "\n";
        }
        if(success)
            checkCodeResult = "Success: " + success;

        logger.error(checkCodeResult);
        return success;
    }

    private static Object startTask(Object preEventResult) {
        try {
            MyClassLoader loader = new MyClassLoader();
            Class my = loader.getClassFromFile(new File("CustomClass.class"));
            Method m = my.getMethod("CustomFunction", Object.class, Connection.class, ViewContext.class, TaskContext.class);
            Object o = my.newInstance();
            return m.invoke(o, preEventResult,connection,viewContext, taskContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            logger.info(String.valueOf(f.length()));
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

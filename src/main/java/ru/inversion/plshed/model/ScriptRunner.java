package ru.inversion.plshed.model;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import ru.inversion.fx.form.ViewContext;
import ru.inversion.plshed.entity.PIkpTaskEvents;
import ru.inversion.tc.TaskContext;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dmitry Hvastunov
 * @created 29 Декабрь 2020 - 14:02
 * @project plshed
 */

@Getter
@Setter
public class ScriptRunner {

    private final Logger logger;
    private final String codeString;
    private final PIkpTaskEvents event;
    private final Object preEventResult;
    private final Connection connection;
    public final ViewContext viewContext;
    public final TaskContext taskContext;

    public String checkCodeResult ;




    public ScriptRunner(Logger logger, String codeString, PIkpTaskEvents event, Object preEventResult, Connection connection, ViewContext viewContext, TaskContext taskContext) {
        this.logger = logger;
        this.codeString = codeString;
        this.event = event;
        this.preEventResult = preEventResult;
        this.connection = connection;
        this.viewContext = viewContext;
        this.taskContext = taskContext;
    }


    public Object startScript() {
        Object result = null;

        List<String> classCodeStringList = wrapInClass(convertStringToListCode(codeString));
        try {
            result = CompileSourceInMemory.runCode(classCodeStringList,preEventResult,logger,connection,viewContext,taskContext);
            checkCodeResult = CompileSourceInMemory.checkCodeResult;
        } catch (IOException e) {
            logger.error(String.format(" Run cjde error: %s", e));
        }
        return result;
    }

    private List<String> wrapInClass(List<String> codeStringList) {
        List<String> classCodeStringList = new ArrayList<>();
        classCodeStringList.add(addImports());
        classCodeStringList.add("public class CustomClass {");
        classCodeStringList.add("public static Object CustomFunction(Object eventresult, " +
                "Connection connection, " +
                "ViewContext viewContext," +
                "TaskContext taskContext) {");
        classCodeStringList.add("Integer eventnpp = " + event.getIEVENTNPP() + ";");
        classCodeStringList.add("Integer eventid = " + event.getIEVENTID() + ";");

        classCodeStringList.addAll(codeStringList);
        classCodeStringList.add("}");

        classCodeStringList.add(addWaitFunction());
        classCodeStringList.add(addIsFileExistFunction());
        classCodeStringList.add(addCallSqlFunc());
        classCodeStringList.add(addStartExAppFunction());

        classCodeStringList.add("}");
        return classCodeStringList;
    }

    private List<String> convertStringToListCode(String code) {
        return Arrays.asList(code
                .split("[\\n]"))
                .stream()
                .filter(p -> !p.isEmpty() && !p.startsWith("//"))
                .collect(Collectors.toList());
    }

    private String addWaitFunction(){
        return "        private static void wait(int mlsec){\n" +
                "            try {\n" +
                "                Thread.sleep(mlsec);\n" +
                "            } catch (InterruptedException e) {\n" +
                "                e.printStackTrace();\n" +
                "            }\n" +
                "        }\n";
    }

    private String addIsFileExistFunction(){
        return "        private static Boolean isFileExist(String fileName){\n" +
                "            return Files.exists(Paths.get(fileName));\n" +
                "        }\n";
    }

    private String addStartExAppFunction(){
        return "            private static int StartProcess(String prefix, String appPath, @NotNull String appName, boolean isWait ){\n" +
                "\n" +
                "            try {\n" +
                "                String path = (prefix.isEmpty() ? \"\" : prefix).concat(appPath).concat(File.separator).concat(appName);\n" +
                "                System.out.println(String.format(\"path: %s\",path));\n" +
                "                Process process = Runtime.getRuntime().exec(path);\n" +
                "                if(isWait)\n" +
                "                    return process.waitFor();\n" +
                "\n" +
                "            } catch (IOException | InterruptedException e) {\n" +
                "                e.printStackTrace();\n" +
                "                return -100;\n" +
                "            }\n" +
                "            return 0;\n" +
                "        }";
    }

    private String addImports(){
        return  "import java.nio.file.Files;\n" +
                "import java.nio.file.Paths;\n" +
                "import java.io.File;\n" +

                "import ru.inversion.fx.form.ViewContext;\n" +
                "import ru.inversion.tc.TaskContext;\n" +

                "import java.sql.Connection;\n" +
                "import java.sql.PreparedStatement;\n" +
                "import java.sql.ResultSet;\n" +
                "import java.sql.SQLException;\n" +

                "import java.io.IOException;\n" +
                "import java.lang.reflect.InvocationTargetException;\n" +
                "import java.lang.reflect.Method;\n" +
                "import java.util.Map;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.List;\n" +
                "import javax.validation.constraints.NotNull;\n" +

                "import ru.inversion.utils.ConnectionStringFormatEnum;\n" +
                "import java.net.URL;\n" +
                "import java.net.URLClassLoader;\n" +
                "import java.lang.reflect.InvocationTargetException;\n" +
                "import java.lang.reflect.Method;\n" +
                "import java.net.MalformedURLException;\n" +
                "import ru.inversion.bicomp.util.ParamMap;\n";
    }

    private String addCallSqlFunc(){
        return "        private static Object CallSqlFunc(Connection connection,String funcName, Object... args) {\n" +
                "            PreparedStatement ps = null;\n" +
                "            ResultSet rs = null;\n" +
                "            StringBuilder stringBuilder = new StringBuilder();\n" +
                "            stringBuilder.append(\"select \").append(funcName).append(\"(\");\n" +
                "            if(args != null)\n" +
                "            for (Object arg : args)\n" +
                "                stringBuilder.append(arg);\n" +
                "            stringBuilder.append(\")\").append(\" from dual\");\n" +
                "\n" +
                "            try {\n" +
                "                rs = connection.createStatement().executeQuery(stringBuilder.toString());\n" +
                "                if (rs.next())\n" +
                "                    return rs.getObject(1);\n" +
                "\n" +
                "            } catch (SQLException e) {\n" +
                "                return null;\n" +
                "            } finally {\n" +
                "                try {\n" +
                "                    if (ps != null) ps.close();\n" +
                "                    if (rs != null) rs.close();\n" +
                "                } catch (SQLException e) {\n" +
                "                    e.printStackTrace();\n" +
                "                }\n" +
                "            }\n" +
                "            return null;\n" +
                "        }\n";
    }
}


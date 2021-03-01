package ru.inversion.plshed.model;

import ru.inversion.fx.form.ViewContext;
import ru.inversion.tc.TaskContext;
import ru.inversion.utils.ConnectionStringFormatEnum;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Dmitry Hvastunov
 * @created 30 Декабрь 2020 - 10:45
 * @project plshed
 */

public class TestClass {

    public static Connection connection ;
    public static ViewContext viewContext;
    public static TaskContext taskContext;


    public static class CustomClass {

        public static String CustomFunction(String preResult) {


            try {
                sendRequest();
            } catch (ClassNotFoundException
                    | InvocationTargetException
                    | IllegalAccessException
                    | NoSuchMethodException
                    | InstantiationException
                    | MalformedURLException e) {
                e.printStackTrace();
            }
//
//            StartProcess("",
//                    "c:\\Program Files\\Notepad++",
//                    "notepad++.exe",
//                    false);
//
//            StartProcess("",
//                    "c:\\Program Files\\Microsoft Office\\Office16",
//                    "WINWORD.EXE",
//                    false);


//            runInversionModule("");

            return "";
        }

        private static void wait(int mlsec) {
            try {
                Thread.sleep(mlsec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private static Boolean isFileExist(String fileName) {
            return Files.exists(Paths.get(fileName));
        }

        private static Object CallSqlFunc(Connection connection, String funcName, Object... args) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("select ").append(funcName).append("(");
            if(args != null)
            for (Object arg : args)
                stringBuilder.append(arg);
            stringBuilder.append(")").append(" from dual");

            try {
                rs = connection.createStatement().executeQuery(stringBuilder.toString());
                if (rs.next())
                    return rs.getObject(1);

            } catch (SQLException e) {
                return null;
            } finally {
                try {
                    if (ps != null) ps.close();
                    if (rs != null) rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private static int StartProcess(String prefix, String appPath, @NotNull String appName, boolean isWait ){

            try {
                String path = (prefix.isEmpty() ? "" : prefix).concat(appPath).concat(File.separator).concat(appName);
                System.out.println(String.format("path: %s",path));
                Process process = Runtime.getRuntime().exec(path);
                if(isWait)
                    return process.waitFor();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return -100;
            }
            return 0;
        }

        private static void runInversionModule(String moduleName){
            try {
                Class aClass = Class.forName("ru.inversion.fxacc.als.PAlsMain");
                Method method = aClass.getDeclaredMethod("showViewAls", ViewContext.class, TaskContext.class,Map.class);
                method.invoke(null,viewContext,taskContext,null);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static Object sendRequest() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, MalformedURLException {
        String connectionString = taskContext.getConnectionString(ConnectionStringFormatEnum.SQL_PLUS);
        File file = new File("c://JAPP_603//citypayverifier-1.0-SNAPSHOT-jar-with-dependencies.jar");
        URLClassLoader child = new URLClassLoader(new URL[]{file.toURI().toURL()}, ClassLoader.getSystemClassLoader());
        Class classToLoad = Class.forName("ru.inversionkavkaz.citypayverifier.Main", true, child);

        Method method = classToLoad.getDeclaredMethod("send", Map.class);
        Object instance  = classToLoad.newInstance();
        Map<String, String> programOptions = new HashMap();

        programOptions.put("o",connectionString);
        programOptions.put("c", "VRF");
        programOptions.put("s", "20190101235959");
        programOptions.put("f","20201118235959");
        programOptions.put("t","555");
        programOptions.put("d","X"); //не выгружать отчет, только сформировать
        String result = (String) method.invoke(instance, programOptions);
        System.out.println(String.format("result: %s", result));
        if (result != null) {
            //Ошибка внешнего процесса
            return -1;
        }

        return 0;
    }
}
/**
    Class<ru.inversionkavkaz.citypayverifier.Main> clazz = (Class<Main>) Class.forName("ru.inversionkavkaz.citypayverifier.Main");
    String runString = "-o xxi/NEW8I@odb12 -c VRF -s 20190101235959 -f 20201118235959 -u https://crb2.donapex.net/cgi-bin/dayreport.cgi -t 777";
    String[] runArgs = runString.split(" ");
    Main.main(runArgs);
 */
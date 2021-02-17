package ru.inversion.plshed.model;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Dmitry Hvastunov
 * @created 30 Декабрь 2020 - 10:45
 * @project plshed
 */

public class TestClass {

    public static Connection connection ;

    public static class CustomClass {

        public static String CustomFunction(String preResult) {
            String result = "OK";
            String fileName = "C:\\PL\\TR\\IN\\TR.dbf";
            int counter = 10;

            System.out.println(String.format("fileName =  %s", fileName));

            while(!isFileExist(fileName) && counter-- > 0 ) {
                System.out.println(String.format("counter =  %d", counter));
                wait(20000);
            }

            return isFileExist(fileName) ? result :  "StopTask";

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


    }
}

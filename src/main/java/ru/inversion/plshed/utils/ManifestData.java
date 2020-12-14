package ru.inversion.plshed.utils;

import ru.inversion.fx.app.BaseApp;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Dmitry Hvastunov
 * @created 14 Декабрь 2020 - 17:02
 * @project plshed
 */


public class ManifestData {

    /**
     * Получение номера версии
     * Очень странный способ! кто знает как лучьше сделать сделайте
     * возвращает мапу с двумя ключами
     * version - номер версии третьим параметром идет номер сборки генерится мавеном автоматически
     * date - дата сборки
     * принимает имя приложения
     * */
    public static Map<String,String> loadDataFromManifestFile(String appName) {

        try {
            String appId = appName + ".jar";
            String markedString = "Implementation-Build";

            File pto = new File(ManifestData.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String a_path = pto.getAbsolutePath();
            a_path = a_path.substring(0,a_path.lastIndexOf("\\") + 1);

            String s = readManifest(a_path + appId);
            int markedStringAssets = s.indexOf(markedString) + markedString.length();
            String implementationString = s.substring(markedStringAssets + 1 , s.indexOf("\n",markedStringAssets) - 1);
            String[] strings = implementationString.split("/");
            if(strings != null){
                return new HashMap<String, String>(){{
                    put("version", strings[0] != null ? strings[0].trim(): "");
                    put("date", strings[1] != null ? strings[1].trim(): "");
                }};

            }
            int i = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private  static String readManifest(String sourceJARFile) throws IOException
    {
        ZipFile zipFile = new ZipFile(sourceJARFile);
        Enumeration entries = zipFile.entries();

        while (entries.hasMoreElements())
        {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            if (zipEntry.getName().equals("META-INF/MANIFEST.MF"))
            {
                return toString(zipFile.getInputStream(zipEntry));
            }
        }

        throw new IllegalStateException("Manifest not found");
    }

    private static String toString(InputStream inputStream) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)))
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
        }

        return stringBuilder.toString().trim() + System.lineSeparator();
    }
}

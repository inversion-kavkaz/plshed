package ru.inversion.plshed.utils;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.fx.form.JInvFXBrowserController;
import ru.inversion.plshed.PLShedMain;
import ru.inversion.tc.TaskContext;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Dmitry Hvastunov
 * @created 16 Декабрь 2020 - 12:22
 * @project plshed
 */

public class dataSetUtils {

    public static <T> Stream<T> dataSetToStream(@NotNull XXIDataSet<T> dataSet){
        return dataSet.getRows().stream();
    }

    public static void runDI(TaskContext context){
//        Reflections scaner = new Reflections("");
//        Set set = scaner.getTypesAnnotatedWith(javax.persistence.Entity.class);
//        Set<Class<? extends JInvFXBrowserController>> classList = scaner.getSubTypesOf(JInvFXBrowserController.class);
//        classList.forEach(a -> {
//            Arrays.stream(a.getDeclaredFields())
//                    .filter(p ->
//                            p.getType().getName().equalsIgnoreCase("ru.inversion.dataset.XXIDataSet")
//                    )
//                    .forEach( v -> {
////                        String genricTypeName = v.getGenericType().getTypeName();
////                        String ns = genricTypeName.substring(genricTypeName.indexOf("<") + 1,genricTypeName.indexOf(">"));
////                        System.out.println(String.format("name:%s  type: %s",v.getName(),ns));
//                        try {
//                            Class<?> entityClass = Class.forName("ru.inversion.plshed.entity.PIkpTaskEvents");//Class.forName(ns);
//                            XXIDataSet dataSet = new XXIDataSet(context, entityClass);
//                            JInvFXBrowserController aa = a.newInstance();
//                            v.setAccessible(true);
//                            v.set(aa,dataSet);
//                        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
//                            e.printStackTrace();
//                        }
//                    });
//
//                });
//        int  i = 0;


    }


}

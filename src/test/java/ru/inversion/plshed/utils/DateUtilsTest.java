package ru.inversion.plshed.utils;


import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DateUtilsTest {

 @Test
 public void streamTest(){
  Map<Integer,String> map = new HashMap<Integer,String>(){{
   put(1,"One");
   put(2,"Two");
   put(3,"Three");
   put(4,"Four");
   put(5,"Five");
  }};


  Stream stream = map.entrySet().stream().flatMap(a -> Stream.of(a.getKey(),a.getValue()));
         stream.forEach(System.out::println);

  IntStream intStream = IntStream.generate(() -> {return 100;});

  Stream intStream1 = Stream.of(100,200,300,400,500);
  intStream1.flatMap(a -> Stream.of(a,((int)a + 50))).forEach(System.out::println);

  System.out.println(String.join("|", "erwer", "rweterer", "645464"));

 }

 }

package ru.inversion.plshed.utils;


import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
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

 @Test
 public void stringTest(){
  List<String> list = new ArrayList<String>(){{
   add("1");add("2");add("3");add("4");add("5");add("6");add("7");add("8");
  }};

  list.forEach(System.out::print);
 System.out.println("");
 System.out.println(String.format("========================================================="));
 System.out.println(String.format("joining: %s",list.stream().collect(Collectors.joining(",","(",")"))));
 System.out.println(String.format("========================================================="));
 System.out.println(String.format("count: %s",list.stream().mapToInt(Integer::valueOf).count()));
 System.out.println(String.format("========================================================="));
 System.out.println(String.format("average: %s",list.stream().mapToInt(Integer::valueOf).average()));
 System.out.println(String.format("========================================================="));

 list.forEach(System.out::println);
 }




 }

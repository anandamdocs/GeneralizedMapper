package com.amdocs.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Map Merger with Java Stream
 * @author ANANDRA
 *
 */

public class GeneralizedMapperMain {

	// Map of String and Integer
	private static Map<String, Integer> map1 = new HashMap<String, Integer>();
	private static Map<String, Integer> map2 = new HashMap<String, Integer>();

	// Map of Integer and String
	private static Map<Integer, String> map3 = new HashMap<Integer, String>();
	private static Map<Integer, String> map4 = new HashMap<Integer, String>();
	
	// Map of String and Integer	
	
	private static List<Map<String, Integer>> list1 = new ArrayList<Map<String, Integer>>();
	private static List<Map<String, Integer>> list2 = new ArrayList<Map<String, Integer>>();
	

	public static void main(String[] args) {
		testMapIntString();
		testMapStringIntAddition();
		testMapStringIntMultiplication();
		testListOfMapStringIntMultiplication();
		testListOfMapStringIntAddition();

	}
	
	private static void testMapIntString(){
		map3.put(1, "Gendry ");
		map3.put(2, "Jhon");
		map4.put(2, "Snow");
		map4.put(3, "Petyr");

		eval(Stream.of(map3), Stream.of(map4), "+");
	}
	
	private static void testMapStringIntAddition(){
		map1.put("John", 20);
		map1.put("Tyron", 30);
		map1.put("Sandor", 30);
		map2.put("John", 40);
		map2.put("Cersei", 50);;

		eval(Stream.of(map1), Stream.of(map2), "+");
	}
	
	private static void testMapStringIntMultiplication(){
		map1.put("John", 20);
		map1.put("Tyron", 30);
		map1.put("Sandor", 30);
		map2.put("John", 40);
		map2.put("Cersei", 50);;

		eval(Stream.of(map1), Stream.of(map2), "*");
	}
	
	private static void testListOfMapStringIntMultiplication(){
		list1.add(map1);
		list2.add(map2);

		evalListOfMap(list1.stream(),list2.stream(),"*");
	}
	
	private static void testListOfMapStringIntAddition(){
		list1.add(map1);
		list2.add(map2);
		
		evalListOfMap(list1.stream(),list2.stream(),"+");

	}
	

	private static void eval(Stream<Map<?, ?>> paramStream1, Stream<Map<?, ?>> paramStream2,
			String operator) {
		validateStream(paramStream1, paramStream2);

		ConcurrentMap<Object, Object> sumMap = Stream.concat(paramStream1, paramStream2).parallel()
				.flatMap(m -> m.entrySet().stream())
				.collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
					if (k instanceof Integer) {
						if (operator.equals("*")) {
							return (int) k * (int) v;
						} else {
							return (int) k + (int) v;
						}
					} else if (k instanceof String) {
						return (String) k + (String) v;
					}
					return k;
				}));

		printMap(operator, sumMap);

	}

	private static void validateStream(Stream<Map<?, ? >> paramStream1, Stream<Map<?, ?>> paramStream2) {
		// Object pass must not be null
		Objects.requireNonNull(paramStream1);
		Objects.requireNonNull(paramStream2);
	}

	private static void printMap(String operator, ConcurrentMap<Object, Object> sumMap) {
		sumMap.forEach((k, v) -> {
			System.out.println("Key is " + k + " value is  " + v + " Operator is " + operator);
		});
	}

	private static  void evalListOfMap(Stream<Map<String,Integer>> p1,Stream<Map<String,Integer>> p2,String operator) {
		ConcurrentMap<Object, Object> sumMap = Stream.concat(p1, p2)
				.flatMap(m -> m.entrySet().stream())
				.collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> {
					if (k instanceof Integer) {
						if (operator.equals("*")) {
							return (int) k * (int) v;
						} else {
							return (int) k + (int) v;
						}
					} else if (k instanceof String) {
						return (String) k + (String) v;
					}
					return k;
				}));
		printMap(operator, sumMap);

		
	}

}

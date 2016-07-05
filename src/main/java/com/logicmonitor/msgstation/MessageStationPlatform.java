package com.logicmonitor.msgstation;

import java.util.Arrays;
import java.util.List;

public class MessageStationPlatform {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * StreamWrapper<Integer> filter = new StreamFilterWrapper<>(n -> {
		 * System.out.println("filter:" + n); return n % 2 == 0; });
		 * StreamWrapper<Integer> mapper = new StreamMapWrapper<>(n -> {
		 * System.out.println("map:" + n); return "map to :" + n; });
		 * StreamWrapper<String> consum = new
		 * StreamConsumerWrapper(System.out::println);
		 * filter.setNext(mapper).setNext(consum);
		 */

		MessageStream<Integer> mStream = new MessageStream<>();

		mStream.filter(n -> {
			System.out.println("filter:" + n);
			return n % 2 == 0;
		}).map(n -> {
			System.out.println("map:" + n);
			return "map to :" + n;
		}).forEach(System.out::println);

		/*
		 * mStream.map(n -> { System.out.println("map:" + n); return "map to :"
		 * + n; }).filter((String n) -> { System.out.println("filter:" + n);
		 * return n.length() > 0; }).forEach(System.out::println);
		 */
		List<Integer> values = Arrays.asList(0, 1, 2, 3);
		mStream.accept(values.stream());
		System.out.println("======");
		mStream.accept(values.stream());
	}
}

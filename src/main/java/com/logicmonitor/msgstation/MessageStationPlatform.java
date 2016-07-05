package com.logicmonitor.msgstation;

public class MessageStationPlatform {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		MessageStation messageStation = new MessageStation();

		MessageStream<Integer> mStream = new MessageStream<>();
		mStream.filter(n -> {
			System.out.println("int filter:" + n);
			return n % 2 == 0;
		}).map(n -> {
			System.out.println("int map:" + n);
			return "int map to :" + n;
		}).forEach(System.out::println);
		messageStation.createTopic("Integer", mStream);

		MessageStream<Long> lStream = new MessageStream<>();
		lStream.filter(n -> {
			System.out.println("long filter:" + n);
			return n % 2 == 0;
		}).map(n -> {
			System.out.println("long map:" + n);
			return "long map to :" + n;
		}).forEach(System.out::println);
		messageStation.createTopic("Long", lStream);

		MessageStream<String> sStream = new MessageStream<>();
		sStream.filter(n -> {
			System.out.println("string filter:" + n);
			return n.startsWith("test");
		}).forEach(System.out::println);
		messageStation.createTopic("String", sStream);

		for (int i = 0; i < 100; i++) {
			System.out.println("==================");
			messageStation.publish("Integer", i);
			messageStation.publish("Long", Long.valueOf(i));
			messageStation.publish("String", "test:" + i);
			Thread.sleep(100);
		}
	}
}

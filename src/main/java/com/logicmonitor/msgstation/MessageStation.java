package com.logicmonitor.msgstation;

import java.nio.ReadOnlyBufferException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rbtq on 7/5/16.
 */
public class MessageStation {
	private Map<String, MessagePublisher> MessagePublishers = new ConcurrentHashMap<>();

	public <T> void createTopic(final String topic, MessageStream<T> messageStream) {
		if (!MessagePublishers.containsKey(topic)) {
			MessagePublishers.put(topic, new LinkedMessageQueue<>(messageStream));
		} else {
			throw new ReadOnlyBufferException();
		}
	}

	public <T> void publish(final String topic, T message) throws InterruptedException {
		if (MessagePublishers.containsKey(topic)) {
			MessagePublishers.get(topic).publish(message);
		} else {
			throw new IllegalArgumentException(String.format("no such topic:%s", topic));
		}
	}
}

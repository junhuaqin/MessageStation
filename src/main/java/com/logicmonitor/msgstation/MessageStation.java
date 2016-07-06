package com.logicmonitor.msgstation;

import java.nio.ReadOnlyBufferException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbtq on 7/5/16.
 */
public class MessageStation {
	private Map<String, MessageQueueReceiver> MessageQueues = new HashMap<>();

	public <T> void createTopic(final String topic, MessageStream<T> messageStream) {
		if (!MessageQueues.containsKey(topic)) {
			MessageQueues.put(topic, new LinkedMessageQueue<>(messageStream));
		} else {
			throw new ReadOnlyBufferException();
		}
	}

	public <T> boolean publish(final String topic, T message) {
		if (MessageQueues.containsKey(topic)) {
			return MessageQueues.get(topic).offer(message);
		} else {
			return false;
		}
	}
}

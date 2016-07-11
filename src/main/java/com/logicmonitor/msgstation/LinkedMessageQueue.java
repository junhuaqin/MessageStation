package com.logicmonitor.msgstation;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by rbtq on 7/5/16.
 */
class LinkedMessageQueue<T> implements MessagePublisher {

	private static final int PROCESS_PERIOD = 1; // run once every minute.
	private LinkedBlockingQueue<T> queue;
	private MessageStream<T> messageStream;
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	protected LinkedMessageQueue(MessageStream<T> messageStream) {
		this.messageStream = Objects.requireNonNull(messageStream);
		this.queue = new LinkedBlockingQueue<>();
		scheduler.scheduleAtFixedRate(() -> processMessages(), PROCESS_PERIOD, PROCESS_PERIOD, TimeUnit.SECONDS);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void publish(Object obj) throws InterruptedException {
		queue.put((T) obj);
	}

	private List<T> getNewMessages() {
		List<T> messages = new LinkedList<>();

		T msg;
		while((msg = queue.poll()) != null) {
			messages.add(msg);
		}

		return messages;
	}

	private void processMessages() {
		List<T> messages = getNewMessages();
		if (!messages.isEmpty()) {
			messageStream.accept(messages.parallelStream());
		}
	}
}

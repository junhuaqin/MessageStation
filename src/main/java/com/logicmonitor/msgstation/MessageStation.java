package com.logicmonitor.msgstation;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rbtq on 7/5/16.
 */
public class MessageStation {
    private ConcurrentHashMap<String, MessageQueue> MessageQueues = new ConcurrentHashMap<>();

    public <T> void createTopic(final String topic, MessageStream<T> messageStream) {
        if (!MessageQueues.containsKey(topic)) {
            MessageQueues.put(topic, new LinkedMessageQueue<>(messageStream));
        }
    }

    public <T> boolean publish(final String topic, T message) {
        if (MessageQueues.containsKey(topic)) {
            return MessageQueues.get(topic).offer(message);
        } else {
            return false;
        }
    }

    protected void processMessages() {
        MessageQueues.entrySet().parallelStream().forEach(n->n.getValue().processMessages());
    }
}

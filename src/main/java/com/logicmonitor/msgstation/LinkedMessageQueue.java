package com.logicmonitor.msgstation;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by rbtq on 7/5/16.
 */
class LinkedMessageQueue<T> implements MessageQueue{

    private LinkedBlockingQueue<T> queue;
    private MessageStream<T> messageStream;

    protected LinkedMessageQueue(MessageStream<T> messageStream) {
        this.messageStream = Objects.requireNonNull(messageStream);
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public boolean offer(Object obj) {
        return queue.offer((T) obj);
    }

    @Override
    public void processMessages() {
        List<T> messages = new LinkedList<>();
        do {
            T msg = queue.poll();
            if (msg != null) {
                messages.add(msg);
            } else {
                break;
            }
        } while (true);

        messageStream.accept(messages.parallelStream());
    }
}


package com.logicmonitor.msgstation;

/**
 * Created by rbtq on 7/5/16.
 */
interface MessageQueue {
    boolean offer(Object obj);
    void processMessages();
}

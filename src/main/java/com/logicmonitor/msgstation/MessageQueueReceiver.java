package com.logicmonitor.msgstation;

/**
 * Created by rbtq on 7/5/16.
 */
interface MessageQueueReceiver {
	void put(Object obj) throws InterruptedException;
}

package com.logicmonitor.msgstation;

/**
 * Created by rbtq on 7/5/16.
 */
interface MessagePublisher {
	void publish(Object obj) throws InterruptedException;
}

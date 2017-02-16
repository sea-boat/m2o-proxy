package com.seaboat.m2o.proxy.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.net.reactor.connection.Connection;
import com.seaboat.net.reactor.connection.ConnectionEventHandler;
import com.seaboat.net.reactor.connection.ConnectionEvents;

/**
 * 
 * <pre><b>ConnectionLogHandler implements ConnectionEventHandler, this handler will be invoked when ACCEPT&CLOSE event happens,mainly to log.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class ConnectionLogHandler implements ConnectionEventHandler {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConnectionLogHandler.class);
	private static int INTERESTED = ConnectionEvents.ACCEPT
			| ConnectionEvents.CLOSE;

	public void event(Connection connection, int event) {
		if ((event & INTERESTED) != 0) {
			if ((event & ConnectionEvents.ACCEPT) != 0)
				LOGGER.info("accept connection,id is " + connection.getId());
			if ((event & ConnectionEvents.CLOSE) != 0)
				LOGGER.info("close connection,id is " + connection.getId());
		}
	}
}

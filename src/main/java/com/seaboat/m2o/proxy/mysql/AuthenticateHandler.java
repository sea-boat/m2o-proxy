package com.seaboat.m2o.proxy.mysql;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.mysql.protocol.AuthPacket;
import com.seaboat.net.reactor.FrontendConnection;

/**
 * 
 * <pre><b>AuthenticateHandler provide a authentication for mysql client.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class AuthenticateHandler implements MysqlHandler {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthenticateHandler.class);
	private static final byte[] AUTH_OK = new byte[] { 7, 0, 0, 2, 0, 0, 0, 2,
			0, 0, 0 };

	public void handle(byte[] data, FrontendConnection connection) {
		// TODO Auto-generated method stub
		AuthPacket auth = new AuthPacket();
		auth.read(data);
		
		LOGGER.info(auth.user + "======" + auth.password);
		connection.WriteToQueue(ByteBuffer.wrap(AUTH_OK));
		try {
			connection.write();
		} catch (IOException e) {
			LOGGER.warn("IOException happens when writing buffer to frontend connection.");
		}
	}
}

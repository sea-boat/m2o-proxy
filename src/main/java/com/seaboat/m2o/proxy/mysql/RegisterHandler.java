package com.seaboat.m2o.proxy.mysql;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.mysql.protocol.Capabilities;
import com.seaboat.mysql.protocol.CharsetUtil;
import com.seaboat.mysql.protocol.HandshakePacket;
import com.seaboat.mysql.protocol.Versions;
import com.seaboat.mysql.protocol.util.RandomUtil;
import com.seaboat.net.reactor.connection.Connection;
import com.seaboat.net.reactor.connection.ConnectionEventHandler;
import com.seaboat.net.reactor.connection.ConnectionEvents;

/**
 * 
 * <pre><b>RegisterHandler implements ConnectionEventHandler, this handler will be invoked when REGISTE event happens.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class RegisterHandler implements ConnectionEventHandler {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RegisterHandler.class);

	public int getEventType() {
		return ConnectionEvents.REGISTE;
	}

	public void event(Connection connection) {
		byte[] rand1 = RandomUtil.randomBytes(8);
		byte[] rand2 = RandomUtil.randomBytes(12);
		byte[] seed = new byte[rand1.length + rand2.length];
		System.arraycopy(rand1, 0, seed, 0, rand1.length);
		System.arraycopy(rand2, 0, seed, rand1.length, rand2.length);
		((MysqlConnection) connection).setSeed(seed);
		HandshakePacket hs = new HandshakePacket();
		hs.packetId = 0;
		hs.protocolVersion = Versions.PROTOCOL_VERSION;
		hs.serverVersion = Versions.SERVER_VERSION;
		hs.threadId = connection.getId();
		hs.seed = rand1;
		hs.serverCapabilities = getServerCapabilities();
		hs.serverCharsetIndex = (byte) (CharsetUtil.getIndex("utf8") & 0xff);
		hs.serverStatus = 2;
		hs.restOfScrambleBuff = rand2;
		ByteBuffer buffer = connection.getReactor().getReactorPool()
				.getBufferPool().allocate();
		hs.write(buffer);
		connection.WriteToQueue(buffer);
		try {
			connection.write();
		} catch (IOException e) {
			LOGGER.warn("IOException happens when writing buffer to frontend connection : "
					+ e);
		}
	}

	protected int getServerCapabilities() {
		int flag = 0;
		flag |= Capabilities.CLIENT_LONG_PASSWORD;
		flag |= Capabilities.CLIENT_FOUND_ROWS;
		flag |= Capabilities.CLIENT_LONG_FLAG;
		flag |= Capabilities.CLIENT_CONNECT_WITH_DB;
		flag |= Capabilities.CLIENT_ODBC;
		flag |= Capabilities.CLIENT_IGNORE_SPACE;
		flag |= Capabilities.CLIENT_PROTOCOL_41;
		flag |= Capabilities.CLIENT_INTERACTIVE;
		flag |= Capabilities.CLIENT_IGNORE_SIGPIPE;
		flag |= Capabilities.CLIENT_TRANSACTIONS;
		flag |= Capabilities.CLIENT_SECURE_CONNECTION;
		return flag;
	}
}

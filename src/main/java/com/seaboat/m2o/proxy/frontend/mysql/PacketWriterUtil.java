package com.seaboat.m2o.proxy.frontend.mysql;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.mysql.protocol.ErrorPacket;
import com.seaboat.mysql.protocol.OKPacket;
import com.seaboat.mysql.protocol.constant.ErrorCode;
import com.seaboat.mysql.protocol.constant.ServerStatus;
import com.seaboat.net.reactor.connection.Connection;

/**
 * 
 * <pre><b>a packet writer helper.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class PacketWriterUtil {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PacketWriterUtil.class);

	public static void writeErrorMessage(Connection connection, int packetId,
			ErrorCode errorCode, String[] messages) {
		ErrorPacket err = new ErrorPacket();
		err.packetId = (byte) packetId;
		err.errno = errorCode.code;
		String message = errorCode.message;
		for (String mess : messages)
			message = message.replaceFirst("%s", mess);
		err.message = message.getBytes();
		ByteBuffer bb = connection.getReactor().getReactorPool()
				.getBufferPool().allocate();
		err.write(bb);
		connection.WriteToQueue(bb);
		try {
			connection.write();
		} catch (IOException e) {
			LOGGER.warn("IOException happens when writing buffer to frontend connection.");
		}
	}

	public static void writeOKPacket(Connection connection, int packetId) {
		OKPacket ok = new OKPacket();
		ok.packetId = (byte) packetId;
		ok.serverStatus = ServerStatus.SERVER_STATUS_AUTOCOMMIT;
		ByteBuffer bb = connection.getReactor().getReactorPool()
				.getBufferPool().allocate();
		ok.write(bb);
		connection.WriteToQueue(bb);
		try {
			connection.write();
		} catch (IOException e) {
			LOGGER.warn("IOException happens when writing buffer to connection.");
		}
	}

	public static void writeOKPacket(Connection connection) {
		connection.WriteToQueue(ByteBuffer.wrap(OKPacket.OK));
		try {
			connection.write();
		} catch (IOException e) {
			LOGGER.warn("IOException happens when writing buffer to connection.");
		}
	}
}

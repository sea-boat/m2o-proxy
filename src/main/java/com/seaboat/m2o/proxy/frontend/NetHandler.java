package com.seaboat.m2o.proxy.frontend;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.m2o.proxy.frontend.mysql.Constants;
import com.seaboat.m2o.proxy.frontend.mysql.MysqlConnection;
import com.seaboat.net.reactor.connection.Connection;
import com.seaboat.net.reactor.handler.Handler;

/**
 * 
 * <pre><b>NetHandler will invoke MysqlHandler when mysql protocol packet is ready.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class NetHandler implements Handler {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(NetHandler.class);

	private byte[] data = null;

	public NetHandler() {
	}

	/**
	 *  deal with the received data.
	 */
	public void handle(Connection connection) throws IOException {

		// data must be ready
		ByteBuffer buff = connection.getReadBuffer();
		int size = buff.position();
		// get all data from bytebuffer
		byte[] thisTime = new byte[size];
		// flip
		buff.flip();
		buff.get(thisTime);
		// recycle the buffer
		connection.getReactor().getReactorPool().getBufferPool().recycle(buff);
		if (data == null) {
			data = thisTime;
		} else {
			// we have to concat the byte arrays
			byte[] temp = new byte[data.length + thisTime.length];
			System.arraycopy(data, 0, temp, 0, data.length);
			System.arraycopy(thisTime, 0, temp, data.length, thisTime.length);
			data = temp;
		}
		// not enough to deal with
		if (data.length < Constants.PACKET_HEADER_LENGTH) {
			return;
		}
		int packetLength = Constants.PACKET_HEADER_LENGTH
				+ calculatePayloadLength(data);
		// enough data to deal with
		if (data.length >= packetLength) {
			((MysqlConnection) connection).getHandler()
					.handle(data, connection);
			data = null;
		}
	}

	private int calculatePayloadLength(byte[] data) {
		int length = data[0] & 0xff;
		length |= data[1] & 0xff << 8;
		length |= data[2] & 0xff << 16;
		return length;
	}
}

package com.seaboat.m2o.proxy.mysql;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.m2o.proxy.configuration.M2OConfig;
import com.seaboat.m2o.proxy.configuration.User;
import com.seaboat.mysql.protocol.AuthPacket;
import com.seaboat.mysql.protocol.ErrorPacket;
import com.seaboat.mysql.protocol.OKPacket;
import com.seaboat.mysql.protocol.constant.ServerStatus;
import com.seaboat.mysql.protocol.constant.ErrorCode;
import com.seaboat.mysql.protocol.util.SecurityUtil;
import com.seaboat.net.reactor.connection.Connection;

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
	private static String ip;
	static {
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress().toString();// get ip
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthenticateHandler.class);

	public void handle(byte[] data, Connection connection) {
		AuthPacket auth = new AuthPacket();
		auth.read(data);
		User user = M2OConfig.getInstance().users.getUsers().get(auth.user);
		if (user == null) {
			String message = ErrorCode.ER_ACCESS_DENIED_ERROR.message
					.replaceFirst("%s", auth.user).replaceFirst("%s", ip)
					.replaceFirst("%s", "YES");
			int code = ErrorCode.ER_ACCESS_DENIED_ERROR.code;
			writeErrorMessage(connection, 2, code, message);
			return;
		}
		// calculate encrypted password
		byte[] encryptPass = null;
		try {
			encryptPass = SecurityUtil.scramble411(user.getPassword()
					.getBytes(), ((MysqlConnection) connection).getSeed());
		} catch (NoSuchAlgorithmException e) {
			LOGGER.warn(e.toString());
			String message = ErrorCode.ER_ACCESS_DENIED_ERROR.message
					.replaceFirst("%s", user.getName()).replaceFirst("%s", ip)
					.replaceFirst("%s", "YES");
			int code = ErrorCode.ER_ACCESS_DENIED_ERROR.code;
			writeErrorMessage(connection, 2, code, message);
			return;
		}
		if (!Arrays.equals(encryptPass, auth.password)) {
			String message = ErrorCode.ER_ACCESS_DENIED_ERROR.message
					.replaceFirst("%s", user.getName()).replaceFirst("%s", ip)
					.replaceFirst("%s", "YES");
			int code = ErrorCode.ER_ACCESS_DENIED_ERROR.code;
			writeErrorMessage(connection, 2, code, message);
			return;
		}
		// change the handler of MysqlConnection to SqlCommandHandler
		MysqlConnection mysqlConnection = (MysqlConnection) connection;
		mysqlConnection.setHandler(new SqlCommandHandler());
		mysqlConnection.setAuthenticated(true);
		mysqlConnection.setCharsetIndex(auth.charsetIndex);
		mysqlConnection.setSchema(auth.database);
		mysqlConnection.setUser(auth.user);
		// authenticate successfully
		OKPacket ok = new OKPacket();
		ok.packetId = 2;
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

	private void writeErrorMessage(Connection connection, int packetId,
			int code, String message) {
		ErrorPacket err = new ErrorPacket();
		err.packetId = (byte) packetId;
		err.errno = code;
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
}

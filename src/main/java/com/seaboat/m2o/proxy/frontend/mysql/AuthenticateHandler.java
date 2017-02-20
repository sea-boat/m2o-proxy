package com.seaboat.m2o.proxy.frontend.mysql;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.m2o.proxy.configuration.M2OConfig;
import com.seaboat.m2o.proxy.configuration.User;
import com.seaboat.mysql.protocol.AuthPacket;
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
			String[] messages = { auth.user, ip, "YES" };
			PacketWriterUtil.writeErrorMessage(connection, 2,
					ErrorCode.ER_ACCESS_DENIED_ERROR, messages);
			LOGGER.info("authenticate fail. connection id is "
					+ connection.getId());
			return;
		}
		// calculate encrypted password
		byte[] encryptPass = null;
		try {
			encryptPass = SecurityUtil.scramble411(user.getPassword()
					.getBytes(), ((MysqlConnection) connection).getSeed());
		} catch (NoSuchAlgorithmException e) {
			LOGGER.warn(e.toString());
			String[] messages = { user.getName(), ip, "YES" };
			PacketWriterUtil.writeErrorMessage(connection, 2,
					ErrorCode.ER_ACCESS_DENIED_ERROR, messages);
			LOGGER.info("authenticate fail. connection id is "
					+ connection.getId());
			return;
		}
		if (!Arrays.equals(encryptPass, auth.password)) {
			String[] messages = { user.getName(), ip, "YES" };
			PacketWriterUtil.writeErrorMessage(connection, 2,
					ErrorCode.ER_ACCESS_DENIED_ERROR, messages);
			LOGGER.info("authenticate fail. connection id is "
					+ connection.getId());
			return;
		}
		LOGGER.info("authenticate successfully. connection id is " + connection.getId());
		// change the handler of MysqlConnection to SqlCommandHandler
		MysqlConnection mysqlConnection = (MysqlConnection) connection;
		mysqlConnection.setHandler(new SqlCommandHandler());
		mysqlConnection.setAuthenticated(true);
		mysqlConnection.setCharsetIndex(auth.charsetIndex);
		mysqlConnection.setSchema(auth.database);
		mysqlConnection.setUser(auth.user);
		// authenticate successfully
		PacketWriterUtil.writeOKPacket(mysqlConnection, 2);
	}
}

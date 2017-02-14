package com.seaboat.m2o.proxy.mysql;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.m2o.proxy.configuration.M2OConfig;
import com.seaboat.m2o.proxy.configuration.User;
import com.seaboat.mysql.protocol.InitDBPacket;
import com.seaboat.mysql.protocol.MysqlPacket;
import com.seaboat.mysql.protocol.OKPacket;
import com.seaboat.mysql.protocol.constant.ErrorCode;
import com.seaboat.net.reactor.connection.Connection;

/**
 * 
 * <pre><b>deal with all sql commands.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class SqlCommandHandler implements MysqlHandler {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SqlCommandHandler.class);

	public void handle(byte[] data, Connection connection) {
		// TODO Auto-generated method stub
		MysqlConnection mysqlConnection = (MysqlConnection) connection;
		switch (data[4]) {
		case MysqlPacket.COM_PING:
			Ping(mysqlConnection);
			break;
		case MysqlPacket.COM_INIT_DB:
			InitDB(mysqlConnection, data);
			break;
		default:
			PacketWriterUtil.writeErrorMessage(mysqlConnection, (byte) 1,
					ErrorCode.ER_YES, new String[0]);
			break;
		}
	}

	private void InitDB(MysqlConnection mysqlConnection, byte[] data) {
		InitDBPacket packet = new InitDBPacket();
		packet.read(data);
		String schema = new String(packet.schema).toUpperCase();
		User user = M2OConfig.getInstance().users.getUsers().get(
				mysqlConnection.getUser());
		if (!user.getSchemas().contains(schema)) {
			String[] db = { schema };
			PacketWriterUtil.writeErrorMessage(mysqlConnection, (byte) 1,
					ErrorCode.ER_BAD_DB_ERROR, db);
			return;
		}
		mysqlConnection.setSchema(schema);
		PacketWriterUtil.writeOKPacket(mysqlConnection, 1);
	}

	private void Ping(MysqlConnection mysqlConnection) {
		mysqlConnection.WriteToQueue(ByteBuffer.wrap(OKPacket.OK));
		try {
			mysqlConnection.write();
		} catch (IOException e) {
			LOGGER.warn("IOException happens when writing buffer to connection.");
		}
	}

}

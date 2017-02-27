package com.seaboat.m2o.proxy.frontend.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.m2o.proxy.M2OEngine;
import com.seaboat.mysql.protocol.MysqlPacket;
import com.seaboat.mysql.protocol.constant.ErrorCode;
import com.seaboat.mysql.protocol.util.HexUtil;
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
		M2OEngine engine = mysqlConnection.getEngine();
		switch (data[4]) {
		case MysqlPacket.COM_PING:
			engine.ping(mysqlConnection);
			break;
		case MysqlPacket.COM_INIT_DB:
			engine.initDB(mysqlConnection, data);
			break;
		case MysqlPacket.COM_QUIT:
			engine.quit(mysqlConnection);
			break;
		case MysqlPacket.COM_QUERY:
			engine.query(mysqlConnection, data);
			break;
		default:
			LOGGER.info("unknown command : " + HexUtil.Bytes2HexString(data));
			PacketWriterUtil.writeErrorMessage(mysqlConnection, (byte) 1,
					ErrorCode.ER_YES, new String[0]);
			break;
		}
	}

}

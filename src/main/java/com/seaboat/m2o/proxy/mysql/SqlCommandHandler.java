package com.seaboat.m2o.proxy.mysql;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.mysql.protocol.MysqlPacket;
import com.seaboat.mysql.protocol.OKPacket;
import com.seaboat.net.reactor.connection.Connection;

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
		}
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

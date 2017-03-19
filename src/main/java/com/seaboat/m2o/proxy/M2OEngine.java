package com.seaboat.m2o.proxy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.m2o.proxy.backend.ConnectionPool;
import com.seaboat.m2o.proxy.configuration.M2OConfig;
import com.seaboat.m2o.proxy.configuration.User;
import com.seaboat.m2o.proxy.exception.JSONFormatException;
import com.seaboat.m2o.proxy.frontend.mysql.MysqlConnection;
import com.seaboat.m2o.proxy.frontend.mysql.PacketWriterUtil;
import com.seaboat.m2o.proxy.util.PreparedStatementParameter;
import com.seaboat.m2o.proxy.util.PreparedStatementParameterJson;
import com.seaboat.m2o.proxy.util.SqlTypeParser;
import com.seaboat.mysql.protocol.InitDBPacket;
import com.seaboat.mysql.protocol.MysqlMessage;
import com.seaboat.mysql.protocol.OKPacket;
import com.seaboat.mysql.protocol.constant.ErrorCode;

/**
 * 
 * <pre><b>m2o engine. this engine deal with a command that relates to oracle.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class M2OEngine implements Lifecycle {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(M2OEngine.class);

	private ConnectionPool connectionPool = new ConnectionPool();

	@Override
	public void init() {
		connectionPool.init();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void ping(MysqlConnection mysqlConnection) {
		mysqlConnection.WriteToQueue(ByteBuffer.wrap(OKPacket.OK));
		try {
			mysqlConnection.write();
		} catch (IOException e) {
			LOGGER.warn("IOException happens when writing buffer to connection.");
			e.printStackTrace();
		}
	}

	public void initDB(MysqlConnection mysqlConnection, byte[] data) {
		InitDBPacket packet = new InitDBPacket();
		packet.read(data);
		String schema = new String(packet.schema).toUpperCase();
		User user = M2OConfig.getInstance().getUsers()
				.get(mysqlConnection.getUser());
		if (!user.getSchemas().contains(schema)) {
			String[] db = { schema };
			PacketWriterUtil.writeErrorMessage(mysqlConnection, (byte) 1,
					ErrorCode.ER_BAD_DB_ERROR, db);
			return;
		}
		mysqlConnection.setSchema(schema);
		PacketWriterUtil.writeOKPacket(mysqlConnection, 1);
	}

	public void quit(MysqlConnection mysqlConnection) {
		// TODO to close oracle connection
		// we can close the client connection directly or return ok packet.
		try {
			mysqlConnection.close();
		} catch (IOException e) {
			LOGGER.warn("IOException happens when closing the client "
					+ mysqlConnection.getId() + " connection.");
			e.printStackTrace();
		}
	}

	public void query(MysqlConnection mysqlConnection, byte[] data) {
		// TODO Auto-generated method stub
		// get sql
		MysqlMessage mm = new MysqlMessage(data);
		mm.position(5);
		String sql = null;
		try {
			sql = mm.readString(mysqlConnection.getCharset());
		} catch (UnsupportedEncodingException e) {
			PacketWriterUtil.writeErrorMessage(mysqlConnection, (byte) 1,
					ErrorCode.ER_UNKNOWN_CHARACTER_SET,
					new String[] { mysqlConnection.getCharset() });
			return;
		}
		if (sql == null || sql.length() == 0) {
			PacketWriterUtil.writeErrorMessage(mysqlConnection, (byte) 1,
					ErrorCode.ER_NOT_ALLOWED_COMMAND, null);
			return;
		}
		if (sql.endsWith(";")) {
			sql = sql.substring(0, sql.length() - 1);
		}
		if (mysqlConnection.isAutoCommit())
			mysqlConnection.setExecuteBeginTime(System.currentTimeMillis());
		Object[] object = parseHint(sql);
		PreparedStatementParameter parameters = (PreparedStatementParameter) object[0];
		if (parameters == null) {
			PacketWriterUtil.writeErrorMessage(mysqlConnection, (byte) 1,
					ErrorCode.ER_YES, null);
			return;
		}
		String level = (String) object[1];
		sql = (String) object[2];
        int type = SqlTypeParser.parse(sql);
        switch(type){
        case(SqlTypeParser.SHOW):
        	
        	return;
        }
	}

	private Object[] parseHint(String sql) {
		String hint = null;
		String level = null;
		int begin = sql.indexOf("/** ");
		int end = sql.indexOf("**/");
		PreparedStatementParameter parameters = null;
		if (begin != -1 && end != -1) {
			hint = sql.substring(begin + 3, end);
			try {
				parameters = PreparedStatementParameterJson.JSON2Object(hint);
			} catch (JSONFormatException e) {
				e.printStackTrace();
				return null;
			}
			sql = sql.substring(end + 3, sql.length());
		}
		begin = sql.indexOf("/*+");
		end = sql.indexOf("*/");
		if (begin != -1 && end != -1) {
			level = sql.substring(begin + 3, end).toUpperCase();
			if (level.indexOf("HIGH") != -1) {
				level = "HIGH";
			} else if (level.indexOf("LOW") != -1) {
				level = "LOW";
			} else {
				level = null;
			}
			if (level != null)
				sql = sql.substring(0, begin)
						+ sql.substring(end + 2, sql.length());
		}
		return new Object[] { parameters, level, sql };
	}

}

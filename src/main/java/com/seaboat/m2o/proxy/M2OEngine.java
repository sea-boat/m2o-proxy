package com.seaboat.m2o.proxy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLUseStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSetNamesStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.seaboat.m2o.proxy.backend.ConnectionPool;
import com.seaboat.m2o.proxy.configuration.M2OConfig;
import com.seaboat.m2o.proxy.configuration.User;
import com.seaboat.m2o.proxy.exception.JSONFormatException;
import com.seaboat.m2o.proxy.frontend.mysql.MysqlConnection;
import com.seaboat.m2o.proxy.frontend.mysql.PacketWriterUtil;
import com.seaboat.m2o.proxy.frontend.mysql.filter.MysqlFilter;
import com.seaboat.m2o.proxy.frontend.mysql.filter.ShowFilter;
import com.seaboat.m2o.proxy.util.Constants;
import com.seaboat.m2o.proxy.util.PreparedStatementParameter;
import com.seaboat.m2o.proxy.util.PreparedStatementParameterJson;
import com.seaboat.mysql.protocol.InitDBPacket;
import com.seaboat.mysql.protocol.MysqlMessage;
import com.seaboat.mysql.protocol.OKPacket;
import com.seaboat.mysql.protocol.constant.ErrorCode;
import com.seaboat.mysql.protocol.util.CharsetUtil;

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

	private List<MysqlFilter> filters = new ArrayList<MysqlFilter>();

	private ConnectionPool connectionPool = new ConnectionPool();

	@Override
	public void init() {
		filters.add(new ShowFilter());
		connectionPool.init();
	}

	@Override
	public void start() {

	}

	@Override
	public void shutdown() {

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
		String level;
		if (parameters != null) {
			level = (String) object[1];
			sql = (String) object[2];
		}
		SQLStatement statement = null;
		try {
			MySqlStatementParser parser = new MySqlStatementParser(sql);
			statement = parser.parseStatement();
		} catch (Exception e) {
			LOGGER.warn("exception : ", e);
			PacketWriterUtil.writeErrorMessage(mysqlConnection, (byte) 1,
					ErrorCode.ER_YES, new String[] {});
			return;
		}
		if (statement instanceof MySqlShowStatement
				|| statement instanceof SQLShowTablesStatement) {
			dealWithShow(mysqlConnection, sql);
			return;
		}
		if (statement instanceof SQLUseStatement) {
			dealWithUse(mysqlConnection, sql);
			return;
		}
		if (statement instanceof MySqlSetNamesStatement) {
			MySqlSetNamesStatement sStatement = (MySqlSetNamesStatement) statement;
			String charset = sStatement.getCharSet();
			if (CharsetUtil.getIndex(charset) != 0) {// check if proxy support
				// this charset
				mysqlConnection.setCharset(charset);
				PacketWriterUtil.writeOKPacket(mysqlConnection, 1);
			} else {
				PacketWriterUtil.writeErrorMessage(mysqlConnection, 1,
						ErrorCode.ER_UNKNOWN_CHARACTER_SET,
						new String[] { charset });
			}
			return;
		}
		if (statement instanceof SQLSetStatement) {
			dealWithSet(mysqlConnection, sql, (SQLSetStatement) statement);
			return;
		}

	}

	private void dealWithSet(MysqlConnection mysqlConnection, String sql,
			SQLSetStatement statement) {
		// TODO Auto-generated method stub
		List<?> list = statement.getItems();
		if (list.size() > 0) {
			String key = list.get(0).toString().split("=")[0];
			String value = list.get(0).toString().split("=")[1].trim();
			if (key.equalsIgnoreCase(Constants.AUTOCOMMIT)) {
				if (value.equals("1") || value.equalsIgnoreCase("true")) {
					if (mysqlConnection.isAutoCommit()) {
						// do nothing
					} else {
						mysqlConnection.rollback();
						mysqlConnection.setAutoCommit(true);
					}
					PacketWriterUtil.writeOKPacket(mysqlConnection);
				} else {
					if (mysqlConnection.isAutoCommit()) {
						mysqlConnection.setAutoCommit(false);
					} else {
						mysqlConnection.rollback();
						mysqlConnection.setAutoCommit(false);
					}
					PacketWriterUtil.writeACOFFPacket(mysqlConnection);
				}
			}
			return;
		}

	}

	private void dealWithShow(MysqlConnection mysqlConnection, String sql) {
		for (MysqlFilter filter : filters)
			if (filter.doFilter(mysqlConnection, sql, "SHOW"))
				return;
	}

	private void dealWithUse(MysqlConnection mysqlConnection, String sql) {
		String schema = sql.trim().toUpperCase();
		if (schema.length() > 0)
			if (schema.endsWith(";"))
				schema = schema.substring(0, schema.length() - 1);
		mysqlConnection.setSchema(schema);
		PacketWriterUtil.writeOKPacket(mysqlConnection);
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

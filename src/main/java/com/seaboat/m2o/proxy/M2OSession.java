package com.seaboat.m2o.proxy;

import com.seaboat.m2o.proxy.backend.OracleConnection;
import com.seaboat.m2o.proxy.frontend.mysql.MysqlConnection;
import com.seaboat.m2o.proxy.frontend.mysql.PacketWriterUtil;
import com.seaboat.mysql.protocol.constant.ErrorCode;

/**
 * 
 * <pre><b>m2o session which allow front-end connections associate with back-end connections.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class M2OSession {

	private OracleConnection conn;

	private MysqlConnection mysqlConnection;

	public M2OSession(MysqlConnection mysqlConnection) {
		this.mysqlConnection = mysqlConnection;
	}

	public void commit() {
		if (conn != null)
			conn.commit();
		if (conn == null) {
			PacketWriterUtil.writeErrorMessage(mysqlConnection, (byte) 1,
					ErrorCode.ER_ERROR_DURING_COMMIT, new String[0]);
		}
	}

	public void setConn(OracleConnection conn) {
		this.conn = conn;
	}

	public OracleConnection getConn() {
		return conn;
	}

	public void releaseConn() {
		if (conn != null)
			conn.close();
		conn = null;
	}

	public void rollback() {
		if (conn != null)
			conn.rollback();
		if (conn == null) {
			PacketWriterUtil.writeErrorMessage(mysqlConnection, (byte) 1,
					ErrorCode.ER_ERROR_DURING_COMMIT, new String[0]);
		}
	}

	public void rollbackWithoutResponse() {
		if (conn != null)
			conn.rollbackWithoutResponse();
	}
}

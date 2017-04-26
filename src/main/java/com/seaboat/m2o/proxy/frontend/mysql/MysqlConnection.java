package com.seaboat.m2o.proxy.frontend.mysql;

import java.nio.channels.SocketChannel;

import com.seaboat.m2o.proxy.M2OEngine;
import com.seaboat.m2o.proxy.M2OSession;
import com.seaboat.net.reactor.Reactor;
import com.seaboat.net.reactor.connection.Connection;

/**
 * 
 * <pre><b>it stands for a mysql connection. it extends a base connection.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class MysqlConnection extends Connection {

	private boolean isAuthenticated;
	private String schema;
	private String user;
	private int charsetIndex;
	private volatile boolean autoCommit;
	private byte[] seed;
	private MysqlHandler handler;
	private M2OEngine engine;
	private long executeBeginTime;
	private M2OSession session;
	private String AppId;

	public MysqlConnection(SocketChannel channel, long id, Reactor reactor) {
		super(channel, id, reactor);
		this.autoCommit = true;
		this.handler = new AuthenticateHandler();
	}

	public byte[] getSeed() {
		return seed;
	}

	public void setSeed(byte[] seed) {
		this.seed = seed;
	}

	public MysqlHandler getHandler() {
		return handler;
	}

	public void setHandler(MysqlHandler handler) {
		this.handler = handler;
	}

	public String getAppId() {
		return AppId;
	}

	public void setAppId(String appId) {
		AppId = appId;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getCharsetIndex() {
		return charsetIndex;
	}

	public void setCharsetIndex(int charsetIndex) {
		this.charsetIndex = charsetIndex;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public M2OEngine getEngine() {
		return engine;
	}

	public void setEngine(M2OEngine engine) {
		this.engine = engine;
	}

	public long getExecuteBeginTime() {
		return executeBeginTime;
	}

	public void setExecuteBeginTime(long executeBeginTime) {
		this.executeBeginTime = executeBeginTime;
	}

	public void rollback() {
		// TODO Auto-generated method stub
		//don't write package to client
	}

	public M2OSession getSession() {
		return session;
	}

	public void setSession(M2OSession session) {
		this.session = session;
	}

}

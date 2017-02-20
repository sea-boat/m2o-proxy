package com.seaboat.m2o.proxy.frontend.mysql;

import java.nio.channels.SocketChannel;

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
	private byte[] seed;
	private MysqlHandler handler;

	public MysqlConnection(SocketChannel channel, long id, Reactor reactor) {
		super(channel, id, reactor);
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

}

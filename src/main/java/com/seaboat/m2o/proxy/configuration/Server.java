package com.seaboat.m2o.proxy.configuration;

/**
 * 
 * <pre><b>server model.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class Server {
	private int port;
	private String host;
	private String acceptorName;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getAcceptorName() {
		return acceptorName;
	}

	public void setAcceptorName(String acceptorName) {
		this.acceptorName = acceptorName;
	}

}

package com.seaboat.m2o.proxy;

import com.seaboat.m2o.proxy.backend.ConnectionPool;

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

}

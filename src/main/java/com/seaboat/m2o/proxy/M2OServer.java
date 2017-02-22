package com.seaboat.m2o.proxy;

import java.io.IOException;

import com.seaboat.m2o.proxy.configuration.M2OConfig;
import com.seaboat.m2o.proxy.frontend.ConnectionLogHandler;
import com.seaboat.m2o.proxy.frontend.NetHandler;
import com.seaboat.m2o.proxy.frontend.mysql.MysqlConnectionFactory;
import com.seaboat.m2o.proxy.frontend.mysql.RegisterHandler;
import com.seaboat.net.reactor.Acceptor;
import com.seaboat.net.reactor.ReactorPool;
import com.seaboat.net.reactor.handler.Handler;

/**
 * 
 * <pre><b>m2o proxy server.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class M2OServer implements Lifecycle {

	private String acceptorName;
	private String host;
	private int port;
	private ReactorPool reactorPool;
	private Acceptor acceptor;
	private M2OEngine engine;
	private static M2OServer server;

	private M2OServer() throws IOException {
		init();
	}

	public M2OEngine getEngine() {
		return engine;
	}

	@Override
	public void start() {
		acceptor.start();
	}

	@Override
	public void init() {
		acceptorName = M2OConfig.getInstance().server.getAcceptorName();
		host = M2OConfig.getInstance().server.getHost();
		port = M2OConfig.getInstance().server.getPort();
		engine = new M2OEngine();
		try {
			reactorPool = new ReactorPool(Runtime.getRuntime()
					.availableProcessors(), new NetHandler());
			acceptor = new Acceptor(reactorPool, acceptorName, host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		acceptor.addConnectionEventHandler(new RegisterHandler());
		acceptor.addConnectionEventHandler(new ConnectionLogHandler());
		acceptor.setConnectionFactory(new MysqlConnectionFactory());
	}

	public static M2OServer getInstance() throws IOException {
		// only the main thread will invoke here,will not be multi instances.
		if (server == null) {
			server = new M2OServer();
		}
		return server;
	}

	@Override
	public void shutdown() {

	}
}

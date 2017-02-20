package com.seaboat.m2o.proxy;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.m2o.proxy.configuration.M2OConfig;
import com.seaboat.m2o.proxy.frontend.ConnectionLogHandler;
import com.seaboat.m2o.proxy.frontend.mysql.MysqlConnectionFactory;
import com.seaboat.m2o.proxy.frontend.mysql.RegisterHandler;
import com.seaboat.net.reactor.Acceptor;
import com.seaboat.net.reactor.ReactorPool;
import com.seaboat.net.reactor.connection.ConnectionEventHandler;
import com.seaboat.net.reactor.handler.Handler;

/**
 * 
 * <pre><b>m2o proxy bootstrap.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class Bootstrap {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Bootstrap.class);

	public static void main(String[] args) {
		try {
			LOGGER.info("m20-proxy is starting up ......");
			M2OServer.getInstance().start();
			LOGGER.info("m2o-proxy has started up successfully.");
			while (true) {
				Thread.sleep(300 * 1000);
			}
		} catch (Throwable e) {
			LOGGER.error(" launch error", e);
			System.exit(-1);
		}
	}
}

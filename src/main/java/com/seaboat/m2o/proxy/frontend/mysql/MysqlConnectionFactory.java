package com.seaboat.m2o.proxy.frontend.mysql;

import java.nio.channels.SocketChannel;

import com.seaboat.m2o.proxy.M2OServer;
import com.seaboat.net.reactor.Reactor;
import com.seaboat.net.reactor.connection.ConnectionFactory;

/**
 * 
 * <pre><b>mysql connection factory.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class MysqlConnectionFactory implements ConnectionFactory {

	public MysqlConnection createConnection(SocketChannel channel, long id,
			Reactor reactor) {
		MysqlConnection mysqlConnection = new MysqlConnection(channel, id,
				reactor);
		mysqlConnection.setEngine(M2OServer.getInstance().getEngine());
		return mysqlConnection;
	}

}

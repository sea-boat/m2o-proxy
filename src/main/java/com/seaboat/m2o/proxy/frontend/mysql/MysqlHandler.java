package com.seaboat.m2o.proxy.frontend.mysql;

import com.seaboat.net.reactor.connection.Connection;

/**
 * 
 * <pre><b>this interface handler mysql protocol.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public interface MysqlHandler {

	void handle(byte[] data, Connection connection);

}

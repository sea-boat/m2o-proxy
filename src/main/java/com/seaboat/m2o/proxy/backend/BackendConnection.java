package com.seaboat.m2o.proxy.backend;

/**
 * 
 * <pre><b>m2o back-end connections interface.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public interface BackendConnection {
	public void commit();

	public void close();

	public void rollbackWithoutResponse();

	public void rollback();
}

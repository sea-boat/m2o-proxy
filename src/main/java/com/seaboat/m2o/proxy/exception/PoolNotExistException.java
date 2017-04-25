package com.seaboat.m2o.proxy.exception;

/**
 * 
 * <pre><b>a  exception that the datasource pool can not be found.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PoolNotExistException extends Exception {

	public PoolNotExistException() {
		super();
	}

	public PoolNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public PoolNotExistException(String message) {
		super(message);
	}

	public PoolNotExistException(Throwable cause) {
		super(cause);
	}

}

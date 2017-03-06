package com.seaboat.m2o.proxy.exception;

/**
 * 
 * <pre><b>json format exception.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class JSONFormatException extends Exception {

	private static final long serialVersionUID = 4690616096650129009L;

	public JSONFormatException() {
		super();
	}

	public JSONFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public JSONFormatException(String message) {
		super(message);
	}

	public JSONFormatException(Throwable cause) {
		super(cause);
	}

}

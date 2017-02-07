package com.seaboat.m2o.proxy.configuration;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * <pre><b>user model.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class User {
	private String name;
	private String password;
	private List<String> schemas;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getSchemas() {
		return schemas;
	}

	public void setSchemas(String schemas) {
		String[] ss = schemas.split("|");
		this.schemas = Arrays.asList(ss);
	}
}

package com.seaboat.m2o.proxy.configuration;

import org.apache.commons.digester.Digester;

/**
 * 
 * <pre><b>m2o config.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class M2OConfig {
	private static M2OConfig instance = new M2OConfig();
	public static final String USER_PATH = "/user.xml";
	public Users users;

	private M2OConfig() {
		users = readUsers();
	}

	public static M2OConfig getInstance() {
		return instance;
	}

	private Users readUsers() {
		try {
			Digester digester = new Digester();
			digester.setValidating(false);
			digester.addObjectCreate("users", Users.class);
			digester.addObjectCreate("users/user", User.class);
			digester.addBeanPropertySetter("users/user/name");
			digester.addBeanPropertySetter("users/user/password");
			digester.addCallMethod("users/user/schemas", "setSchemas", 0);
			digester.addSetNext("users/user", "addUser");
			return (Users) digester.parse(Users.class
					.getResourceAsStream(M2OConfig.USER_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

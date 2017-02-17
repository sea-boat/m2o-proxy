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
	public static final String SERVER_PATH = "/server.xml";
	public Users users;
	public Server server;

	private M2OConfig() {
		users = readUsers();
		server = readServer();
	}

	private Server readServer() {
		try {
			Digester digester = new Digester();
			digester.setValidating(false);
			digester.addObjectCreate("server", Server.class);
			digester.addBeanPropertySetter("server/port");
			digester.addBeanPropertySetter("server/host");
			digester.addBeanPropertySetter("server/acceptorName");
			return (Server) digester.parse(Server.class
					.getResourceAsStream(M2OConfig.SERVER_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

package com.seaboat.m2o.proxy.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public static final String POOL_PATH = "/pool.xml";
	private Map<String, User> users = new HashMap<String, User>();
	private List<Host> hosts = new ArrayList<Host>();
	private Server server;

	private M2OConfig() {
		readUsers();
		readServer();
		readPools();
	}

	private void readPools() {
		try {
			Digester digester = new Digester();
			digester.setValidating(false);
			digester.push(this);
			digester.addObjectCreate("pools/host", Host.class);
			digester.addSetProperties("pools/host");
			digester.addBeanPropertySetter("pools/host/heartbeat");
			digester.addObjectCreate("pools/host/pool", Pool.class);
			digester.addSetNext("pools/host/pool", "addPool");
			digester.addSetNext("pools/host", "addHost");
			digester.addSetProperties("pools/host/pool");
			digester.parse(Pool.class.getResourceAsStream(M2OConfig.POOL_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addHost(Host host) {
		hosts.add(host);
	}

	private Server readServer() {
		try {
			Digester digester = new Digester();
			digester.setValidating(true);
			digester.addObjectCreate("server", Server.class);
			digester.addBeanPropertySetter("server/port");
			digester.addBeanPropertySetter("server/host");
			digester.addBeanPropertySetter("server/acceptorName");
			this.server = (Server) digester.parse(Server.class
					.getResourceAsStream(M2OConfig.SERVER_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static M2OConfig getInstance() {
		return instance;
	}

	public void addUser(User user) {
		users.put(user.getName(), user);
	}

	public Map<String, User> getUsers() {
		return users;
	}

	public void setUsers(Map<String, User> users) {
		this.users = users;
	}

	public List<Host> getHosts() {
		return hosts;
	}

	public Server getServer() {
		return server;
	}

	private void readUsers() {
		try {
			Digester digester = new Digester();
			digester.setValidating(true);
			digester.push(this);
			digester.addObjectCreate("users/user", User.class);
			digester.addBeanPropertySetter("users/user/name");
			digester.addBeanPropertySetter("users/user/password");
			digester.addCallMethod("users/user/schemas", "setSchemas", 0);
			digester.addSetNext("users/user", "addUser");
			digester.parse(User.class.getResourceAsStream(M2OConfig.USER_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

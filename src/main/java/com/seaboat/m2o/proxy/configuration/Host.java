package com.seaboat.m2o.proxy.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <pre><b>host model.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class Host {
	private String name;
	private String url;
	private String heartbeat = "select 1 from dual";
	private List<Pool> pools = new ArrayList<Pool>();;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(String heartbeat) {
		this.heartbeat = heartbeat;
	}

	public List<Pool> getPools() {
		return pools;
	}

	public void addPool(Pool pool) {
		pools.add(pool);
	}

}

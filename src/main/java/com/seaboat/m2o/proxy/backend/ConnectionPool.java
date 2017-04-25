package com.seaboat.m2o.proxy.backend;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.util.StringUtils;
import com.seaboat.m2o.proxy.Lifecycle;
import com.seaboat.m2o.proxy.configuration.Host;
import com.seaboat.m2o.proxy.configuration.M2OConfig;
import com.seaboat.m2o.proxy.configuration.Pool;
import com.seaboat.m2o.proxy.exception.PoolNotExistException;

/**
 * 
 * <pre><b>connection pool manages all datasources of all host.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class ConnectionPool implements Lifecycle {
	private static final Logger LOGGER = LoggerFactory
			.getLogger("ConnectionPool");
	private static final String DEFAULTDRIVER = "oracle.jdbc.driver.OracleDriver";
	private final Map<String, DataSource> pools = new ConcurrentHashMap<>();
	private final List<Host> hosts = M2OConfig.getInstance().getHosts();

	@Override
	public void init() {
		iniDataSource();
	}

	private void iniDataSource() {
		for (int i = 0; i < hosts.size(); i++) {
			String url = hosts.get(i).getUrl();
			String heartbeat = hosts.get(i).getHeartbeat();
			for (int j = 0; j < hosts.get(i).getPools().size(); j++) {
				Pool pool = hosts.get(i).getPools().get(j);
				String key = makeKey(pool);
				LOGGER.debug("iniDataSource KEY = " + key);
				DataSource ds = null;
				try {
					ds = createPool(pool, url, heartbeat);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (ds != null)
					pools.put(key, ds);
			}
		}
	}

	private DataSource createPool(Pool pool, String url, String heartbeat)
			throws Exception {
		Properties properties = new Properties();
		properties.setProperty("driverClassName", DEFAULTDRIVER);
		properties.setProperty("url", url);
		properties.setProperty("username", pool.getUsername());
		properties.setProperty("password", pool.getPassword());
		properties.setProperty("initialSize",
				String.valueOf(pool.getInitialSize()));
		properties
				.setProperty("maxActive", String.valueOf(pool.getMaxActive()));
		properties.setProperty("maxWait", String.valueOf(pool.getMaxWait()));
		properties.setProperty("minIdle", String.valueOf(pool.getMinIdle()));
		properties.setProperty("validationQuery", heartbeat);
		properties.setProperty("removeAbandoned", pool.getRemoveAbandoned());
		properties.setProperty("removeAbandoned",
				String.valueOf(pool.getRemoveAbandonedTimeout()));
		properties.setProperty("removeAbandoned", pool.getTestOnBorrow());
		properties.setProperty("removeAbandoned", pool.getTestOnReturn());
		properties.setProperty("removeAbandoned", pool.getTestWhileIdle());
		DataSource ds = (DruidDataSource) DruidDataSourceFactory
				.createDataSource(properties);
		return ds;
	}

	private String makeKey(Pool pool) {
		String appId = "";
		String level = "";
		if (!StringUtils.isEmpty(pool.getAppId()))
			appId = pool.getAppId();
		if (!StringUtils.isEmpty(pool.getLevel()))
			level = pool.getLevel();
		String username = pool.getUsername();
		return username + appId + level;
	}

	/**
	 * @param key
	 * @param appId
	 * @param level
	 * @return oracle connection
	 */
	public OracleConnection getNormalConnectionByKey(String key, String appId,
			String level) throws SQLException, PoolNotExistException {
		OracleConnection connection = new OracleConnection();
		DataSource source = getDataSource(key, appId, level);
		if (source == null)
			throw new PoolNotExistException(
					"username or appid or level is not exist: username = "
							+ key + ", appid = " + appId + ", level = " + level);
		LOGGER.debug(source.toString());
		Connection c = source.getConnection();
		c.rollback();
		connection.setCon(c);
		return connection;
	}

	/**
	* @param key
	* @param appId
	* @param level
	* @return DataSource
	*/
	private DataSource getDataSource(String key, String appId, String level) {
		LOGGER.debug("key = " + key + ", appId = " + appId + ", level = "
				+ level);
		if (appId == null || appId.equals("")) {
			if (level == null || level.equals("")) {
				for (String k : pools.keySet()) {
					if (k.startsWith(key) && k.endsWith("HIGH"))
						return pools.get(k);
					if (k.contains(key))
						return pools.get(k);
				}
			} else {
				for (String k : pools.keySet()) {
					if (k.startsWith(key) && k.endsWith(level))
						return pools.get(k);
				}
			}
		}

		if (level == null || level.equals("")) {
			if (appId == null || appId.equals("")) {
				for (String k : pools.keySet()) {
					if (k.startsWith(key) && k.endsWith("HIGH"))
						return pools.get(k);
					if (k.contains(key))
						return pools.get(k);
				}
			} else {
				DataSource pooled = pools.get(key + appId + "HIGH");
				if (pooled != null)
					return pooled;
			}
		}

		if (appId != null && level != null && !appId.equals("")
				&& !level.equals("")) {
			DataSource pooled = pools.get(key + appId + level);
			if (pooled != null)
				return pooled;
		}

		LOGGER.info("can not get a datasource in the ConnectionPool by key="
				+ key + ", appId=" + appId + ", level=" + level);
		return null;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

}

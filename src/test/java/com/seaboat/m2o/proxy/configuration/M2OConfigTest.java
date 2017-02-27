package com.seaboat.m2o.proxy.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * <pre><b>M2OConfig testcase.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class M2OConfigTest {
	@Test
	public void test() {
		M2OConfig config = M2OConfig.getInstance();
		assertTrue(config.getUsers().size() == 2);
		assertEquals(config.getUsers().get("user").getName(), "user");
		assertEquals(config.getUsers().get("root").getName(), "root");
		assertTrue(config.getServer().getPort() == 6789);
		assertEquals(config.getServer().getHost(), "localhost");
		assertEquals(config.getHosts().get(0).getName(), "test_DEV");
		assertEquals(config.getHosts().size(), 2);
		assertEquals(config.getHosts().get(0).getPools().size(), 2);
		assertEquals(config.getHosts().get(0).getPools().get(0).getUsername(),
				"test_A");
	}
}

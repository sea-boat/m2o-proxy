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
		assertTrue(config.users.getUsers().size() == 2);
		assertEquals(config.users.getUsers().get("user").getName(),"user");
		assertEquals(config.users.getUsers().get("root").getName(),"root");
		assertTrue(config.server.getPort()==6789);
		assertEquals(config.server.getHost(),"localhost");
	}
}

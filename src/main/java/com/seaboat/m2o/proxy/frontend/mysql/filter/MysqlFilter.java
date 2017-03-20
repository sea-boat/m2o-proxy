package com.seaboat.m2o.proxy.frontend.mysql.filter;

import com.seaboat.m2o.proxy.frontend.mysql.MysqlConnection;

/**
 * 
 * <pre><b>this filter prevents from passing the mysql commands to oracle.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public interface MysqlFilter {

	public void doFilter(MysqlConnection c, String sql);

}

package com.seaboat.m2o.proxy.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <pre><b>stand for all parameters of client.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class PreparedStatementParameter {

	private int num;
	private List<Parameters> parameters = new ArrayList<Parameters>();

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<Parameters> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameters> parameters) {
		this.parameters = parameters;
	}

}

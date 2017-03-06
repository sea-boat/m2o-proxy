package com.seaboat.m2o.proxy.util;

import java.util.HashMap;
import java.util.Map;

import com.seaboat.m2o.proxy.exception.JSONFormatException;

import net.sf.json.JSONObject;

/**
 * 
 * <pre><b>json to object util.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class PreparedStatementParameterJson {

	public static PreparedStatementParameter JSON2Object(String jsonStr)
			throws JSONFormatException {
		JSONObject obj = null;
		try {
			obj = JSONObject.fromObject(jsonStr);
		} catch (Exception e) {
			throw new JSONFormatException(e.toString());
		}
		@SuppressWarnings("rawtypes")
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("parameters", Parameters.class);
		PreparedStatementParameter jb = (PreparedStatementParameter) JSONObject
				.toBean(obj, PreparedStatementParameter.class, classMap);
		return jb;
	}

}

package com.seaboat.m2o.proxy.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.seaboat.m2o.proxy.exception.JSONFormatException;

public class PreparedStatementParameterJsonTest {

	@Test
	public void test() {
		PreparedStatementParameter p = null;
		String str = "{\"num\":\"2\",\"parameters\":[{\"type\":\"12\",\"value\":\"'242760985'\"},{\"type\":\"12\",\"value\":\"'1'\"}]}";
		try {
			p = PreparedStatementParameterJson.JSON2Object(str);
		} catch (JSONFormatException e) {
			e.printStackTrace();
		}
		assertEquals(p.getNum(), 2);
		assertEquals(p.getParameters().get(0).getValue(), "'242760985'");
	}
}

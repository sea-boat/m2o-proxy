package com.seaboat.m2o.proxy.mysql;

import com.seaboat.net.reactor.FrontendConnection;

public interface MysqlHandler {
	void handle(byte[] data, FrontendConnection connection);
}

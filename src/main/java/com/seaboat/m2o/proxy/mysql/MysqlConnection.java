package com.seaboat.m2o.proxy.mysql;

import java.nio.channels.SocketChannel;

import com.seaboat.net.reactor.Reactor;
import com.seaboat.net.reactor.connection.Connection;

public class MysqlConnection extends Connection {

	private byte[] seed;

	public MysqlConnection(SocketChannel channel, long id, Reactor reactor) {
		super(channel, id, reactor);
	}

	public byte[] getSeed() {
		return seed;
	}

	public void setSeed(byte[] seed) {
		this.seed = seed;
	}

}

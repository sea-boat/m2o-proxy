package com.seaboat.m2o.proxy.frontend.mysql.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.m2o.proxy.frontend.mysql.MysqlConnection;
import com.seaboat.mysql.protocol.ColumnCountPacket;
import com.seaboat.mysql.protocol.ColumnDefinitionPacket;
import com.seaboat.mysql.protocol.EOFPacket;
import com.seaboat.mysql.protocol.ResultsetRowPacket;
import com.seaboat.mysql.protocol.constant.ColumnType;
import com.seaboat.mysql.protocol.util.CharsetUtil;

/**
 * 
 * <pre><b>all show commands will return a null resultset . it dons't matter.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class ShowFilter implements MysqlFilter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShowFilter.class);
	private static final int FIELD_COUNT = 1;
	private static final ColumnCountPacket header = new ColumnCountPacket();
	private static final ColumnDefinitionPacket[] fields = new ColumnDefinitionPacket[FIELD_COUNT];

	static {
		byte packetId = 0;
		header.packetId = ++packetId;
		header.columnCount = FIELD_COUNT;
		fields[0] = new ColumnDefinitionPacket();
		fields[0].charsetSet = CharsetUtil.getIndex("utf-8");
		fields[0].name = getBytes("result", "utf-8");
		fields[0].type = ColumnType.FIELD_TYPE_STRING;
		fields[0].packetId = ++packetId;
	}

	static byte[] getBytes(String src, String code) {
		try {
			return src.getBytes(code);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean doFilter(MysqlConnection c, String sql, String type) {
		if (type.equals("SHOW")) {
			ByteBuffer buffer = c.getReactor().getReactorPool().getBufferPool()
					.allocate();
			header.write(buffer);
			byte packetId = 0;
			for (ColumnDefinitionPacket field : fields) {
				field.write(buffer);
				packetId = field.packetId;
			}
			EOFPacket eof = new EOFPacket();
			eof.packetId = ++packetId;
			eof.write(buffer);
			packetId = eof.packetId;
//			List<byte[]> values = new ArrayList();
//			 values.add(new byte[] { 1, 1, 1, 1 });
//			ResultsetRowPacket row = new ResultsetRowPacket(FIELD_COUNT);
//			row.columnValues = values;
//			row.packetId = ++packetId;
//			row.write(buffer);
			EOFPacket lastEof = new EOFPacket();
			lastEof.packetId = ++packetId;
			lastEof.write(buffer);
			c.WriteToQueue(buffer);
			try {
				c.write();
			} catch (IOException e) {
				LOGGER.warn("IOException happens when writing buffer to frontend connection : "
						+ e);
			}
			return true;
		}
		return false;
	}

}

package com.seaboat.m2o.proxy.frontend.mysql.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seaboat.m2o.proxy.frontend.mysql.MysqlConnection;
import com.seaboat.mysql.protocol.ColumnCountPacket;
import com.seaboat.mysql.protocol.ColumnDefinitionPacket;
import com.seaboat.mysql.protocol.EOFPacket;
import com.seaboat.mysql.protocol.constant.ColumnType;
import com.seaboat.mysql.protocol.util.CharsetUtil;

/**
 * 
 * <pre><b>a filter for select that can't be sent to oracle.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class SelectFilter implements MysqlFilter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SelectFilter.class);

	@Override
	public boolean doFilter(MysqlConnection c, String sql, String type) {
		if (type.equalsIgnoreCase("SELECT")) {
			if (sql.equalsIgnoreCase("@@version_comment")) {
				ByteBuffer buffer = c.getReactor().getReactorPool()
						.getBufferPool().allocate();
				byte packetId = 0;
				ColumnCountPacket header = new ColumnCountPacket();
				header.columnCount = 1;
				header.packetId = ++packetId;
				header.write(buffer);
				ColumnDefinitionPacket[] fields = new ColumnDefinitionPacket[1];
				fields[0] = new ColumnDefinitionPacket();
				fields[0].charsetSet = CharsetUtil.getIndex("utf-8");
				try {
					fields[0].name = "@@version_comment".getBytes("utf-8");
				} catch (UnsupportedEncodingException e1) {
					// will not happen
					e1.printStackTrace();
				}
				fields[0].type = ColumnType.FIELD_TYPE_STRING;
				fields[0].packetId = ++packetId;
				for (ColumnDefinitionPacket field : fields) {
					field.write(buffer);
					packetId = field.packetId;
				}
				EOFPacket eof = new EOFPacket();
				eof.packetId = ++packetId;
				eof.write(buffer);
				packetId = eof.packetId;
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
			}
			return true;
		}
		return false;
	}

}

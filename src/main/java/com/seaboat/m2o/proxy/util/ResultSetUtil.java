package com.seaboat.m2o.proxy.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.seaboat.mysql.protocol.ColumnDefinitionPacket;
import com.seaboat.mysql.protocol.ResultsetRowPacket;

/**
 * 
 * <pre><b>ResultSet util.</b></pre>
 * @author 
 * <pre>seaboat</pre>
 * <pre><b>email: </b>849586227@qq.com</pre>
 * <pre><b>blog: </b>http://blog.csdn.net/wangyangzhizhou</pre>
 * @version 1.0
 */
public class ResultSetUtil {

	public static int toFlag(ResultSetMetaData metaData, int column)
			throws SQLException {
		int flags = 0;
		if (metaData.isNullable(column) == 1) {
			flags |= 0001;
		}

		if (metaData.isSigned(column)) {
			flags |= 0020;
		}

		if (metaData.isAutoIncrement(column)) {
			flags |= 0200;
		}

		return flags;
	}

	public static void resultSetToFieldPacket(String charset,
			List<ColumnDefinitionPacket> fieldPks, ResultSet rs)
			throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int colunmCount = metaData.getColumnCount();
		if (colunmCount > 0) {
			for (int i = 0; i < colunmCount; i++) {
				int j = i + 1;
				ColumnDefinitionPacket cPacket = new ColumnDefinitionPacket();
				cPacket.orgName = StrUtil.encode(metaData.getColumnName(j),
						charset);
				cPacket.name = StrUtil.encode(metaData.getColumnLabel(j),
						charset);
				cPacket.orgTable = StrUtil.encode(metaData.getTableName(j),
						charset);
				cPacket.table = StrUtil.encode(metaData.getTableName(j),
						charset);
				cPacket.schema = StrUtil.encode(metaData.getSchemaName(j),
						charset);
				cPacket.flags = toFlag(metaData, j);
				cPacket.length = metaData.getColumnDisplaySize(j);

				cPacket.decimals = (byte) metaData.getScale(j);
				int javaType = MysqlDefs.javaTypeDetect(
						metaData.getColumnType(j), cPacket.decimals);
				cPacket.type = (byte) (MysqlDefs.javaTypeMysql(javaType) & 0xff);
				fieldPks.add(cPacket);
			}
		}
	}

	public static ResultsetRowPacket parseRowData(byte[] row,
			List<byte[]> fieldValues) {
		ResultsetRowPacket rowPkg = new ResultsetRowPacket(fieldValues.size());
		rowPkg.read(row);
		return rowPkg;
	}

	public static String getColumnValAsString(byte[] row,
			List<byte[]> fieldValues, int columnIndex) {
		ResultsetRowPacket rowPkg = new ResultsetRowPacket(fieldValues.size());
		rowPkg.read(row);
		byte[] columnData = rowPkg.columnValues.get(columnIndex);
		return new String(columnData);
	}

	public static byte[] getColumnVal(byte[] row, List<byte[]> fieldValues,
			int columnIndex) {
		ResultsetRowPacket rowPkg = new ResultsetRowPacket(fieldValues.size());
		rowPkg.read(row);
		byte[] columnData = rowPkg.columnValues.get(columnIndex);
		return columnData;
	}

	public static byte[] fromHex(String hexString) {
		String[] hex = hexString.split(" ");
		byte[] b = new byte[hex.length];
		for (int i = 0; i < hex.length; i++) {
			b[i] = (byte) (Integer.parseInt(hex[i], 16) & 0xff);
		}

		return b;
	}
}

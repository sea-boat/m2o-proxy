package com.seaboat.m2o.proxy.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrUtil {

	private static final Logger LOG = LoggerFactory.getLogger(StrUtil.class);

	public static final String CHARSET_UTF8 = "UTF-8";

	protected StrUtil() {
	}

	public static String gBToUnicode(final String strIn) {
		String strOut = null;

		if (strIn == null || "".equals(strIn.trim())) {
			return strIn;
		}
		try {
			final byte[] b = strIn.getBytes("GBK");
			strOut = new String(b, "ISO8859_1");
		} catch (final Exception e) {
			LOG.error(StrUtil.class.getName(), e);
		}
		return strOut;
	}

	public static String unicodeToGB(final String strIn) {
		String strOut = null;

		if (strIn == null || "".equals(strIn.trim())) {
			return strIn;
		}
		try {
			final byte[] b = strIn.getBytes("ISO8859_1");
			strOut = new String(b, "GBK");
		} catch (final Exception e) {
			LOG.error(StrUtil.class.getName(), e);
		}
		return strOut;
	}

	public static String encode(final String str, final String oldCharset,
			final String newCharset) {
		if (str == null) {
			return str;
		}
		String newStr = null;
		try {
			newStr = new String(str.getBytes(oldCharset), newCharset);
		} catch (final Exception e) {
			LOG.error(StrUtil.class.getName(), e);
		}
		return newStr;

	}

	public static String[] split(String str, final String sgn) {
		String[] returnValue = null;
		if (!"".equals(StrUtil.strnull(str))) {
			final List list = new ArrayList();
			int i = str.indexOf(sgn);
			String tempStr = "";
			for (; i >= 0; i = str.indexOf(sgn)) {
				tempStr = str.substring(0, i);
				str = str.substring(i + sgn.length());
				list.add(tempStr);
			}
			if (!"".equalsIgnoreCase(str)) {
				list.add(str);
			}
			returnValue = new String[list.size()];
			for (i = 0; i < list.size(); i++) {
				returnValue[i] = (String) list.get(i);
				returnValue[i] = returnValue[i].trim();
			}
		}
		return returnValue;
	}

	public static String arrayToStr(final String[] array, final String split) {
		if (array == null || array.length < 1) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append(split);
			}
			sb.append(StrUtil.strnull(array[i]));
		}
		return sb.toString();

	}

	public static String arrayToStrWithStr(final String[] array,
			final String split) {
		return StrUtil.arrayToStrWithStr(array, split, "0");

	}

	public static String arrayToStrWithStr(final String[] array,
			final String split, final String optType) {
		if (array == null || array.length < 1) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append("'");
			if ("1".equals(optType)) {
				final String temp = StrUtil.strnull(array[i]);
				sb.append(temp.substring(1, temp.length()));
			} else {
				sb.append(StrUtil.strnull(array[i]));
			}
			sb.append("'");

		}
		return sb.toString();

	}

	public static String[] strConvertoArray(final String str, final String sgn) {
		final StringTokenizer st = new StringTokenizer(str, sgn);
		final String[] retstr = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++) {
			retstr[i] = st.nextToken();
		}
		return retstr;
	}

	public static List strConvertoList(final String str, final String sgn) {
		final StringTokenizer st = new StringTokenizer(str, sgn);
		final List result = new LinkedList();

		for (int i = 0; st.hasMoreTokens(); i++) {
			result.add(st.nextToken());
		}
		return result;
	}

	public static String intToStr(final int lpInt, final int lpMaxLength) {
		int length, i;
		String returnValue = "";

		length = Integer.toString(lpInt).length();
		if (length < lpMaxLength) {
			i = lpMaxLength - length;
			while (i > 0) {
				returnValue = returnValue + "0";
				i--;
			}
			returnValue = returnValue + Integer.toString(lpInt);
		} else {
			returnValue = Integer.toString(lpInt);
		}
		return returnValue;
	}

	public static int toInt(final String source) {
		try {
			return Integer.parseInt(source);
		} catch (final NumberFormatException notint) {
			LOG.error(StrUtil.class.getName(), notint);
			return 0;
		}
	}

	public static String getPathFile(final String path) {
		String substr = "";
		try {
			if (path != null && !"".equals(path)) {
				final int i = path.lastIndexOf("/");
				substr = path.substring(i + 1).trim();
			}
		} catch (final Exception ex) {
			LOG.error(StrUtil.class.getName(), ex);
		}
		return substr;
	}

	public static String getLastTwo(final String str) {
		String substr = "";
		try {
			if (str != null && !"".equals(str)) {
				final int i = str.lastIndexOf(".");
				substr = str.substring(i + 1).trim();
			}
		} catch (final Exception ex) {
			LOG.error(StrUtil.class.getName(), ex);
		}
		return substr;
	}

	public static String getFileType(final String lpFileName) {
		String lpReturnValue = "";

		if (lpFileName != null && !"".equals(lpFileName)) {
			final int i = lpFileName.lastIndexOf(".");
			lpReturnValue = lpFileName.substring(i, lpFileName.length());
		}
		return lpReturnValue;
	}

	public static String getSubString(String str, final int beginIndex,
			final int endIndex) {
		String str1 = "";

		if (str == null) {
			str = "";
		}
		if (str.length() >= beginIndex && str.length() >= endIndex) {
			str1 = str.substring(beginIndex, endIndex);
		} else {
			str1 = str;
		}
		return str1;
	}

	public static String strnull(final String str, final String rpt) {
		if (str == null || "null".equals(str) || "".equals(str)
				|| str.trim() == null) {
			return rpt;
		} else {
			return str.trim();
		}
	}

	public static String strnull(final String strn) {
		return StrUtil.strnull(strn, "");
	}

	public static String strnull(final Object str) {
		String result = "";
		if (StrUtil.isNullOrEmpty(str)) {
			result = "";
		} else {
			result = str.toString();
		}
		return result;
	}

	public static String repnull(final String strn) {
		return StrUtil.strnull(strn, "&nbsp;");
	}

	public static String strnull(final Date strn) {
		String str = "";

		if (strn == null) {
			str = "0000-00-00 00:00:00";
		} else {
			str = strn.toString();
		}
		return (str);
	}

	public static String strnull(final BigDecimal strn) {
		String str = "";

		if (strn == null) {
			str = "0";
		} else {
			str = strn.toString();
		}
		return (str);
	}

	public static String strnull(final int strn) {
		final String str = String.valueOf(strn);
		return (str);
	}

	public static String strnull(final float strn) {
		final String str = String.valueOf(strn);
		return (str);
	}

	public static String strnull(final long strn) {
		final String str = String.valueOf(strn);
		return (str);
	}

	public static String strnull(final double strn) {
		final String str = String.valueOf(strn);
		return (str);
	}

	public static char hex(final int bin) {
		char retval;
		if (bin >= 0 && bin <= 9) {
			retval = (char) ('0' + bin);
		} else if (bin >= 10 && bin <= 15) {
			retval = (char) ('A' + bin - 10);
		} else {
			retval = '0';
		}
		return retval;
	}

	public static String replace(final String content, final String oldString,
			final String newString) {
		if (content == null || oldString == null) {
			return content;
		}
		if ("".equals(content) || "".equals(oldString)) {
			return content;
		}

		String resultString = "";
		int stringAtLocal = content.indexOf(oldString);
		int startLocal = 0;
		while (stringAtLocal >= 0) {
			resultString = resultString
					+ content.substring(startLocal, stringAtLocal) + newString;
			startLocal = stringAtLocal + oldString.length();
			stringAtLocal = content.indexOf(oldString, startLocal);
		}

		resultString = resultString
				+ content.substring(startLocal, content.length());
		return resultString;
	}

	public static String replaceStr(String strSource, final String strFrom,
			final String strTo) {
		if (strFrom == null || "".equals(strFrom)) {
			return strSource;
		}
		String strDest = "";
		final int intFromLen = strFrom.length();
		int intPos;
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			strDest = strDest + strSource.substring(0, intPos);
			strDest = strDest + strTo;
			strSource = strSource.substring(intPos + intFromLen);
		}
		strDest = strDest + strSource;
		return strDest;
	}

	public static String replaceStr(String source, String format, int from,
			int to) {
		if (source == null || "".equals(source)) {
			return source;
		}
		if (from > to) {
			return source;
		}
		final int sourceLen = source.length();
		if (sourceLen < from) {
			return source;
		}
		if (from < 0) {
			from = 0;
		}
		if (sourceLen - 1 <= to) {
			to = sourceLen - 1;
		}
		String dest = "";
		dest = dest + source.substring(0, from);
		for (int i = 0; i <= (to - from); i++) {
			dest = dest + format;
		}
		dest = dest + source.substring(to + 1, sourceLen);

		return dest;
	}

	public static String formatToHTML(final String strn) {
		final StringBuilder dest = new StringBuilder();
		if ("".equals(StrUtil.strnull(strn))) {
			dest.append("&nbsp;");
		} else {
			for (int i = 0; strn != null && i < strn.length(); i++) {
				final char c = strn.charAt(i);
				if (c == '\n') {
					dest.append("<br>");
				} else if (c == '\'') {
					dest.append("&#39;");
				} else if (c == '\"') {
					dest.append("&#34;");
				} else if (c == '<') {
					dest.append("&lt;");
				} else if (c == '>') {
					dest.append("&gt;");
				} else if (c == '&') {
					dest.append("&amp;");
				} else if (c == 0x20) {
					dest.append("&nbsp;");
				} else {
					dest.append(c);
				}
			}
		}
		return (dest.toString());
	}

	public static String formatToHTML(final String strn, final int length) {
		int m = 0;
		final StringBuilder dest = new StringBuilder();
		if ("".equals(StrUtil.strnull(strn))) {
			dest.append("&nbsp;");
		} else {
			for (int i = 0; strn != null && i < strn.length(); i++) {
				m++;
				if (m == length) {
					dest.append("...");
					break;
				}
				final char c = strn.charAt(i);
				if (c == '\n') {
					dest.append("<br>");
				} else if (c == '\'') {
					dest.append("&#39;");
				} else if (c == '\"') {
					dest.append("&#34;");
				} else if (c == '<') {
					dest.append("&lt;");
				} else if (c == '>') {
					dest.append("&gt;");
				} else if (c == '&') {
					dest.append("&amp;");
				} else if (c == 0x20) {
					dest.append("&nbsp;");
				} else {
					dest.append(c);
				}
			}
		}
		return (dest.toString());
	}

	public static String formatToHTML(final BigDecimal strb) {
		String strn = "";
		if (strb == null) {
			strn = "&nbsp;";
		} else {
			strn = StrUtil.strnull(strb);
		}
		return strn;
	}

	public static String nl2Br(final String source) {
		if ("".equals(StrUtil.strnull(source))) {
			return "&nbsp;";
		}
		final StringBuilder dest = new StringBuilder(source.length());
		for (int i = 0; i < source.length(); i++) {
			char c;
			c = source.charAt(i);
			if (c == '\n') {
				dest.append("<br>");
			} else if (c == 0x20) {
				dest.append("&nbsp;");
			} else {
				dest.append(c);
			}
		}
		return dest.toString();
	}

	public static boolean findString(final String sourceStr,
			final String fieldStr) {
		boolean strExist = false;
		if (sourceStr.length() == 0) {
			return strExist;
		}
		if (sourceStr.indexOf(fieldStr) >= 0) {
			strExist = true;
		}
		return strExist;
	}

	public static String getStackTrace(final Throwable exception) {
		final StringWriter sw = new StringWriter();
		return sw.toString();
	}

	public static String[] bubbleSort(final String[] arr) {
		int tag = 1;
		for (int i = 1; i < arr.length && tag == 1; i++) {
			tag = 0;
			for (int j = 0; j < arr.length - i; j++) {
				if (arr[j].compareTo(arr[j + 1]) > 0) {
					final String temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
					tag = 1;
				}
			}
		}
		return arr;
	}

	public static String[] bubbleSort(final String[] valueArr,
			final String[] contentArr) {
		int tag = 1;
		for (int i = 1; i < valueArr.length && tag == 1; i++) {
			tag = 0;
			for (int j = 0; j < valueArr.length - i; j++) {
				if (valueArr[j].compareTo(valueArr[j + 1]) > 0) {
					final String temp1 = valueArr[j];
					final String temp2 = contentArr[j];
					valueArr[j] = valueArr[j + 1];
					contentArr[j] = contentArr[j + 1];
					valueArr[j + 1] = temp1;
					contentArr[j + 1] = temp2;
					tag = 1;
				}
			}
		}
		return valueArr;
	}

	public static int[] bubbleSort(final int[] arr) {
		int tag = 1;
		for (int i = 1; i < arr.length && tag == 1; i++) {
			tag = 0;
			for (int j = 0; j < arr.length - i; j++) {
				if (arr[j] > arr[j + 1]) {
					final int temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
					tag = 1;
				}
			}
		}
		return arr;
	}

	public static String nullToZero(String str) {
		if (str == null || "".equals(str)) {
			str = "0";
		}
		return str;
	}

	public static String getPOFieldName(final String obName) {
		String aFieldName = obName;
		if (aFieldName == null) {
			return "";
		}
		aFieldName = aFieldName.toLowerCase();
		while (aFieldName.indexOf("_") >= 0) {
			if (aFieldName.indexOf("_") >= 0) {
				final int pos = aFieldName.indexOf("_");
				final String low = aFieldName.substring(0, pos);
				final String midd = aFieldName.substring(pos + 1, pos + 2)
						.toUpperCase();
				final String end = aFieldName.substring(pos + 2,
						aFieldName.length());
				aFieldName = low + midd + end;
			}
		} // end while
		return aFieldName;
	}

	public static String getPOTableName(final String obName) {
		String aTableName = obName;
		if (aTableName == null) {
			return "";
		}
		aTableName = aTableName.toLowerCase();
		while (aTableName.indexOf("_") >= 0) {
			if (aTableName.indexOf("_") >= 0) {
				final int pos = aTableName.indexOf("_");
				final String low = aTableName.substring(0, pos);
				final String midd = aTableName.substring(pos + 1, pos + 2)
						.toUpperCase();
				final String end = aTableName.substring(pos + 2,
						aTableName.length());
				aTableName = low + midd + end;
			}
		} // end while
		aTableName = aTableName.substring(0, 1).toUpperCase()
				+ aTableName.substring(1, aTableName.length());
		return aTableName;
	}

	public static String encodePassword(final String password,
			final String algorithm) {
		final byte[] unencodedPassword = password.getBytes();

		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (final Exception e) {
			LOG.error(StrUtil.class.getName(), e);
			return password;
		}

		md.reset();

		md.update(unencodedPassword);

		final byte[] encodedPassword = md.digest();

		final StringBuilder buf = new StringBuilder();

		for (final byte element : encodedPassword) {
			if ((element & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString(element & 0xff, 16));
		}

		return buf.toString();
	}

	public static String padTrailing(final String rString, final int rLength,
			final String rPad) {
		String lTmpPad = "";

		final String lTmpStr = StrUtil.strnull(rString);

		if (lTmpStr.length() >= rLength) {
			return lTmpStr.substring(0, lTmpStr.length());
		} else {
			for (int gCnt = 1; gCnt <= rLength - lTmpStr.length(); gCnt++) {
				lTmpPad = rPad + lTmpPad;
			}
		}
		return lTmpStr + lTmpPad;
	}

	public static String padLeading(final String rString, final int rLength,
			final String rPad) {
		String lTmpPad = "";

		final String lTmpStr = StrUtil.strnull(rString);

		if (lTmpStr.length() >= rLength) {
			return lTmpStr.substring(0, lTmpStr.length());
		} else {
			for (int gCnt = 1; gCnt <= rLength - lTmpStr.length(); gCnt++) {
				lTmpPad = lTmpPad + rPad;
			}
		}
		return lTmpPad + lTmpStr;
	}

	public static int contains(final String[] source, final String subStr) {
		for (int i = 0; i < source.length; i++) {
			if (source[i].equals(subStr)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isNullOrEmpty(final Object str) {
		boolean result = false;
		if (str == null || "null".equals(str)
				|| "".equals(str.toString().trim())) {
			result = true;
		}
		return result;
	}

	public static final String dateTimeToStr(String aDate) {
		String returnValue = "";
		if (aDate == null) {
			return "";
		}
		if (aDate.length() < 14) {
			StrUtil.padTrailing(aDate, 14, "0");
		}
		String str = aDate.replaceAll("-", "");
		String str1 = str.replaceAll(" ", "");
		String str2 = str1.replaceAll(":", "");
		returnValue = str2.substring(0, 14);

		return (returnValue);
	}

	public static final int getLength(String str) {
		return str == null ? 0 : str.length();
	}

	public static boolean isContainChnStr(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i, i + 1).matches("[\\u4e00-\\u9fbb]+")) {
				return true;
			}
		}
		return false;
	}

	public static String strConverFromHtml(String src) {
		StringBuilder sb = new StringBuilder();
		java.util.regex.Pattern p = java.util.regex.Pattern
				.compile("&[a-zA-Z]*;");
		Matcher m = p.matcher(src);
		int pos1 = 0;
		while (m.find(pos1)) {
			int pos2 = m.start();
			sb.append(src.substring(pos1, pos2));
			String entity = m.group().toLowerCase();
			if ("&#39;".equals(entity)) {
				sb.append("\'");
			} else if ("&#34;".equals(entity)) {
				sb.append("\"");
			} else if ("&lt;".equals(entity)) {
				sb.append("<");
			} else if ("&gt;".equals(entity)) {
				sb.append(">");
			} else if ("&nbsp;".equals(entity)) {
				sb.append(" ");
			} else if ("&amp;".equals(entity)) {
				sb.append("&");
			} else {
				sb.append("[UNKNOWN] ");
			}
			pos1 = m.end();
		}
		sb.append(src.substring(pos1));
		return sb.toString();
	}

	public static String getXmlContent(String inXML, String maskStartStr,
			String maskEndStr) {
		String outStr = "";
		int startIndex = -1;
		int endIndex = -1;

		if (inXML != null) {
			startIndex = inXML.indexOf(maskStartStr);
			endIndex = inXML.indexOf(maskEndStr);

			if (startIndex != -1) {
				int contentStart = inXML.indexOf('>', startIndex) + 1;
				outStr = inXML.substring(contentStart, endIndex);
			}
		}

		return outStr;
	}

	public static List<String> getSubXmlList(String inXml, String maskStartStr,
			String maskEndStr) {

		String tmp = inXml.replace(maskEndStr, maskStartStr);
		tmp += " ";
		String[] list = tmp.split(maskStartStr);
		List ret = new ArrayList<String>();
		for (int i = 0; i < list.length; i++) {
			if (i != 0 && i != list.length - 1 && !"".equals(list[i].trim())) {
				ret.add(list[i]);
			}
		}
		return ret;
	}

	public static String readXmlContent(String content, String element) {
		if (isNullOrEmpty(element) || isNullOrEmpty(content)) {
			return "";
		}
		StringBuilder startMark = new StringBuilder(element);
		startMark.insert(0, "<");
		startMark.append(">");
		StringBuilder endMark = new StringBuilder(startMark.toString());
		endMark.insert(1, "/");
		return StrUtil.getXmlContent(content, startMark.toString(),
				endMark.toString());
	}

	public static String strTrim(String source) {
		if (source == null) {
			return "";
		}

		String str = source.trim();
		return str;
	}

	public static String numtochinese(String input) {
		String s1 = "零壹贰叁肆伍陆柒捌玖";
		String s4 = "分角整元拾佰仟万拾佰仟亿拾佰仟";
		String temp = "";
		String result = "";
		if (input == null) {
			return "";
		}
		temp = input.trim();
		float f;
		try {
			f = Float.parseFloat(temp);

		} catch (Exception e) {
			LOG.error(StrUtil.class.getName(), e);
			return result;
		}
		int len = 0;
		if (temp.indexOf(".") == -1) {
			len = temp.length();
		} else {
			len = temp.indexOf(".");
		}
		if (len > s4.length() - 3) {
			return result;
		}
		int n1 = 0;
		String num = "";
		String unit = "";

		for (int i = 0; i < temp.length(); i++) {
			if (i > len + 2) {
				break;
			}
			if (i == len) {
				continue;
			}
			n1 = Integer.parseInt(String.valueOf(temp.charAt(i)));
			num = s1.substring(n1, n1 + 1);
			n1 = len - i + 2;
			unit = s4.substring(n1, n1 + 1);
			result = result.concat(num).concat(unit);
		}
		if ((len == temp.length()) || (len == temp.length() - 1)) {
			result = result.concat("整");
		}
		if (len == temp.length() - 2) {
			result = result.concat("零分");
		}
		return result;
	}

	public static String removeSpace(String str) {
		if (isNullOrEmpty(str)) {
			return "";
		}
		return str.replaceAll(" ", "");
	}

	public static String blob2string(Blob blob) {
		InputStream ins = null;
		String contentString = "";
		try {
			ins = blob.getBinaryStream();
			byte[] c = new byte[(int) blob.length()];
			ins.read(c);
			contentString = new String(c);

		} catch (SQLException e) {
			LOG.error(StrUtil.class.getName(), e);
		} catch (IOException e) {
			LOG.error(StrUtil.class.getName(), e);
		} finally {
			try {
				if (ins != null) {
					ins.close();
				}
			} catch (IOException e) {
				LOG.error(StrUtil.class.getName(), e);
			}
		}
		return contentString;
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString().toUpperCase();
		if (isNullOrEmpty(uuidStr)) {
			return "";
		}
		return uuidStr;
	}

	public static String wipeTransitionStr(String inXML) {
		inXML = inXML.replaceAll("&", "＆");
		return inXML;
	}

	public static String underScore2CamelCase(String strs) {
		String[] elems = strs.split("_");
		for (int i = 0; i < elems.length; i++) {
			elems[i] = elems[i].toLowerCase();
			String elem = elems[i];
			char first = elem.toCharArray()[0];
			elems[i] = "" + (char) (first - 32) + elem.substring(1);
		}
		StringBuilder sb = new StringBuilder();
		for (String e : elems) {
			sb.append(e);
		}
		return sb.toString();
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return "";
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || "".equals(hexString)) {
			return new byte[0];
		}
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789abcdef".indexOf(c);
	}

	public static String byteToString(byte[] b) {
		return byteToString(b, CHARSET_UTF8);
	}

	public static String byteToString(byte[] b, String charset) {

		if (charset == null || "".equals(charset)) {
			charset = CHARSET_UTF8;
		}

		try {
			return new String(b, charset);
		} catch (UnsupportedEncodingException e) {
			LOG.error(StrUtil.class.getName(), e);
			throw new RuntimeException("StringUtil stringTobyte exception : "
					+ e.getMessage());
		}
	}

	public static byte[] stringToByte(String str) {
		return stringToByte(str, CHARSET_UTF8);
	}

	public static byte[] stringToByte(String str, String charset) {

		if (str == null) {
			return new byte[0];
		}

		if (charset == null || "".equals(charset)) {
			charset = CHARSET_UTF8;
		}

		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			LOG.error(StrUtil.class.getName(), e);
			throw new RuntimeException("StringUtil stringTobyte exception : "
					+ e.getMessage());
		}
	}

	public static byte[] encode(String src, String charset) {
		if (src == null) {
			return null;
		}
		try {
			return src.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			return src.getBytes();
		}
	}

	public static int count(String str, String sql) {
		int count = 1;
		int index = 0;
		while ((index = sql.indexOf(str, index)) != -1) {
			index = index + str.length();
			count++;
		}
		return count;
	}
}

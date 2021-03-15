package com.bill.utils;

import java.io.UnsupportedEncodingException;

public class FileEncodingUtils
{
	//把iso格式中文转化成utf8
	public static String ISOtoUtf8(String iso){
		String utf8=null;
		try {
			//解决乱码问题, 判断当前字符串是否是使用ISO-8859-1码表, 如果不是就不会走到if里
			if (iso.equals(new String(iso.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
				utf8 = new String(iso.getBytes("ISO-8859-1"), "utf-8");
			}else {
				utf8=iso;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return utf8;
	}
}

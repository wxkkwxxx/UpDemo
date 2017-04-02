package com.wxk.baselibrary.log;

/**
 * 打印输出方法声明
 */
public interface LogUtilPrinterInterface {

	/**
	 * 得到设置的参数
	 */
	Settings getSettings();

	void d(Object message, String tag);

	void e(Object message, String tag);

	void w(Object message, String tag);

	void i(Object message, String tag);

	void v(Object message, String tag);

	void wtf(Object message, String tag);

	/**
	 * 打印Json
	 * @param json
	 */
	void json(String json, String tag);

	/**
	 * 打印Xml
	 * @param xml
	 */
	void xml(String xml, String tag);
	
	// 纯输出
	void printD(Object message, String tag);
	void printE(Object message, String tag);
}

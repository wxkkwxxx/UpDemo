package com.wxk.baselibrary.log;


import com.google.gson.Gson;

/**
 * @author 日志打印输出的工具类
 *
 */
public class LogUtils {
	private static boolean mIsDebug = true;
	private static final LogUtilPrinter printer = new LogUtilPrinter();
	private LogUtils() {

	}

	public static Settings initParam(boolean isDebug) {
		mIsDebug = isDebug;
		return printer.getSettings();
	}
	
	public static void d(String tag, Object message){
		if(!mIsDebug) return;
		printer.d(message, tag);
	}

	public static void e(String tag, Object message){
		if(!mIsDebug) return;
		printer.e(message, tag);
	}

	public static void w(String tag, Object message){
		if(!mIsDebug) return;
		printer.w(message, tag);
	}

	public static void i(String tag, Object message){
		if(!mIsDebug) return;
		printer.i(message, tag);
	}

	public static void v(String tag, Object message){
		if(!mIsDebug) return;
		printer.v(message, tag);
	}

	public static void wtf(String tag, Object message){
		if(!mIsDebug) return;
		printer.wtf(message, tag);
	}

	/**
	 * 打印Json
	 * @param json
	 */
	public static void json(String json){
		if(!mIsDebug) return;
		printer.json(json,"");
	}

	/**
	 * 打印Xml
	 * @param xml
	 */
	public static void xml(String xml){
		if(!mIsDebug) return;
		printer.xml(xml,"");
	}
	
	/**
	 * 打印Json
	 * @param json
	 */
	public static void json(String tag,String json){
		if(!mIsDebug) return;
		printer.json(json,tag);
	}

	/**
	 * 打印Xml
	 * @param xml
	 */
	public static void xml(String tag ,String xml){
		if(!mIsDebug) return;
		printer.xml(xml,tag);
	}
	
	/**
	 * 打印object
	 * @param object
	 */
	public static void object(Object object) {
        object("", object);
    }
	
	/**
	 * 打印object
	 * @param object
	 */
	public static void object(String tag,Object object) {
        if (object != null) {
        	String jsonStr = new Gson().toJson(object);
        	String classInfo = object.getClass().getSimpleName();
        	json(tag,classInfo+":"+jsonStr);
        }
    }
	
	// 纯输出
	public static void printD(String tag, Object message){
		if(!mIsDebug) return;
		printer.printD(message,tag);
	}

	public static void printE(String tag, Object message){
		if(!mIsDebug) return;
		printer.printE(message,tag);
	}
	
	public static void d(Object message){
		if(!mIsDebug) return;
		printer.d(message,"");
	}

	public static void e(Object message){
		if(!mIsDebug) return;
		printer.e(message,"");
	}

	public static void w(Object message){
		if(!mIsDebug) return;
		printer.w(message,"");
	}

	public static void i(Object message){
		if(!mIsDebug) return;
		printer.i(message,"");
	}

	public static void v(Object message){
		if(!mIsDebug) return;
		printer.v(message,"");
	}

	public static void wtf(Object message){
		if(!mIsDebug) return;
		printer.wtf(message,"");
	}
	
	// 纯输出
	public static void printD(Object message){
		if(!mIsDebug) return;
		printer.printD(message,"");
	}

	public static void printE(Object message){
		if(!mIsDebug) return;
		printer.printE(message,"");
	}
}

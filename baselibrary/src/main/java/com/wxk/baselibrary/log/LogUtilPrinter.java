package com.wxk.baselibrary.log;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 打印输出
 */
public final class LogUtilPrinter implements LogUtilPrinterInterface {
	public static String TAG = "PRETTYLOGGER";
	private static final char TOP_LEFT_CORNER = '╔';
	private static final char BOTTOM_LEFT_CORNER = '╚';
	private static final char HORIZONTAL_DOUBLE_LINE = '║';
	private static final char MIDDLE_CORNER = '╟';
	private static final String SINGLE_DIVIDER = "──────────────────────────────────";
	private static final String DOUBLE_DIVIDER = "══════════════════════════════════";
	private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER
			+ DOUBLE_DIVIDER;
	private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER
			+ SINGLE_DIVIDER;
	private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER
			+ DOUBLE_DIVIDER + DOUBLE_DIVIDER;
	/**
	 * 它用于json漂亮的打印
	 */
	private static final int JSON_INDENT = 4;

	// 参数设置
	private static final Settings settings = new Settings();

	@Override
	public Settings getSettings() {
		return settings;
	}

	@Override
	public void d(Object message, String tag) {
		log(Log.DEBUG, tag, message);
	}

	@Override
	public void e(Object message, String tag) {
		log(Log.ERROR, tag, message);
	}

	@Override
	public void w(Object message, String tag) {
		log(Log.WARN, tag, message);
	}

	@Override
	public void i(Object message, String tag) {
		log(Log.INFO, tag, message);
	}

	@Override
	public void v(Object message, String tag) {
		log(Log.VERBOSE, tag, message);
	}

	@Override
	public void wtf(Object message, String tag) {
		log(Log.ASSERT, tag, message);
	}

	@Override
	public void json(String json,String tag) {
		if (TextUtils.isEmpty(json)) {
			d("Empty/Null json content", "");
			return;
		}
		try {
			// object 也要公用这个方法
			int firstIndex = json.indexOf("{");
			if(firstIndex == -1){
				firstIndex = json.indexOf("[");
			}
			String classInfo = "";
			if(firstIndex > 0){
				classInfo = json.substring(0, firstIndex);
				json = json.substring(firstIndex, json.length());
			}
			
			// 如果打印的是对象
			if(!TextUtils.isEmpty(classInfo)){
				classInfo += System.getProperty("line.separator");
				json = json.replace("[]:", "");
			}
			
			if (json.startsWith("{")) {
				JSONObject jsonObject = new JSONObject(json);
				String message = jsonObject.toString(JSON_INDENT);
				d(classInfo+message, tag);
				return;
			}
			if (json.startsWith("[")) {
				JSONArray jsonArray = new JSONArray(json);
				String message = jsonArray.toString(JSON_INDENT);
				d(classInfo+message, tag);
			}
		} catch (JSONException e) {
			e(e.getCause().getMessage() + "\n" + json, "");
		}
	}

	@Override
	public synchronized void xml(String xml,String tag) {
		if (TextUtils.isEmpty(xml)) {
			d("Empty/Null xml content", tag);
			return;
		}
		try {
			Source xmlInput = new StreamSource(new StringReader(xml));
			StreamResult xmlOutput = new StreamResult(new StringWriter());
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(xmlInput, xmlOutput);
			d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"), "");
		} catch (TransformerException e) {
			e(e.getCause().getMessage() + "\n" + xml, tag);
		}
	}

	@Override
	public synchronized void printD(Object message, String tag) {
		Log.d(tag, String.valueOf(message));
	}

	@Override
	public synchronized void printE(Object message, String tag) {
		Log.e(tag, String.valueOf(message));
	}

	/**
	 * 打印内容
	 * 
	 * @param logType
	 * @param tag
	 * @param message
	 */
	private synchronized void logContentPart(int logType, String tag, String message) {
		logContent(logType, tag, message);
		logBottomBorder(logType, tag);
	}

	/**
	 * 画底部线
	 * 
	 * @param logType
	 * @param tag
	 */
	private synchronized void logBottomBorder(int logType, String tag) {
		logChunk(logType, tag, BOTTOM_BORDER);
	}

	/**
	 * 画内容部分
	 * 
	 * @param logType
	 * @param tag
	 * @param chunk
	 */
	private synchronized void logContent(int logType, String tag, String chunk) {
		String[] lines = chunk.split(System.getProperty("line.separator"));
		for (String line : lines) {
			logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " " + line);
		}
	}

	public synchronized void log(int logType, String tag, Object message) {
		logThreadPart(logType, tag);
		logMiddenInfoPart(logType, tag);
		logContentPart(logType, tag, String.valueOf(message));
	}

	/**
	 * 中间信息部分，行号，类名
	 * 
	 * @param debug
	 * @param string
	 */
	private synchronized void logMiddenInfoPart(int logType, String tag) {
		logChunk(logType, tag, generateFileTag());
		logDivider(logType, tag);
	}

	/**
	 * 得到文件信息，包括行号，文件，方法等等
	 * 
	 * @return
	 */
	private synchronized String generateFileTag() {
		StackTraceElement caller = getCallerStackTraceElement();
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName
				.lastIndexOf(".") + 1);
		StringBuilder builder = new StringBuilder();
		builder.append("║ ").append(callerClazzName).append(".")
				.append(caller.getMethodName()).append(" ").append(" (")
				.append(caller.getFileName()).append(":")
				.append(caller.getLineNumber()).append(")");
		return builder.toString();
	}

	

	/**
	 * 画进程部分
	 * 
	 * @param logType
	 * @param tag
	 */
	private synchronized void logThreadPart(int logType, String tag) {
		logTopBorder(logType, tag);
		logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " Thread: "
				+ Thread.currentThread().getName());
		logDivider(logType, tag);
	}

	/**
	 * 头部线
	 * 
	 * @param logType
	 * @param tag
	 */
	private synchronized void logTopBorder(int logType, String tag) {
		logChunk(logType, tag, TOP_BORDER);
	}

	/**
	 * 画中间的线
	 * 
	 * @param logType
	 * @param tag
	 */
	private synchronized void logDivider(int logType, String tag) {
		logChunk(logType, tag, MIDDLE_BORDER);
	}

	/**
	 * 根据类型输出打印
	 */
	private synchronized void logChunk(int logType, String tag, String chunk) {
		String finalTag = formatTag(tag);
		switch (logType) {
		case Log.ERROR:
			Log.e(finalTag, chunk);
			break;
		case Log.INFO:
			Log.i(finalTag, chunk);
			break;
		case Log.VERBOSE:
			Log.v(finalTag, chunk);
			break;
		case Log.WARN:
			Log.w(finalTag, chunk);
			break;
		case Log.ASSERT:
			Log.wtf(finalTag, chunk);
			break;
		case Log.DEBUG:
			// Fall through, log debug by default
		default:
			Log.d(finalTag, chunk);
			break;
		}
	}

	private static String formatTag(String tag) {
		if (!TextUtils.isEmpty(tag) && !TextUtils.equals(TAG, tag)) {
			return tag;
		}
		return TAG;
	}

	/**
	 * 返回一个数组StackTraceElement代表当前线程的堆栈
	 * 
	 * @return
	 */
	private StackTraceElement getCallerStackTraceElement() {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		return trace[getStackOffset(trace)];
	}
	
	/**
	   * Determines the starting index of the stack trace, after method calls made by this class.
	   *
	   * @param trace the stack trace
	   * @return the stack offset
	   */
	  private int getStackOffset(StackTraceElement[] trace) {
	    for (int i = 0; i < trace.length; i++) {
	      StackTraceElement e = trace[i];
	      String name = e.getClassName();
	      if (name.equals(LogUtils.class.getName())) {
	        return i+1;
	      }
	    }
	    return -1;
	  }
}

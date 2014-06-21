package tv.luxs.rcassistant.utils;

import tv.luxs.http.AsyncHttpClient;
import tv.luxs.http.AsyncHttpResponseHandler;
import tv.luxs.http.RequestParams;
import android.content.Context;
import android.telephony.TelephonyManager;

public class HttpUtils {

	public static AsyncHttpClient client = new AsyncHttpClient();
	private static final String URL0 = "";

	/**
	 * 获取设别串号
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static String getIME(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 提取IME到服务器
	 * 
	 * @param context
	 *            上下文
	 * @param handler
	 *            处理
	 */
	public static void PostIME(Context context, AsyncHttpResponseHandler handler) {
		String ime = getIME(context);
		RequestParams requestParams = new RequestParams();
		requestParams.put("ime", ime);
		client.post(URL0, requestParams, handler);

	}

}

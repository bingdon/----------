package tv.luxs.rcassistant.utils;

import org.json.JSONException;
import org.json.JSONObject;

import tv.luxs.http.AsyncHttpResponseHandler;
import tv.luxs.rcassistant.R;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;


/**
 * 升级工具类
 * 
 * @author lyl
 * 
 */
public class UpdateAppUtils {

	public static long id = 0;

	public static Context context;

	private static String getLoVersion() {

		PackageManager packageManager = UpdateAppUtils.context
				.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					UpdateAppUtils.context.getPackageName(), 0);
			String version = packageInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	private static AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(String content) {
			// TODO Auto-generated method stub
			super.onSuccess(content);
			parseUrl(content);
			// ToastUtils.showLong(AboutActivity.this,
			// getString(R.string.is_alreadly_version));
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			super.onFailure(error, content);
		}

	};

	private static void parseUrl(String content) {

		try {
			JSONObject jsonObject = new JSONObject(content);
			String url ="";
			String versionNum = "";

			String updateinfo = "";

			if (!versionNum.equals(getLoVersion())) {
				Toast.makeText(UpdateAppUtils.context, "有新版本", Toast.LENGTH_LONG).show();
				downDialog(url, updateinfo);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void downDialog(final String url, String info) {
		new AlertDialog.Builder(UpdateAppUtils.context)
				.setTitle(
						UpdateAppUtils.context.getResources().getString(
								R.string.downtitle))
				.setMessage(info)
				.setPositiveButton(R.string.download,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								downLoad(url);

							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private static void downLoad(String url) {
		DownloadManager downloadManager = (DownloadManager) UpdateAppUtils.context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(url);
		Request request = new Request(uri);
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
				| DownloadManager.Request.NETWORK_WIFI);
		request.setTitle(UpdateAppUtils.context.getResources().getString(
				R.string.app_name));
		id = downloadManager.enqueue(request);
	}

	/**
	 * 升级
	 * 
	 * @param context
	 */
	public static void upDateApp(Context context) {
		UpdateAppUtils.context = context;
		HttpUtils.getIME(context);
	}

}

package tv.luxs.rcassistant.receiver;

import tv.luxs.config.G;
import tv.luxs.rcassistant.RCActivity;
import tv.luxs.rcassistant.utils.IpUtils;
import tv.luxs.rcassistant.utils.OpenGpsUtils;
import tv.luxs.rcassistant.utils.SecConDialogUtils;
import tv.luxs.rcassistant.utils.Utils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 接收开启对话框广播接收器
 * 
 * @author lyl
 * 
 */
public class SecConReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(G.ACTION_CONNECT)&&!Utils.getIpconPreferences(context)) {
			showconNotice(RCActivity.activity);
		} else if (action.equals(G.ACTION_OPEN_GPS)
				&& !Utils.getGpsPreferences(RCActivity.activity)) {
//			OpenGpsUtils.OpenGpsDialog(RCActivity.activity);
		}else if (action.equals(G.ACTION_ERRO_PASSWORD)) {
			Toast.makeText(context, "推送密码错误", Toast.LENGTH_SHORT).show();
		}

	}

	private void showconNotice(Activity context) {
		if (IpUtils.getUpnpController().GetMR().size() != 0) {
			SecConDialogUtils.SecIPDialog("是否连接Ip来控制机顶盒", context);
		}
	}

}

package tv.luxs.rcassistant.utils;

import tv.luxs.rcassistant.R;
import tv.luxs.rcassistant.SettingConnectActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SecConDialogUtils {

	private static boolean isconnect=false;
	private static AlertDialog secConDialog;
	public static void SecIPDialog( String info,final Activity context) {
		View checkView=context.getLayoutInflater().inflate(R.layout.gps_sec, null);
		CheckBox checkBox=(CheckBox)checkView.findViewById(R.id.gpscheck);
		
		if (null!=secConDialog) {
			if (secConDialog.isShowing()) {
				return ;
			}
			
		}
		
		secConDialog=new AlertDialog.Builder(context)
				.setTitle(
						context.getResources().getString(
								R.string.connotice))
				.setMessage(info)
				.setView(checkView)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Utils.saveIpconPreferences(context, isconnect);
								context.startActivity(new Intent(context, SettingConnectActivity.class));
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Utils.saveIpconPreferences(context, isconnect);
								dialog.dismiss();
							}
						}).show();
		
		
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				isconnect=isChecked;
			}
		});
		
	}
	
}

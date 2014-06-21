package tv.luxs.rcassistant.utils;

import tv.luxs.rcassistant.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class OpenGpsUtils {
	private static  boolean isopen=false;
	private static AlertDialog openGpsDialog;
	public static void OpenGpsDialog(final Activity context) {
		
		View checkView=context.getLayoutInflater().inflate(R.layout.gps_sec, null);
		CheckBox checkBox=(CheckBox)checkView.findViewById(R.id.gpscheck);
		if (null!=openGpsDialog) {
			if (openGpsDialog.isShowing()) {
				return;
			}
		}
		openGpsDialog=new AlertDialog.Builder(context)
				.setTitle(
						context.getResources().getString(
								R.string.opengpsnotice))
				.setMessage(context.getResources().getString(R.string.gpsmessage))
				.setView(checkView)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Utils.saveGpsPreferences(context, isopen);
								showConnectGps(context);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Utils.saveGpsPreferences(context, isopen);
								dialog.dismiss();
							}
						}).show();
		
		
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				isopen=isChecked;
			}
		});
		
	}
	
	
	private static void showConnectGps(Activity context){
		 Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);   
        context.startActivityForResult(intent,0); 
	}
	
}

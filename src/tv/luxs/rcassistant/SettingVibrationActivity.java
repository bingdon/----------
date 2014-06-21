package tv.luxs.rcassistant;

import tv.luxs.rcassistant.setting.SettingManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class SettingVibrationActivity extends Activity {
	Context context;
	Activity activity;
	
	TextView value;
	
	SettingManager settingManager = new SettingManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_vibration);
		initUI();
		initData();
	}
	
	/**
	 * 初始化UI
	 */
	public void initUI(){
		context = this;
		activity = this;
		value = (TextView)findViewById(R.id.value);
	}
	
	/**
	 * 初始化数据
	 */
	public void initData(){
		int val = settingManager.getVibration(activity);
		value.setText(val + "");
	}
	
	/**
	 * 设置按键振动强度值
	 */
	public void setValue(int id){
		int val = settingManager.getVibration(activity);
		switch (id) {
		case R.id.btnMinus:
			if(val > 0) {
				value.setText((val - 1) + "");
				settingManager.setVibration(activity, val - 1);
			} 
			break;
		case R.id.btnPlus:
			if(val < 10) {
				value.setText((val + 1) + "");
				settingManager.setVibration(activity, val + 1);
			} 
			break;

		default:
			break;
		}
		settingManager.click(activity,clickHandler);
	}
	
	/**
	 * 振动返回
	 */
	Handler clickHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			settingManager.vibrate(activity);
		};
	};
	
	/**
	 * 点击事件
	 */
	public void doClick(View view){
		switch (view.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnMinus:
			setValue(R.id.btnMinus);
			break;
		case R.id.btnPlus:
			setValue(R.id.btnPlus);
			break;
		}
	}
}

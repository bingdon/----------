package tv.luxs.rcassistant;

import tv.luxs.config.G;
import tv.luxs.rcassistant.utils.MD5;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SuijiaMaActivity extends Activity implements
		OnCheckedChangeListener, OnClickListener {

	private static final String TAG = "SuijiaMaActivity";
	private RadioGroup suijiGroup;
	private RadioButton[] suijiButton = new RadioButton[5];
	private String[] code = new String[5];
	private Button canel;
	private Button ok;
	private static boolean bing = false;

	public static boolean isBing() {
		return bing;
	}

	public static void setBing(boolean bing) {
		SuijiaMaActivity.bing = bing;
	}

	private static String key = "";

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		SuijiaMaActivity.key = key;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_suijiama_sec);
		initView();
	}

	private void initView() {
		suijiGroup = (RadioGroup) findViewById(R.id.suijigroup);
		suijiButton[0] = (RadioButton) findViewById(R.id.radio0);
		suijiButton[1] = (RadioButton) findViewById(R.id.radio1);
		suijiButton[2] = (RadioButton) findViewById(R.id.radio2);
		suijiButton[3] = (RadioButton) findViewById(R.id.radio3);
		suijiButton[4] = (RadioButton) findViewById(R.id.radio4);
		ok = (Button) findViewById(R.id.okbtn);
		canel = (Button) findViewById(R.id.canelbtn);
		ok.setOnClickListener(this);
		canel.setOnClickListener(this);
		suijiGroup.setOnCheckedChangeListener(this);
		
		
		code[0] = G.CURRENT_VALUE;
		Log.i(TAG, "随机码:"+G.CURRENT_VALUE);
		int postion=(int) (Math.random()*5);
		Log.i(TAG, "位置:"+postion);
		for (int i = 0; i < 5; i++) {
			if (i==postion) {
				code[i] = G.CURRENT_VALUE;
				continue;
			}
			code[i] = MD5.MD5jm(G.CURRENT_VALUE + i).substring(0, 8);
		}

		for (int i = 0; i < 5; i++) {
			suijiButton[i].setText(code[i]);
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.radio0:
			key = code[0];
			break;

		case R.id.radio1:
			key = code[1];
			break;

		case R.id.radio2:
			key = code[2];
			break;

		case R.id.radio3:
			key = code[3];
			break;

		case R.id.radio4:
			key = code[4];
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.okbtn:
			bing = true;
			if (TextUtils.isEmpty(key)) {
				key=code[0];
			}
			Log.i(TAG, "KEY:"+key);
			enterBack();
			break;

		case R.id.canelbtn:
			enterBack();
			key=code[0];
			G.CURRENT_VALUE="";
			break;

		default:
			break;
		}
	}

	
	private void enterBack(){
		Intent intent=new Intent();
		intent.setClass(SuijiaMaActivity.this, SettingConnectActivity.class);
		startActivity(intent);
		finish();
	}
	
}

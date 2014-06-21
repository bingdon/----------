package tv.luxs.rcassistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * 注册
 * 
 * @author lyl
 * 
 */
public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
	}

}

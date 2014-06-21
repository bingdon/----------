package tv.luxs.rcassistant;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/**
 * 搜索
 * 
 * @author lyl
 * 
 */
public class MySearchActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Log.v("MySearchActivity", "======onCreate=====");
	}
}

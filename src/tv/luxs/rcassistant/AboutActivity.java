package tv.luxs.rcassistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AboutActivity extends Activity {

	private Button checkButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initView();
	}
	
	
	private void initView(){
		checkButton=(Button)findViewById(R.id.check_up);
		checkButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(AboutActivity.this, R.string.al_new_ver, Toast.LENGTH_LONG).show();
			}
		});
	}
	
}

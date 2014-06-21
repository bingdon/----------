package tv.luxs.rcassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * 预订
 * 
 * @author lyl
 * 
 */
public class YudingActivity extends Activity implements OnClickListener {

	private Button compleyudingButton;
	private Button gononyudingButton;
	private RelativeLayout jiudianLayout;
	private RelativeLayout canguanLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_yuding_info);
		initView();
	}

	private void initView() {

		Intent intent=getIntent();
		String nameString=intent.getStringExtra("yudingstyle");
		
		compleyudingButton = (Button) findViewById(R.id.yuding_info_comple_btn);
		gononyudingButton = (Button) findViewById(R.id.goondiancai_btn);
		
		jiudianLayout=(RelativeLayout)findViewById(R.id.yuding_bottom_layout);
		canguanLayout=(RelativeLayout)findViewById(R.id.yuding_bottom_layout2);
		
		if (nameString.equals(getResources().getString(R.string.hotel))) {
			canguanLayout.setVisibility(View.GONE);
		}else if (nameString.equals(getResources().getString(R.string.restaurant))) {
			jiudianLayout.setVisibility(View.GONE);
		}
		
		
		compleyudingButton.setOnClickListener(this);
		gononyudingButton.setOnClickListener(this);
	}

	/**
	 * 按钮点击事件
	 * 
	 * @param view
	 */
	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.order_back_img_btn:
			finish();
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
		case R.id.goondiancai_btn:
			enterNewActivity(id);
			break;

		case R.id.yuding_info_comple_btn:

			break;

		default:
			break;
		}
	}

	
	private void enterNewActivity(int id){
		Intent intent=new Intent();
		switch (id) {
		case R.id.goondiancai_btn:
			intent.setClass(YudingActivity.this, CaiDanActivity.class);
			break;

		default:
			break;
		}
		
		startActivity(intent);
		
	}
	
}

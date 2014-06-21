package tv.luxs.rcassistant;

import tv.luxs.imgutils.ImageFetcher;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 详情2
 * 
 * @author lyl
 * 
 */
public class BusinessInfo2Activity extends Activity implements OnClickListener {

	private TextView titleTextView;
	private Button yudingButton;
	private Button vipyudingButton;
	private String title;
	private ImageView infoImageView;
	private String headurl = "";
	private ImageFetcher mFetcher;
	private TextView infoTextView;
	private String infoString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_business_2info);
		initView();
	}

	private void initView() {
		mFetcher=new ImageFetcher(this, 200);
		mFetcher.setLoadingImage(R.drawable.hotel);
		title=getIntent().getStringExtra("name");
		headurl = getIntent().getStringExtra("imgurl");
		infoString=getIntent().getStringExtra("summary");
		titleTextView = (TextView) findViewById(R.id.business_2info_title_txt);
		titleTextView.setText(title);
		yudingButton = (Button) findViewById(R.id.business_2info_yuding_btn);
		vipyudingButton = (Button) findViewById(R.id.business_2info_vipyuding_btn);
		infoImageView=(ImageView)findViewById(R.id.business_2info_shangjia_img);
		infoTextView=(TextView)findViewById(R.id.business_2info_introduce_txt);
		
		if (!TextUtils.isEmpty(infoString)) {
			infoTextView.setText(infoString);
		}
		
		mFetcher.loadImage(headurl, infoImageView);
		yudingButton.setOnClickListener(this);
		vipyudingButton.setOnClickListener(this);
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
		Intent intent = new Intent();
		intent.putExtra("yudingstyle", "酒店");
		intent.setClass(BusinessInfo2Activity.this, YudingActivity.class);
		startActivity(intent);
	}

	
	
	
}

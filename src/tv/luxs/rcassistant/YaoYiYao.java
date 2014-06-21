package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;







import tv.luxs.adapter.YaoAdapter;
import tv.luxs.config.G;
import tv.luxs.rcassistant.utils.ShakeListener;
import tv.luxs.rcassistant.utils.ShakeListener.OnShakeListener;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 摇一摇
 * 
 * @author lyl
 * 
 */
public class YaoYiYao extends Activity {
	
	
	private static final String TAG="YaoYiYao";
	private LinearLayout linearLayout;
	private SensorManager sensorManager;
	private Vibrator vibrator;
	public static long[] bing = new long[4];
	static {
		bing[0] = 100;
		bing[1] = 300;
		bing[2] = 100;
		bing[3] = 300;
	}

	private AlphaAnimation mAlphaAnimation;

//	private GridView yaoGridView;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//	private YaoAdapter yaoAdapter;
	private RelativeLayout yaoLayout;
	private ImageView yaopic;
	private int[] imgs = { R.drawable.img0, R.drawable.img1, R.drawable.img2,
			R.drawable.img3, R.drawable.img5, R.drawable.img6, R.drawable.img7,
			R.drawable.img8 };
//	private AbsoluteLayout mAbsoluteLayout;

	private ImageView[] imageViews = new ImageView[5];

	private TextView notice;

	private int width = 0;
	private int height = 0;

	private List<Map<String, Integer>> postionList = new ArrayList<Map<String, Integer>>();
	//大按键位置
	private List<Map<String, Integer>> bigpostionList = new ArrayList<Map<String, Integer>>();
	//小按键位置
	private List<Map<String, Integer>> smallpostionList = new ArrayList<Map<String, Integer>>();
	private AbsoluteLayout tagLayout;
	private Button[] buttons=new Button[7];
	private ShakeListener shakeListener;
	//加载多个动画
	private AnimationSet mAnimationSet=new AnimationSet(true);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yaoyiyao);
		initView();
		initSenor();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		linearLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		sensorManager.unregisterListener(bingEventListener);
		shakeListener.stop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		shakeListener.start();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		shakeListener.stop();
	}
	
	private void initView() {
		tagLayout=(AbsoluteLayout)findViewById(R.id.yao_tag_layout);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		Log.i(TAG, "y:"+tagLayout.getBaseline());
		Log.i(TAG, "y:"+tagLayout.getHeight());
		Log.i(TAG, "y:"+tagLayout.getBottom());
		Log.i(TAG, "y:"+tagLayout.getMeasuredHeight());
		Log.i(TAG, "y:"+tagLayout.getRootView().getHeight());
		getPosition();
		
		
		buttons[0] = (Button) findViewById(R.id.button1);
		buttons[1]= (Button) findViewById(R.id.button2);
		buttons[2] = (Button) findViewById(R.id.button3);
		buttons[3] = (Button) findViewById(R.id.button4);
		buttons[4] = (Button) findViewById(R.id.button5);
		buttons[5]=(Button) findViewById(R.id.button6);
		buttons[6]=(Button) findViewById(R.id.button7);
		getNewPosition();
		for (int i = 0; i < 7; i++) {
			buttons[i].setOnClickListener(bingClickListener);
		}
		linearLayout = (LinearLayout) findViewById(R.id.yao_my_line);
		yaoLayout = (RelativeLayout) findViewById(R.id.center_layout);
		yaopic = (ImageView) findViewById(R.id.yao_center_imag);

		mAlphaAnimation = new AlphaAnimation(0, 1.0f);
		mAlphaAnimation.setDuration(3000);

		findViewById(R.id.yao_notice_txt).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

//						changePostion();
//						changenewPostion();
						
												
						
					}
				});

	}

	private void initSenor() {
//		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

//		sensorManager.registerListener(bingEventListener,
//				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//				SensorManager.SENSOR_DELAY_NORMAL);
		
		shakeListener=new ShakeListener(YaoYiYao.this);
		shakeListener.setOnShake(mShakeListener);
		

	}

	private SensorEventListener bingEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			float values[] = event.values;
			float x = values[0];
			float y = values[1];
			float z = values[2];

			if (Math.abs(x) > 15 || Math.abs(y) > 15 || Math.abs(z) > 15) {
				vibrator.vibrate(bing, -1);
				linearLayout.setVisibility(View.VISIBLE);
				yaoLayout.setVisibility(View.VISIBLE);
				new BingAsyncTask().execute(0);
				Animation mAnimation=AnimationUtils.loadAnimation(YaoYiYao.this, R.anim.shake);
				yaoLayout.startAnimation(mAnimation);

			}

			// Log.i("摇晃", "指数:"+max);

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};

	private void sendbrocast() {

		Intent intent = new Intent();
		intent.setAction(G.ACTION_YAOYIYAO);
		sendBroadcast(intent);

	}

	public class BingAsyncTask extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(5000);
				publishProgress(params);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			shakeListener.start();
			int index = (int) (Math.random() * imgs.length);
			yaopic.setImageResource(imgs[index]);
			linearLayout.setVisibility(View.GONE);
			yaoLayout.startAnimation(mAlphaAnimation);

		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (yaoLayout.isShown()) {
				yaoLayout.setVisibility(View.GONE);
				linearLayout.setVisibility(View.GONE);
				tagLayout.setVisibility(View.VISIBLE);
				return true;
			}

		}

		return super.onKeyDown(keyCode, event);
	}


	private void getPosition() {
		Log.i("位置", "x:" + width + "y:" + height);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {

				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("x", i * width / 4);
				map.put("y", j * height / 4);
				postionList.add(map);
			}

		}
	}
	
	
	private void changePostion(){
		Collections.shuffle(postionList);
		for (int i = 0; i < 7; i++) {
			setPostion(postionList.get(i).get("x"), postionList.get(i).get("y"),
					buttons[i]);
		}
	}
	
	
	private void changenewPostion(){
		linearLayout.setVisibility(View.VISIBLE);
		Collections.shuffle(bigpostionList);
		Log.i(TAG, "坐标:"+bigpostionList.size());
		setPostion(bigpostionList.get(0).get("x"), bigpostionList.get(0).get("y"),
				buttons[0]);
		Log.i(TAG, "大坐标:"+bigpostionList.get(0));
		
		setPostion(bigpostionList.get(1).get("x"), bigpostionList.get(1).get("y"),
				buttons[2]);
		Log.i(TAG, "大坐标:"+bigpostionList.get(1));
		setPostion(bigpostionList.get(2).get("x"), bigpostionList.get(2).get("y"),
				buttons[5]);
		Log.i(TAG, "大坐标:"+bigpostionList.get(2));
		
		
		Collections.shuffle(smallpostionList);
		setPostion(smallpostionList.get(0).get("x"), smallpostionList.get(0).get("y"),
				buttons[1]);
		Log.i(TAG, "小坐标:"+smallpostionList.get(0));
		setPostion(smallpostionList.get(1).get("x"), smallpostionList.get(1).get("y"),
				buttons[3]);
		Log.i(TAG, "小坐标:"+smallpostionList.get(1));
		setPostion(smallpostionList.get(2).get("x"), smallpostionList.get(2).get("y"),
				buttons[4]);
		Log.i(TAG, "小坐标:"+smallpostionList.get(2));
		setPostion(smallpostionList.get(3).get("x"), smallpostionList.get(3).get("y"),
				buttons[6]);
		Log.i(TAG, "小坐标:"+smallpostionList.get(3));
		
		
		
	}
	
	
	private void setPostion(final int x, final int y, final Button button) {
		TranslateAnimation translateAnimation2 = new TranslateAnimation(
				0,x-button.getLeft(), 0, y-button.getTop());
		translateAnimation2.setDuration(3000);
		AnimationSet animationSet=new AnimationSet(true);
		animationSet.addAnimation(translateAnimation2);
		animationSet.addAnimation(mAlphaAnimation);
		animationSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				button.layout(x, y, x + button.getWidth(), y + button.getHeight());
				shakeListener.start();
			}
		});
//		translateAnimation2.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				// TODO Auto-generated method stub
////				button.startAnimation(mAlphaAnimation);
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				// TODO Auto-generated method stub
//				button.layout(x, y, x + button.getWidth(), y + button.getHeight());
//				
//			}
//		});
		button.startAnimation(animationSet);
		
		linearLayout.setVisibility(View.INVISIBLE);
	}
	
	
	private OnClickListener bingClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			tagLayout.setVisibility(View.INVISIBLE);
//			yaoLayout.setVisibility(View.VISIBLE);
			
			startActivity(new Intent(YaoYiYao.this, Yao2yao.class));

		}
	};
	
	
	private OnShakeListener mShakeListener=new OnShakeListener() {
		
		@Override
		public void onShake() {
			// TODO Auto-generated method stub
			shakeListener.stop();
			vibrator.vibrate(bing, -1);
			if (tagLayout.isShown()) {
				changenewPostion();
			} else {
				linearLayout.setVisibility(View.VISIBLE);
				yaoLayout.setVisibility(View.VISIBLE);
				yaopic.setImageResource(R.drawable.help);
				new BingAsyncTask().execute(0);
				Animation mAnimation=AnimationUtils.loadAnimation(YaoYiYao.this, R.anim.shake);
				yaoLayout.startAnimation(mAnimation);
			}
			
		}
	};
	
	
	private void getNewPosition(){
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("x", (int) (width*0.15));
		map.put("y", (int) (height*0.103));
		bigpostionList.add(map);
		
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		map1.put("x", (int) (width*0.309));
		map1.put("y", (int) (height*0.35));
		bigpostionList.add(map1);
		
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		map2.put("x", (int) (width*0.124));
		map2.put("y", (int) (height*0.507));
		bigpostionList.add(map2);
		
		for (int i = 0; i < bigpostionList.size(); i++) {
			Log.i(TAG, "坐标:"+bigpostionList.get(i).toString());
		}
		
//		for (int i = 0; i < 7; i++) {
//			if (i==0||i==2||i==5) {
//				continue;
//			}
//			Map<String, Integer> map3 = new HashMap<String, Integer>();
//			map.put("x", buttons[i].getLeft());
//			map.put("y", buttons[i].getTop());
//			smallpostionList.add(map3);
//		}
		
		Map<String, Integer> map3 = new HashMap<String, Integer>();
		map3.put("x", (int) (width*0.7));
		map3.put("y", height/9);
		smallpostionList.add(map3);
		
		Map<String, Integer> map4 = new HashMap<String, Integer>();
		map4.put("x", (int) (width*0.82));
		map4.put("y", (int) (height*0.2));
		smallpostionList.add(map4);
		
		Map<String, Integer> map5 = new HashMap<String, Integer>();
		map5.put("x", (int) (width*0.83));
		map5.put("y", (int) (height*0.46));
		smallpostionList.add(map5);
		
		Map<String, Integer> map6 = new HashMap<String, Integer>();
		map6.put("x", (int) (width*0.6));
		map6.put("y", (int) (height*0.58));
		smallpostionList.add(map6);
		
		
		
	}
	
}

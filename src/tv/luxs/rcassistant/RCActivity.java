package tv.luxs.rcassistant;

import tv.luxs.config.G;
import tv.luxs.rcassistant.connect.ConnectManager;
import tv.luxs.rcassistant.connect.ServerManager;
import tv.luxs.rcassistant.setting.SettingManager;
import tv.luxs.rcassistant.utils.CodeUtils;
import tv.luxs.rcassistant.utils.ShareUtlis;
import tv.luxs.rcassistant.utils.Utils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class RCActivity extends Activity {
	Context context;
	public static Activity activity;

	FrameLayout wrapper;

	final static int RC = 1;
	final static int DP = 2;
	float startX = 0;
	float startY = 0;
	float endX = 0;
	float endY = 0;
	int SENS_WEIDTH = 50;
	int SENS_HEIGHT = 300;
	boolean isClick = true;
	SettingManager settingManager = new SettingManager();

	String host;
	boolean isConnecting = false;
	ServerManager serverManager = new ServerManager();

	GestureDetector mGestureDetector;

	private long mytime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (G.ACTIVITY_CURRENT_RC == RC)
			setContentView(R.layout.activity_rc);
		else if (G.ACTIVITY_CURRENT_RC == DP)
			setContentView(R.layout.activity_dp);
		initUI();
		serverManager.setActivity(this);
		// 自动连接
		
		if (!MainActivityt.maint) {
			connect();
			setMenu();
		} else {
			setMenut();
		}

		addAction();

		mGestureDetector = new GestureDetector(this, bingGestureListener);
		connectFirst();
	}

	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("view", G.ACTIVITY_CURRENT_RC);
		Log.i("控制", "保存");
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		Log.i("控制", "取出");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mGestureDetector.onTouchEvent(ev);
		super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ConnectManager.close();
		unregisterReceiver(homeReceiver);
	}

	/**
	 * 初始化UI
	 */
	public void initUI() {
		context = this;
		activity = this;
	}

	/**
	 * 连接
	 */
	public void connect() {
		Utils.log("isConnecting" + isConnecting);
		if (isConnecting == true)
			return;

		
		
		// 获取默认机顶盒IP
		host = serverManager.getHostIP(context);
		if (TextUtils.isEmpty(G.CURRENT_VALUE)) {
			CodeUtils.getSuijiCode(RCActivity.this);
		}
		if (host == null) {
			Utils.toActivity(activity, SettingConnectActivity.class);
			return;
		}

		if (TextUtils.isEmpty(G.CURRENT_VALUE)) {
			Utils.toActivity(activity, SettingConnectActivity.class);
		}

		Log.i("控制", "随机码:"+G.CURRENT_VALUE);
		
		// 是否已连接？
		boolean isConnected = ConnectManager.isConnected();
		Utils.log("isConnected" + isConnected);
		if (isConnected == true) {
			return;
		} 
		else {
			serverManager.setHostStatus(context, host, 0);
			Utils.toActivity(activity, SettingConnectActivity.class);
			
		}

		new Thread() {
			public void run() {
				isConnecting = true;
				boolean result = ConnectManager.connectServer(host);
				if (result) {
					Message msg = connectHandler.obtainMessage();
					msg.what = G.LOAD_SUCCESS;
					msg.sendToTarget();
				} else {
					Message msg = connectHandler.obtainMessage();
					msg.what = G.LOAD_FAILED;
					msg.sendToTarget();
				}
			};
		}.start();
	}
	
	
	/**
	 * 连接
	 */
	public void connectFirst() {
		Utils.log("isConnecting" + isConnecting);
		if (isConnecting == true)
			return;

		// 获取默认机顶盒IP
		host = serverManager.getHostIP(context);
		if (TextUtils.isEmpty(G.CURRENT_VALUE)) {
			CodeUtils.getSuijiCode(RCActivity.this);
		}
		if (host == null) {
			
			return;
		}

		if (TextUtils.isEmpty(G.CURRENT_VALUE)) {
			return;
		}

		// 是否已连接？
		boolean isConnected = ConnectManager.isConnected();
		Utils.log("isConnected" + isConnected);
		if (isConnected == true) {
			return;
		} else {
//			Utils.toActivity(activity, SettingConnectActivity.class);
			serverManager.setHostStatus(context, host, 0);
		}

		new Thread() {
			public void run() {
				isConnecting = true;
				boolean result = ConnectManager.connectServer(host);
				if (result) {
					Message msg = connectHandler.obtainMessage();
					msg.what = G.LOAD_SUCCESS;
					msg.sendToTarget();
				} else {
					Message msg = connectHandler.obtainMessage();
					msg.what = 100;
					msg.sendToTarget();
				}
			};
		}.start();
	}
	

	/**
	 * 连接后更新UI
	 */
	Handler connectHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case G.LOAD_SUCCESS:
				serverManager.setHostStatus(context, host, 1);
				// Utils.toast(activity,"连接 " + host+ " 成功！");
				break;
			case G.LOAD_FAILED:
				serverManager.setHostStatus(context, host, 0);
				Utils.toActivity(activity, SettingConnectActivity.class);
				break;
				
			case 100:
				serverManager.setHostStatus(context, host, 0);
//				Utils.toActivity(activity, SettingConnectActivity.class);
				break;	
				
			default:
				break;
			}
			isConnecting = false;
		};
	};

	/**
	 * 执行点击
	 */
	public void excute(int data) {
		if (TextUtils.isEmpty(G.CURRENT_VALUE)) {
			CodeUtils.getSuijiCode(RCActivity.this);
		}
		settingManager.click(activity, handler);
		if (isConnecting == true) {

			Utils.toast(
					activity,
					"机顶盒连接不上！\n1.请检查机顶盒是否开启?\n2.手机WIFI是否与机顶盒在同一网段?\n或者点击乐更多 > 乐遥控设置 > 连接机顶盒中重新扫描连接！");
			return;
		}
		new SendThread(data + "").start();
	}

	/**
	 * 发送消息线程
	 */
	class SendThread extends Thread {
		String data;

		public SendThread(String data) {
			this.data = data;
		}

		@Override
		public void run() {
			boolean isSended = ConnectManager.send(data);
			Utils.log("isSended:" + isSended);
			Message msg = handler.obtainMessage();
			msg.what = G.SEND;
			msg.obj = isSended;
			msg.sendToTarget();
		};
	}

	/**
	 * 发送返回
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			settingManager.vibrate(activity);
			switch (msg.what) {
			case G.SEND:
				boolean isSended = (Boolean) msg.obj;
				if (!isSended) {
					connect();
//					Utils.toActivity(activity, SettingConnectActivity.class);
				}
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 切换功能和数字面板
	 */
	public void switchPanel() {
		if (G.ACTIVITY_CURRENT_RC == RC) {
			setContentView(R.layout.activity_dp);
			G.ACTIVITY_CURRENT_RC = DP;
		} else if (G.ACTIVITY_CURRENT_RC == DP) {
			setContentView(R.layout.activity_rc);
			G.ACTIVITY_CURRENT_RC = RC;
		}
		
		setMenut();
		
	}

	/**
	 * 触摸
	 */
	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// startX = event.getX();
	// startY = event.getY();
	// Utils.log("按下: x=" + event.getX() + "y=" + event.getY());
	// break;
	// case MotionEvent.ACTION_MOVE:
	// isClick = false;
	// Utils.log("移动: x=" + event.getX() + "y=" + event.getY());
	// break;
	// case MotionEvent.ACTION_UP:
	// endX = event.getX();
	// endY = event.getY();
	//
	// float x = 0;
	// if(startX >= endX) x = startX - endX;
	// else x = endX - startX;
	//
	// float y = 0;
	// if(startY >= endY) y = startY - endY;
	// else y = endY - startY;
	// Utils.log("x:" + x + " y:" + y);
	//
	// //上/下/左/右键映射
	// if(x > y && y <= SENS_WEIDTH){
	// if(startX > endX + SENS_HEIGHT){
	// excute(G.VALUE_LEFT);
	// Utils.toast(activity, "keyCode:" + G.VALUE_LEFT);
	// }else if(startX < (endX - SENS_HEIGHT)){
	// excute(G.VALUE_RIGHT);
	// Utils.toast(activity, "keyCode:" + G.VALUE_RIGHT);
	// }
	// }else if(x < y && x <= SENS_WEIDTH){
	// if(startY > endY + SENS_HEIGHT){
	// excute(G.VALUE_UP);
	// Utils.toast(activity, "keyCode:" + G.VALUE_UP);
	// }else if(startY < (endY - SENS_HEIGHT)){
	// excute(G.VALUE_BOTTOM);
	// Utils.toast(activity, "keyCode:" + G.VALUE_BOTTOM);
	// }
	// }
	//
	// Utils.log("抬起: x=" + event.getX() + "y=" + event.getY());
	// break;
	// }
	// return true;
	// }

	/**
	 * 点击事件
	 */
	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.btnMenu:
			excute(G.VALUE_MENU);
			break;
		case R.id.btnSwitch:
			switchPanel();
			break;
		case R.id.btnHome:
			excute(G.VALUE_HOME);
			break;
		case R.id.btnMusic:
			excute(G.VALUE_MUSIC);
			break;
		case R.id.btnMouse:
			Utils.toActivity(activity, MouseKeyboardActivity.class);
			break;
		case R.id.btnBack:
			excute(G.VALUE_BACK);
			break;
		case R.id.btnOk:
			excute(G.VALUE_OK);
			break;
		case R.id.btnUp:
			excute(G.VALUE_UP);
			break;
		case R.id.btnBottom:
			excute(G.VALUE_BOTTOM);
			break;
		case R.id.btnRight:
			excute(G.VALUE_RIGHT);
			break;
		case R.id.btnLeft:
			excute(G.VALUE_LEFT);
			break;
		case R.id.btnRed:
			excute(G.VALUE_RED);
			break;
		case R.id.btnYellow:
			excute(G.VALUE_YELLOW);
			break;
		case R.id.btnBlue:
			excute(G.VALUE_BLUE);
			break;
		case R.id.btnGreen:
			excute(G.VALUE_GREEN);
			break;

		case R.id.btn1:
			excute(G.VALUE_1);
			break;
		case R.id.btn2:
			excute(G.VALUE_2);
			break;
		case R.id.btn3:
			excute(G.VALUE_3);
			break;
		case R.id.btn4:
			excute(G.VALUE_4);
			break;
		case R.id.btn5:
			excute(G.VALUE_5);
			break;
		case R.id.btn6:
			excute(G.VALUE_6);
			break;
		case R.id.btn7:
			excute(G.VALUE_7);
			break;
		case R.id.btn8:
			excute(G.VALUE_8);
			break;
		case R.id.btn9:
			excute(G.VALUE_9);
			break;
		case R.id.btn0:
			excute(G.VALUE_0);
			break;
		case R.id.btnVoiceUp:
			excute(G.VALUE_VOLUME_UP);
			break;
		case R.id.btnVoiceDown:
			excute(G.VALUE_VOLUME_DOWN);
			break;
		case R.id.btnMute:
			excute(G.VALUE_MUTE);
			break;
		case R.id.btnCannelUp:
			excute(G.VALUE_PLUS);
			break;
		case R.id.btnCannelDown:
			excute(G.VALUE_MINUS);
			break;
		case R.id.btnQrcode:
			Intent intent = new Intent();
			intent.putExtra("type", 1);
			intent.setClass(context, SettingConnectActivity.class);
			startActivity(intent);
			break;
		}
	}

	/**
	 * 返回主页接收
	 */
	private BroadcastReceiver homeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if (action.equals(G.SEND_BACK_HOME)) {
				excute(G.VALUE_BACK);
				excute(G.VALUE_HOME);
			}else if (action.equals(G.ACTION_SHARE_)&&!Utils.getSharePreferences(activity)) {
				ShareUtlis.shareDailog(activity);
			}
			
		}
	};

	/**
	 * 注册广播
	 */
	private void addAction() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(G.SEND_BACK_HOME);
		filter.addAction(G.ACTION_SHARE_);
		registerReceiver(homeReceiver, filter);
	}

	private OnGestureListener bingGestureListener = new OnGestureListener() {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			Log.i("onSingleTapUp", "MotionEvent:" + e.getAction());
			if (System.currentTimeMillis() - mytime > 1000) {

			} else {
				excute(G.VALUE_OK);
			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			Log.i("onShowPress", "MotionEvent:" + e.getAction());
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub

			float x1 = e1.getX();
			float x2 = e2.getX();
			float y1 = e1.getY();
			float y2 = e2.getY();

			float xx = x1 - x2;
			float yy = y1 - y2;
			float bing = Math.max(Math.abs(xx), Math.abs(yy));
			if (bing < 100) {

				return false;
			}
			mytime = System.currentTimeMillis();
			if (Math.abs(xx) > Math.abs(yy)) {

				sendlor(xx);

			} else {
				senduod(yy);
			}

			Log.i("手势", "X轴:" + (x1 - x2));
			Log.i("手势", "Y轴:" + (y1 - y2));

			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			Log.i("onDown", "MotionEvent:" + e.getAction());
			return false;
		}
	};

	private void sendlor(float xx) {

		if (xx > 0) {
			excute(G.VALUE_LEFT);
		} else {
			excute(G.VALUE_RIGHT);
		}

	}

	private void senduod(float yy) {
		if (yy > 0) {
			excute(G.VALUE_UP);
		} else {
			excute(G.VALUE_BOTTOM);
		}
	}

	private void setMenu() {
		ImageView menu = (ImageView) findViewById(R.id.menu_img);
		MainActivity.popupMenus[0] = new PopupMenu(RCActivity.this, menu);
		MainActivity.popupMenus[0].inflate(R.menu.le_more);
		MainActivity.popupMenus[0]
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						switch (item.getItemId()) {
						case R.id.le_more:
							intent.setClass(RCActivity.this, MoreActivity.class);
							startActivity(intent);
							break;

						case R.id.isao:
							intent.setClass(RCActivity.this,
									SettingConnectActivity.class);
							startActivity(intent);
							break;

						case R.id.le_vip_m:

							intent.setClass(RCActivity.this,
									LeVipActivity.class);
							startActivity(intent);
							break;
						case R.id.leshare:

							ShareUtlis.share2Fre(context);

							break;

						default:
							break;
						}
						return false;
					}
				});

		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.popupMenus[0].show();
			}
		});

	}

	private void setMenut() {
		ImageView menu = (ImageView) findViewById(R.id.menu_img);
		MainActivityt.popupMenus[0] = new PopupMenu(RCActivity.this, menu);
		MainActivityt.popupMenus[0].inflate(R.menu.more_menu);
		MainActivityt.popupMenus[0]
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						switch (item.getItemId()) {
						case R.id.le_more:
							intent.setClass(RCActivity.this, MoreActivity.class);
							startActivity(intent);
							break;

						case R.id.isao:
							intent.setClass(RCActivity.this,
									SettingConnectActivity.class);
							startActivity(intent);
							break;
						case R.id.le_vip_m:

							intent.setClass(RCActivity.this,
									LeVipActivity.class);
							startActivity(intent);
							break;

						case R.id.leshare:

							ShareUtlis.share2Fre(context);

							break;

						default:
							break;
						}
						return false;
					}
				});

		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivityt.popupMenus[0].show();
			}
		});

	}

}

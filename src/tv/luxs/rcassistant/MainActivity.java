package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.List;

import cat.projects.dmc_service.DMCMediaService;
import tv.luxs.rcassistant.connect.ServerManager;
import tv.luxs.rcassistant.utils.NetUtils;
import tv.luxs.rcassistant.utils.ShareUtlis;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * 主页面
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	Context context;
	Activity activity;

	ImageView tabIcon1;
	ImageView tabIcon2;
	ImageView tabIcon3;
	ImageView tabIcon4;
	ImageView tabIcon5;
	TextView tabName1;
	TextView tabName2;
	TextView tabName3;
	TextView tabName4;
	TextView tabName5;
	private TabHost tabHost;

	ServerManager serverManager = new ServerManager();

	public static PopupMenu[] popupMenus = new PopupMenu[4];

	public FrameLayout mainFrameLayout;
	// 浮动标签
	private Button fudongButton;
	
	private List<String> wifiList=new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		activity = this;
		serverManager.setActivity(this);
		mainFrameLayout = (FrameLayout) findViewById(R.id.main_fra);
		
		initWifiList();
		initTabHost();
		Intent intent = new Intent(this, DMCMediaService.class);
		startService(intent);

		if (NetUtils.isConnect(context)) {
			mHandler.sendEmptyMessageDelayed(0, 2000);
			Log.i("Mainactivity", "名称:" + NetUtils.getWifiName(context));
			Log.i("Mainactivity", "名称:" + NetUtils.getWifiName(context));

		}

		fudongButton = (Button) findViewById(R.id.fudong_btn);
		
		String wifiname=NetUtils.getWifiName(context);
		
		if (NetUtils.isConnect(context)) {
			Log.i("Mainactivity", "名称:" + wifiname);
			if (hasBusiness(wifiname)) {
				Log.i("Mainactivity", "名称:" + wifiname);
				fudongButton.setVisibility(View.VISIBLE);
			}
		}

//		fudongButton.setVisibility(View.VISIBLE);
		
		fudongButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(activity, MainActivityt.class));
			}
		});
		// fudongButton.setOnTouchListener(fudong);

	}

	/**
	 * 初始化导航栏
	 */
	private void initTabHost() {
		tabHost = getTabHost();
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		// View tabView1 = layoutInflater.inflate(R.layout.main_tab, null);
		// tabIcon1 = (ImageView) tabView1.findViewById(R.id.tabIcon);
		// tabIcon1.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.icon_nav_le));
		// tabName1 = (TextView) tabView1.findViewById(R.id.tabName);
		// tabName1.setText(R.string.le);
		// TabSpec tabSpec1 = tabHost.newTabSpec("le");
		// Intent tabIntent1 = new Intent();
		// tabIntent1.setClass(context, LeActivity.class);
		// tabSpec1.setContent(tabIntent1);
		// tabSpec1.setIndicator(tabView1);
		// tabHost.addTab(tabSpec1);

		View tabView1 = layoutInflater.inflate(R.layout.main_tab, null);
		tabIcon1 = (ImageView) tabView1.findViewById(R.id.tabIcon);
		tabIcon1.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.icon_nav_user));
		tabName1 = (TextView) tabView1.findViewById(R.id.tabName);
		tabName1.setText(R.string.user);
		TabSpec tabSpec1 = tabHost.newTabSpec("user");
		Intent tabIntent1 = new Intent();
		tabIntent1.setClass(context, LeVipActivity.class);
		tabSpec1.setContent(tabIntent1);
		tabSpec1.setIndicator(tabView1);
		tabHost.addTab(tabSpec1);

		// View tabView2 = layoutInflater.inflate(R.layout.main_tab, null);
		// tabIcon2 = (ImageView) tabView2.findViewById(R.id.tabIcon);
		// tabIcon2.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.icon_nav_rc_click));
		// tabName2 = (TextView) tabView2.findViewById(R.id.tabName);
		// tabName2.setText(R.string.rc);
		// TabSpec tabSpec2 = tabHost.newTabSpec("remote");
		// Intent tabIntent2 = new Intent();
		// tabIntent2.setClass(context, RCActivity.class);
		// tabSpec2.setContent(tabIntent2);
		// tabSpec2.setIndicator(tabView2);
		// tabHost.addTab(tabSpec2);

		View tabView3 = layoutInflater.inflate(R.layout.main_tab, null);
		tabIcon2 = (ImageView) tabView3.findViewById(R.id.tabIcon);
		tabIcon2.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.icon_nav_push));
		tabName2 = (TextView) tabView3.findViewById(R.id.tabName);
		tabName2.setText(R.string.leshare);
		TabSpec tabSpec3 = tabHost.newTabSpec("share");
		Intent tabIntent3 = new Intent();
		tabIntent3.setClass(context, LeSahre.class);
		tabSpec3.setContent(tabIntent3);
		tabSpec3.setIndicator(tabView3);
		tabHost.addTab(tabSpec3);

		tabView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShareUtlis.share2Fre(context);
			}
		});

		View tabView4 = layoutInflater.inflate(R.layout.main_tab, null);
		tabIcon4 = (ImageView) tabView4.findViewById(R.id.tabIcon);
		tabIcon4.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.yaoyiyaoo));
		tabName4 = (TextView) tabView4.findViewById(R.id.tabName);
		tabName4.setText(R.string.tab_setting_shake);
		TabSpec tabSpec4 = tabHost.newTabSpec("yao");
		Intent tabIntent4 = new Intent();
		tabIntent4.setClass(context, YaoYiYao_yy.class);
		tabSpec4.setContent(tabIntent4);
		tabSpec4.setIndicator(tabView4);
		tabHost.addTab(tabSpec4);

		// View tabView4 = layoutInflater.inflate(R.layout.main_tab, null);
		// tabIcon4 = (ImageView) tabView4.findViewById(R.id.tabIcon);
		// tabIcon4.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.icon_nav_user));
		// tabName4 = (TextView) tabView4.findViewById(R.id.tabName);
		// tabName4.setText(R.string.user);
		// TabSpec tabSpec4 = tabHost.newTabSpec("user");
		// Intent tabIntent4 = new Intent();
		// tabIntent4.setClass(context, LeVipActivity.class);
		// tabSpec4.setContent(tabIntent4);
		// tabSpec4.setIndicator(tabView4);
		// tabHost.addTab(tabSpec4);

		View tabView5 = layoutInflater.inflate(R.layout.main_tab, null);
		tabIcon5 = (ImageView) tabView5.findViewById(R.id.tabIcon);
		tabIcon5.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.icon_nav_more));
		tabName5 = (TextView) tabView5.findViewById(R.id.tabName);
		tabName5.setText("主页");
		TabSpec tabSpec5 = tabHost.newTabSpec("more");
		Intent tabIntent5 = new Intent();
		tabIntent5.setClass(context, LeActivity.class);
		tabSpec5.setContent(tabIntent5);
		tabSpec5.setIndicator(tabView5);
		tabHost.addTab(tabSpec5);

		tabView5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// if ("QPF".equals(NetUtils.getWifiName(context))
				// || "QuanpinJG".equals(NetUtils.getWifiName(context))
				// || "Wireless".equals(NetUtils.getWifiName(context))
				// || "CHINA-DTV-N16".equals(NetUtils.getWifiName(context))) {
				// tabHost.setCurrentTab(3);
				// mHandler.sendEmptyMessageDelayed(0, 1000);
				// } else {
				tabHost.setCurrentTab(3);
				// }
			}
		});

		tabHost.setCurrentTab(3);
		updateTab(tabHost);

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				updateTab(tabHost);
			}
		});

	}

	/**
	 * 更新导航栏
	 * 
	 * @param tabHost
	 */
	private void updateTab(final TabHost tabHost) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View v = tabHost.getTabWidget().getChildAt(i);
			ImageView tabIcon = (ImageView) v.findViewById(R.id.tabIcon);
			TextView tabName = (TextView) v.findViewById(R.id.tabName);

			if (tabHost.getCurrentTab() == i) {
				tabName.setTextColor(getResources().getColor(
						R.color.nav_current));
				switch (i) {
				case 0:
					// tabIcon.setBackgroundDrawable(getResources().getDrawable(
					// R.drawable.icon_nav_le_click));
					// tabIcon.setBackgroundDrawable(getResources().getDrawable(
					// R.drawable.icon_nav_rc_click));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.icon_nav_user_click));
					break;
				case 1:
					// tabIcon.setBackgroundDrawable(getResources().getDrawable(
					// R.drawable.icon_nav_rc_click));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.icon_nav_push_click));
					break;
				case 2:
					// tabIcon.setBackgroundDrawable(getResources().getDrawable(
					// R.drawable.icon_nav_push_click));

					tabIcon.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.yaoyiyaoo_press));
					break;
				case 3:
					// tabIcon.setBackgroundDrawable(getResources().getDrawable(
					// R.drawable.icon_nav_user_click));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.icon_nav_more_click));
					if ("QPF".equals(NetUtils.getWifiName(context))
							|| "QuanpinJG"
									.equals(NetUtils.getWifiName(context))
							|| "Wireless".equals(NetUtils.getWifiName(context))) {
						mHandler.sendEmptyMessageDelayed(0, 1000);
					}

					break;
				case 4:
					tabIcon.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.icon_nav_more_click));

					break;
				default:
					break;
				}

			} else {
				tabName.setTextColor(getResources().getColor(R.color.black9));
				switch (i) {
				case 0:
					// tabIcon.setBackgroundDrawable(getResources().getDrawable(
					// R.drawable.icon_nav_le));
					// tabIcon.setBackgroundDrawable(getResources().getDrawable(
					// R.drawable.icon_nav_rc));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.icon_nav_user));
					break;
				case 1:
					// tabIcon.setBackgroundDrawable(getResources().getDrawable(
					// R.drawable.icon_nav_rc));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.icon_nav_push));
					break;
				case 2:
					// tabIcon.setBackgroundDrawable(getResources().getDrawable(
					// R.drawable.icon_nav_push));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.yaoyiyaoo));
					break;
				case 3:
					// tabIcon.setBackgroundDrawable(getResources().getDrawable(
					// R.drawable.icon_nav_user));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.icon_nav_more));
					break;
				case 4:
					tabIcon.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.icon_nav_more));
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.getCurrentActivity().onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		serverManager.close();

		Intent intent = new Intent(this, DMCMediaService.class);
		stopService(intent);
		super.onDestroy();
	}

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Intent intent2 = new Intent();
			intent2.setClass(MainActivity.this, MainActivityt.class);
			startActivity(intent2);
			return false;
		}
	});

	private long exitTime = 0;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_UP
				&& event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			Log.i("点击", "按钮:" + event.getKeyCode());
			showWindow();
			return true;
		}

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.finsh_toast),
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				// System.exit(0);
			}
			return true;
		}

		return super.dispatchKeyEvent(event);
	}

	private void showWindow() {

		switch (tabHost.getCurrentTab()) {
		case 0:
			popupMenus[0].show();
			break;
		case 1:
			popupMenus[1].show();
			break;

		case 3:
			popupMenus[3].show();
			break;

		default:
			break;
		}
	}

	// private void getpopMenu(){
	// PopupMenu popupMenu=new PopupMenu(activity, menu[0]);
	// popupMenu.inflate(R.menu.more_menu);
	// popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
	//
	// @Override
	// public boolean onMenuItemClick(MenuItem item) {
	// // TODO Auto-generated method stub
	// switch (item.getItemId()) {
	// case R.id.le_more:
	//
	// break;
	//
	// case R.id.isao:
	//
	// break;
	//
	// default:
	// break;
	// }
	// return false;
	// }
	// });
	// }

	private int mx, my;
	private OnTouchListener fudong = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			int curx = 0, cury = 0;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mx = (int) event.getX();
				my = (int) event.getY();
				break;

			case MotionEvent.ACTION_MOVE:

				curx = (int) event.getRawX() - mx;
				cury = (int) event.getRawY() - my - 80;
				fudongButton.layout(curx, cury, curx + fudongButton.getWidth(),
						cury + fudongButton.getHeight());

				break;

			case MotionEvent.ACTION_UP:
				// curx=(int) event.getX();
				// cury=(int) event.getY();
				// mImageView.scrollBy((int)(mx-curx),(int)(my-cury));
				// mx=curx;
				// my=cury;
				break;

			default:
				break;
			}

			Log.i("ming", "x:" + curx + "y:" + cury);

			return true;

		}
	};

	
	private void initWifiList(){
		wifiList.add("QPF");
		wifiList.add("QuanpinJG");
		wifiList.add("Wireless");
		wifiList.add("CHINA-DTV-N16");
	}
	
	
	private boolean hasBusiness(String ssid){
		for (int i = 0; i < wifiList.size(); i++) {
			if (ssid.equals(wifiList.get(i))) {
				return true;
			}
		}
		
		return false;
		
	}
}

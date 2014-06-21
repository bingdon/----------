package tv.luxs.rcassistant;

import tv.luxs.rcassistant.service.SearchIpService;
import tv.luxs.rcassistant.utils.NetUtils;
import android.app.Activity;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
/**
 * 新界面
 * @author lyl
 *
 */
@SuppressWarnings("deprecation")
public class MainActivityt extends TabActivity {
	

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
	private TabHost tabHost ;
	public static boolean maint=false;
	public static PopupMenu[] popupMenus = new PopupMenu[4];
	private SearchIpService searchIpService;
	private int main=5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("MainActivityt", "新的");
		main=getIntent().getIntExtra("main", 5);
		maint=true;
		context = this;
		activity = this;
		initTabHost();
		
//		startService(new Intent(context, SearchIpService.class));
		SearchIpService.setFLAG(false);
		bindService(new Intent(context, SearchIpService.class), serviceConnection, Context.BIND_AUTO_CREATE);
		if (NetUtils.isConnect(context)) {
			Log.i("Mainactivity", "名称:"+NetUtils.getWifiName(context));
			
		}
		
		
		
	}
	
	
	
	
	
	/**
	 * 初始化导航栏
	 */
	private void initTabHost(){	
  		 tabHost = getTabHost();
  		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
  		
//  		View tabView1 = layoutInflater.inflate(R.layout.main_tab, null);
//  		tabIcon1 = (ImageView)tabView1.findViewById(R.id.tabIcon);
//  		tabIcon1.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_le));
//  		tabName1 = (TextView)tabView1.findViewById(R.id.tabName);
//  		tabName1.setText(R.string.le_shangjia);
//  		TabSpec tabSpec1 = tabHost.newTabSpec("le"); 
//  		Intent tabIntent1 = new Intent();
//  		tabIntent1.setClass(context, WebActivity.class);
//  		tabSpec1.setContent(tabIntent1);
//  		tabSpec1.setIndicator(tabView1);
//  		tabHost.addTab(tabSpec1);
  		
  		View tabView2 = layoutInflater.inflate(R.layout.main_tab, null);
  		tabIcon2 = (ImageView)tabView2.findViewById(R.id.tabIcon);
  		tabIcon2.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_rc_click));
  		tabName2 = (TextView)tabView2.findViewById(R.id.tabName);
  		tabName2.setText(R.string.rc);
  		TabSpec tabSpec2 = tabHost.newTabSpec("remote"); 
  		Intent tabIntent2 = new Intent();
  		tabIntent2.setClass(context, RCActivity.class);
  		tabSpec2.setContent(tabIntent2);
  		tabSpec2.setIndicator(tabView2);
  		tabHost.addTab(tabSpec2);
  		
  		View tabView3 = layoutInflater.inflate(R.layout.main_tab, null);
  		tabIcon2 = (ImageView)tabView3.findViewById(R.id.tabIcon);
  		tabIcon2.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_push));
  		tabName2 = (TextView)tabView3.findViewById(R.id.tabName);
  		tabName2.setText(R.string.push);
  		TabSpec tabSpec3 = tabHost.newTabSpec("push"); 
  		Intent tabIntent3 = new Intent();
  		tabIntent3.setClass(context, PushActivity.class);
  		tabSpec3.setContent(tabIntent3);
  		tabSpec3.setIndicator(tabView3);
  		tabHost.addTab(tabSpec3);
  		
  		View tabView4 = layoutInflater.inflate(R.layout.main_tab, null);
  		tabIcon4 = (ImageView)tabView4.findViewById(R.id.tabIcon);
  		tabIcon4.setBackgroundDrawable(getResources().getDrawable(R.drawable.yaoyiyaoo));
  		tabName4 = (TextView)tabView4.findViewById(R.id.tabName);
  		tabName4.setText(R.string.tab_setting_shake);
  		TabSpec tabSpec4 = tabHost.newTabSpec("yao"); 
  		Intent tabIntent4 = new Intent();
  		tabIntent4.setClass(context, YaoYiYao_yy.class);
  		tabSpec4.setContent(tabIntent4);
  		tabSpec4.setIndicator(tabView4);
  		tabHost.addTab(tabSpec4);
  		
//  		View tabView1 = layoutInflater.inflate(R.layout.main_tab, null);
//		tabIcon1 = (ImageView)tabView1.findViewById(R.id.tabIcon);
//		TabSpec tabSpec1 = tabHost.newTabSpec("le"); 
//		Intent tabIntent1 = new Intent();
//		tabIntent1.setClass(context, WebActivity.class);
//		tabSpec1.setContent(tabIntent1);
//		tabSpec1.setIndicator(tabView1);
//		tabHost.addTab(tabSpec1);
//  		tabView1.setVisibility(View.GONE);
  		
//  		View tabView5 = layoutInflater.inflate(R.layout.main_tab, null);
//  		tabIcon5 = (ImageView)tabView5.findViewById(R.id.tabIcon);
//  		tabIcon5.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_more));
//  		tabName5 = (TextView)tabView5.findViewById(R.id.tabName);
//  		tabName5.setText(R.string.more);
//  		TabSpec tabSpec5 = tabHost.newTabSpec("more"); 
//  		Intent tabIntent5 = new Intent();
//  		tabIntent5.setClass(context, MoreActivity.class);
//  		tabSpec5.setContent(tabIntent5);
//  		tabSpec5.setIndicator(tabView5);
//  		tabHost.addTab(tabSpec5);
  		
  		
  		
  		View tabView1 = layoutInflater.inflate(R.layout.main_tab, null);
  		tabIcon1 = (ImageView)tabView1.findViewById(R.id.tabIcon);
  		tabIcon1.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_le));
  		tabName1 = (TextView)tabView1.findViewById(R.id.tabName);
  		tabName1.setText("主页");
  		TabSpec tabSpec1 = tabHost.newTabSpec("le"); 
  		Intent tabIntent1 = new Intent();
  		tabIntent1.setClass(context, WebActivity.class);
  		tabSpec1.setContent(tabIntent1);
  		tabSpec1.setIndicator(tabView1);
  		tabHost.addTab(tabSpec1);
  		
  		if (main==0) {
			tabHost.setCurrentTab(0);
		}else if (main==1) {
			tabHost.setCurrentTab(1);
		}else {
			tabHost.setCurrentTab(3);
		}
  		
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
	 * @param tabHost
	 */
  	private void updateTab(final TabHost tabHost) {
      for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
          	View v = tabHost.getTabWidget().getChildAt(i);
          	ImageView tabIcon = (ImageView)v.findViewById(R.id.tabIcon);
          	TextView tabName = (TextView)v.findViewById(R.id.tabName);

          	if (tabHost.getCurrentTab() == i) {
          		tabName.setTextColor(getResources().getColor(R.color.nav_current));
          		switch (i) {
				case 0:
//					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_le_click));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_rc_click));
					break;
				case 1:
//					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_rc_click));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_push_click));
					break;
				case 2:
//					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_push_click));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.yaoyiyaoo_press));
					break;
				case 3:
					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_more_click));
					break;
				case 4:
					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_more_click));
					break;
				default:
					break;
				}
          		
          	} else {
          		tabName.setTextColor(getResources().getColor(R.color.black9));
          		switch (i) {
				case 0:
//					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_le));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_rc));
					break;
				case 1:
//					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_rc));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_push));
					break;
				case 2:
//					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_push));
					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.yaoyiyaoo));
					break;
				case 3:
					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_more));
					break;
				case 4:
					tabIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_nav_more));
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
  		super.onDestroy();
  		maint=false;
  		SearchIpService.setFLAG(true);
  		unbindService(serviceConnection);
  	}

  	
  	@Override
  	public boolean onCreateOptionsMenu(Menu menu) {
  		// TODO Auto-generated method stub
  		getMenuInflater().inflate(R.menu.tab_setting, menu);
  		return true;
  	}
  	
  	
  	@Override
  	public boolean onOptionsItemSelected(MenuItem item) {
  		// TODO Auto-generated method stub
  		switch (item.getItemId()) {
		case R.id.business:
			tabHost.setCurrentTab(0);
			break;

		default:
			break;
		}
  		return super.onOptionsItemSelected(item);
  	}

	
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_UP
				&& event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			Log.i("点击", "按钮:" + event.getKeyCode());
			showWindow();
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
	
	
	private ServiceConnection serviceConnection=new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			SearchIpService.setFLAG(true);
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			searchIpService=((SearchIpService.MyBinder)service).getMyBinder();
			
		}
	};
  	

}

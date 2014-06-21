package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.List;

import net.zhaidian.file.FileBean;
import net.zhaidian.file.FileManager;
import net.zhaidian.more.FinanceLister;
import net.zhaidian.more.GameLister;
import net.zhaidian.more.MoreSettingLister;
import net.zhaidian.more.ScanLister;
import net.zhaidian.more.ShakeLister;
import tv.luxs.config.G;
import tv.luxs.rcassistant.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MoreActivity  extends Activity implements OnGestureListener{
	Context context;
	Activity activity;
	
	//滑动变量
	GestureDetector detector;
	ViewFlipper flipper;

	//列表变量
	View settingView;
	View scanView;
	View shakeView;
	View gameView;
//	View financeView;
	ListView settingListView;
	ListView scanListView;
	ListView shakeListView;
	ListView gameListView;
//	ListView financeListView;
	List<FileBean> settingData = new ArrayList<FileBean>();
	List<FileBean> scanData = new ArrayList<FileBean>();
	List<FileBean> shakeData = new ArrayList<FileBean>();
	List<FileBean> gameData = new ArrayList<FileBean>();
//	List<FileBean> financeData = new ArrayList<FileBean>();
	FileAdapter settingAdapter;
	FileAdapter scanAdapter;
	FileAdapter shakeAdapter;
	FileAdapter gameAdapter;
//	FileAdapter financeAdapter;
	View loadingView;
	int currentView = G.SETTING;
	ImageView tabCurrent;
	Animation currentFlagAnim;
	float currentX = 0;
	float settingX = 0;
	float scanX = 0;
	float shakeX = 0;
	float gameX = 0;
//	float financeX = 0;
	
	FileManager fileManager = new FileManager();
	FileBean fileBean;
	boolean hassettingData = true;
	boolean hasscanData = true;
	boolean hasshakeData = true;
	boolean hasgameData = true;
	boolean hasfinanceData = true;
	
	boolean isLoading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		initUI();
	}
	
	private void initUI(){
    	context = this;
		activity = this;
		detector = new GestureDetector(this);
    	
    	flipper = (ViewFlipper)findViewById(R.id.flipper);
    	
    	Display mDisplay = getWindowManager().getDefaultDisplay();
    	int width = mDisplay.getWidth();
    	
    	tabCurrent = (ImageView)findViewById(R.id.tabCurrent);
    	LayoutParams params=(LayoutParams)tabCurrent.getLayoutParams();
    	float distance = width / 4;
    	params.width = (int)(distance);
    	tabCurrent.setLayoutParams(params);
    	
    	scanX = distance * 1;
    	shakeX = distance * 2;
    	gameX = distance * 3;
//    	financeX = distance * 4;
    	
    	//初始化滑动页面
    	LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    	settingView = layoutInflater.inflate(R.layout.file_list, null);
    	scanView = layoutInflater.inflate(R.layout.file_list, null);
    	shakeView = layoutInflater.inflate(R.layout.file_list, null);
    	gameView = layoutInflater.inflate(R.layout.file_list, null);
//    	financeView = layoutInflater.inflate(R.layout.file_list, null);
    	loadingView = layoutInflater.inflate(R.layout.list_loading, null);
    	
    	settingView.setId(G.SETTING);
    	scanView.setId(G.SCAN);
    	shakeView.setId(G.SHAKE);
    	gameView.setId(G.GAME);
//    	financeView.setId(G.FINANCE);
    	
    	//将滑动页面加入滑动框里
    	flipper.addView(settingView);
    	flipper.addView(scanView);
    	flipper.addView(shakeView);
    	flipper.addView(gameView);
//    	flipper.addView(financeView);
    	flipper.setDisplayedChild(G.SETTING);
    	currentView=G.SETTING;
    	
    	//绑定列表
    	settingListView = (ListView)settingView.findViewById(R.id.listView);
    	scanListView = (ListView)scanView.findViewById(R.id.listView);
    	shakeListView = (ListView)shakeView.findViewById(R.id.listView);
    	gameListView = (ListView)gameView.findViewById(R.id.listView);
//    	financeListView = (ListView)financeView.findViewById(R.id.listView);

    	//加载数据
    	fileBean = new FileBean();
    	fileBean.setContext(context);
    	loadData();
    	//绑定适配器
    	settingAdapter = new FileAdapter(settingData);
    	settingListView.setAdapter(settingAdapter);
    	settingListView.setOnItemClickListener(new FileListOnItemClickListener(settingData));
    	
    	scanListView.addFooterView(loadingView);
    	scanAdapter = new FileAdapter(scanData);
    	scanListView.setAdapter(scanAdapter);
    	scanListView.setOnItemClickListener(new FileListOnItemClickListener(scanData));
    	
    	shakeListView.addFooterView(loadingView);
    	shakeAdapter = new FileAdapter(shakeData);
    	shakeListView.setAdapter(shakeAdapter);
    	shakeListView.setOnItemClickListener(new FileListOnItemClickListener(shakeData));
    	
    	gameListView.addFooterView(loadingView);
    	gameAdapter = new FileAdapter(gameData);
    	gameListView.setAdapter(gameAdapter);
    	gameListView.setOnItemClickListener(new FileListOnItemClickListener(gameData));
    	
//    	financeListView.addFooterView(loadingView);
//    	financeAdapter = new FileAdapter(financeData);
//    	financeListView.setAdapter(financeAdapter);
//    	financeListView.setOnItemClickListener(new FileListOnItemClickListener(financeData));
    }
	
	private void anim(View view,float x1,float x2,float y1,float y2){
		Animation translateAnimation = new TranslateAnimation(x1, x2, y1, y2);  
		translateAnimation.setDuration(500);  
		translateAnimation.setFillAfter(true);
		view.startAnimation(translateAnimation);  
		currentX = x2; 
	}
	
	//加载数据
    private void loadData(){
    	if(!isLoading){
	    	switch (currentView) {
			case G.SETTING:
				fileBean.setFileType(G.SETTING);
				fileManager.setIlister(new MoreSettingLister());
				settingListView.addFooterView(loadingView);
				break;
			case G.SCAN:
				fileBean.setFileType(G.SCAN);
				fileManager.setIlister(new ScanLister());
				scanListView.addFooterView(loadingView);
				break;
			case G.SHAKE:
				fileBean.setFileType(G.SHAKE);
				fileManager.setIlister(new ShakeLister());
				shakeListView.addFooterView(loadingView);
				break;
			case G.GAME:
				fileBean.setFileType(G.GAME);
				fileManager.setIlister(new GameLister());
				gameListView.addFooterView(loadingView);
				break;
			case G.FINANCE:
				fileBean.setFileType(G.FINANCE);
				fileManager.setIlister(new FinanceLister());
//				financeListView.addFooterView(loadingView);
				break;
			default:
				break;
			}
    		new Thread(new loadDataThread()).start();
    		Utils.log("load");
    	}
    }
    
    //加载数据线程
  	private class loadDataThread extends Thread{
  		@SuppressWarnings("unchecked")
		@Override
  		public void run() {
  			try{
  				isLoading = true;
  				List<FileBean> tempLists = (List<FileBean>) fileManager.getList(fileBean);
  				Utils.log("tempLists" + tempLists);
  				if(tempLists != null){
	  				Message msg = loadDataHandler.obtainMessage();
	  				msg.arg1 = fileBean.getFileType();
	  				msg.what = G.LOAD_SUCCESS;
	  				msg.obj = tempLists;
	  				msg.sendToTarget();
  				}else{
  					Message msg = loadDataHandler.obtainMessage();
  					msg.arg1 = fileBean.getFileType();
	  				msg.what = G.LOAD_FAILED;
	  				msg.sendToTarget();
  				}  				
  			}catch (Exception e) {
  				Message msg = loadDataHandler.obtainMessage();
  				msg.what = G.LOAD_FAILED;
  				msg.sendToTarget();
				e.printStackTrace();
			}
  		}
  	}
    
    /**
     * 更新列表UI
     */
    private Handler loadDataHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		List<FileBean> tempLists = (List<FileBean>) msg.obj;
    		
    		switch (msg.what) {
			case G.LOAD_SUCCESS:
				switch (msg.arg1) {
				case G.SETTING:
					settingData.addAll(tempLists);
					Utils.log("handler common");
					settingListView.removeFooterView(loadingView);
					settingAdapter.notifyDataSetChanged();
					break;
				case G.SCAN:
					scanData.addAll(tempLists);
					scanListView.removeFooterView(loadingView);
					scanAdapter.notifyDataSetChanged();
					break;
				case G.SHAKE:
					shakeData.addAll(tempLists);
					shakeListView.removeFooterView(loadingView);
					shakeAdapter.notifyDataSetChanged();
					break;
				case G.GAME:
					gameData.addAll(tempLists);
					gameListView.removeFooterView(loadingView);
					gameAdapter.notifyDataSetChanged();
					break;
				case G.FINANCE:
//					financeData.addAll(tempLists);
//					financeListView.removeFooterView(loadingView);
//					financeAdapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
				
				break;
			case G.LOAD_FAILED:
				//Utils.toast(activity, "暂无数据！");
				switch (msg.arg1) {
				case G.SETTING:
					hassettingData = false;
					settingListView.removeFooterView(loadingView);
					break;
				case G.SCAN:
					hasscanData = false;
					scanListView.removeFooterView(loadingView);
					break;
				case G.SHAKE:
					hasshakeData = false;
					shakeListView.removeFooterView(loadingView);
					break;
				case G.GAME:
					hasgameData = false;
					gameListView.removeFooterView(loadingView);
					break;
				case G.FINANCE:
					hasfinanceData = false;
//					financeListView.removeFooterView(loadingView);
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
    		isLoading = false;
    	};
    };

  	
    
  	
	
	//适配器
    private class FileAdapter extends BaseAdapter{
    	List<FileBean> dataList;
    	public FileAdapter(List<FileBean> dataList) {
			this.dataList = dataList;
		}
		@Override
		public int getCount() {
			if(dataList != null){
				return dataList.size();
			}else{
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		LayoutInflater inflater;
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.file_list_item, null);
				viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
				viewHolder.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}	
			switch (dataList.get(position).getFileType()) {
			case G.SETTING_RC:
				viewHolder.image.setBackgroundResource(R.drawable.icon_rc_setting);
				break;
			case G.SETTING_PUSH:
				viewHolder.image.setBackgroundResource(R.drawable.icon_push_setting);
				break;
			case G.SETTING_LOGOUT:
				viewHolder.image.setBackgroundResource(R.drawable.icon_logout);
				break;
				
			case G.SETTING_ABOUT:
				viewHolder.image.setBackgroundResource(R.drawable.gantan);
				break;	
				
				
			default:
				break;
			}
			viewHolder.name.setText(dataList.get(position).getFileName());

			return convertView;
		} 
    	
    }
    
    class ViewHolder{
    	ImageView image;
    	TextView name;
    }
    
  	
  	//点击监听器
  	class FileListOnItemClickListener implements OnItemClickListener{
  		List<FileBean> dataList;
  		public FileListOnItemClickListener(List<FileBean> dataList) {
  			this.dataList = dataList;
		}
  		
  		@Override
  		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
  				long arg3) {
  			if(dataList.size() > 0 && position >= 0 && position < dataList.size()){
				FileBean tempBean = dataList.get(position);
				switch (tempBean.getFileType()) {	
				case G.SETTING_RC:
					Utils.toActivity(activity, SettingActivity.class);
					break;
				case G.SETTING_PUSH:
					Utils.toActivity(activity, SettingPushActivity.class);
					break;
				case G.SETTING_ABOUT:
					Utils.toActivity(activity, AboutActivity.class);
					break;
				case G.SETTING_LOGOUT:
					finish();
					System.exit(0);
					break;

				default:
					break;
				}
  			}
  		}
  		
  	}

  	@Override
    public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
    	return this.detector.onTouchEvent(event);
    }
  	
  	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	detector.onTouchEvent(ev);
    	super.dispatchTouchEvent(ev); 
        return true;
    }
  	
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() > (e2.getX() + 200)) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
//            flipper.showNext();
            View view = this.flipper.getCurrentView();
            Log.i("乐更多", "当前pager:"+view.getId());
            switch (view.getId()) {
            
			case G.SETTING:
				anim(tabCurrent, currentX, scanX, 0f, 0f);
				currentView = G.SCAN;
				if(scanData.size() < 1) loadData();
				
				break;
			case G.SCAN:
				anim(tabCurrent, currentX, shakeX, 0f, 0f);
				currentView = G.SHAKE;
				if(shakeData.size() < 1) loadData();
				
				break;
			case G.SHAKE:
				anim(tabCurrent, currentX, gameX, 0f, 0f);
				currentView = G.GAME;
				if(gameData.size() < 1) loadData();
				
				break;
			case G.GAME:
				anim(tabCurrent, currentX, settingX, 0f, 0f);
				currentView = G.FINANCE;
				if(settingData.size() < 1) loadData();
				
				break;
			case G.FINANCE:
				anim(tabCurrent, currentX, settingX, 0f, 0f);
				currentView = G.SETTING;
				if(settingData.size() < 1) loadData();
				
				break;
			default:
				break;
			}
            this.flipper.showNext();
            return true;
        } else if (e1.getX() < (e2.getX() - 200)) {
        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
            this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));
            View view = this.flipper.getCurrentView();
            switch (view.getId()) {
			case G.SETTING:
				anim(tabCurrent, currentX, gameX, 0f, 0f);
				currentView = G.GAME;
				if(settingData.size() < 1) loadData();
				break;
			case G.SCAN:
				anim(tabCurrent, currentX, settingX, 0f, 0f);
				currentView = G.SETTING;
				if(settingData.size() < 1) loadData();
				break;
			case G.SHAKE:
				anim(tabCurrent, currentX, scanX, 0f, 0f);
				currentView = G.SCAN;
				if(scanData.size() < 1) loadData();
				break;
			case G.GAME:
				anim(tabCurrent, currentX, shakeX, 0f, 0f);
				currentView = G.SHAKE;
				if(shakeData.size() < 1) loadData();
				break;
			case G.FINANCE:
				anim(tabCurrent, currentX, gameX, 0f, 0f);
				currentView = G.GAME;
				if(gameData.size() < 1) loadData();
				break;
			default:
				break;
			}
            this.flipper.showPrevious();
            return true;
        }
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.tabSetting:
			anim(tabCurrent, currentX, settingX, 0f, 0f);
			flipper.setDisplayedChild(G.SETTING);
			currentView = G.SETTING;
			if(settingData.size() < 1) loadData();
			break;
		case R.id.tabScan:
			anim(tabCurrent, currentX, scanX, 0f, 0f);
			flipper.setDisplayedChild(G.SCAN);
			currentView = G.SCAN;
			if(scanData.size() < 1) loadData();
			break;
		case R.id.tabShake:
			anim(tabCurrent, currentX, shakeX, 0f, 0f);
			flipper.setDisplayedChild(G.SHAKE);
			currentView = G.SHAKE;
			if(shakeData.size() < 1) loadData();
			break;
		case R.id.tabGame:
			anim(tabCurrent, currentX, gameX, 0f, 0f);
			flipper.setDisplayedChild(G.GAME);
			currentView = G.GAME;
			if(gameData.size() < 1) loadData();
			break;
//		case R.id.tabFinance:
//			anim(tabCurrent, currentX, financeX, 0f, 0f);
//			flipper.setDisplayedChild(G.FINANCE);
//			currentView = G.FINANCE;
//			if(financeData.size() < 1) loadData();
//			break;
		case R.id.btnQrcode:
			Intent intent = new Intent();
			intent.putExtra("type", 1);
			intent.setClass(context, SettingConnectActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}

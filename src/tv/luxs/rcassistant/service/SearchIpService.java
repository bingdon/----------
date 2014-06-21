package tv.luxs.rcassistant.service;

import java.util.ArrayList;
import java.util.List;

import net.zhaidian.file.FileBean;
import net.zhaidian.file.FileManager;
import tv.luxs.config.G;
import tv.luxs.rcassistant.connect.ConnectManager;
import tv.luxs.rcassistant.gpsowifibean.GpsoWifiBean;
import tv.luxs.rcassistant.gpsowifibean.GpsoWifiSqliter;
import tv.luxs.rcassistant.utils.IpUtils;
import tv.luxs.rcassistant.utils.NetUtils;
import tv.luxs.rcassistant.R;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class SearchIpService extends Service {

	private static boolean FLAG=false;
	private static final String TAG="SearchIpService";
	private LocationManager locationManager;
	//经度
	private long lon=0;
	//纬度
	private long lat=0;
	
	private final Binder mBinder=new MyBinder();
	
	private List<GpsoWifiBean> gpsowifiList=new ArrayList<GpsoWifiBean>();
	
	private GpsoWifiBean sBean=new GpsoWifiBean();
	
	public class MyBinder extends Binder{
		public SearchIpService getMyBinder(){
			return SearchIpService.this;
		}
	}
	public static boolean isFLAG() {
		return FLAG;
	}

	public static void setFLAG(boolean fLAG) {
		FLAG = fLAG;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		Log.i(TAG, "===onCreate===");
		if (null==IpUtils.getUpnpController()) {
			IpUtils.initUpCoTro();
		}
		new Thread(mRunnable).start();
		initGps();
//		getgpsowifi();
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "===onDestroy===");
		if (lat>0&&lon>0) {
//			saveIpapostion();
		}
	}
	private Runnable mRunnable=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!FLAG) {
				
				if (FLAG) {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				if (!ConnectManager.isConnected()) {
					if (null==IpUtils.getUpnpController()) {
						IpUtils.initUpCoTro();
						sendSecConBroadcast();
					}else {
						sendSecConBroadcast();
					}
				}
				
				try {
					
					Thread.sleep(1000*60*5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.i(TAG, "===服务运行中===");
				
			
				
			}
			
			stopSelf();
			
		}
	};
	
	
	
	/**
	 * 连接提示广播
	 */
	private void sendSecConBroadcast(){
		Intent intent=new Intent();
		intent.setAction(getResources().getString(R.string.connectaction));
		sendBroadcast(intent);
	}
	
	private void sendOpenGpsBroadcast(){
		Intent intent=new Intent();
		intent.setAction(G.ACTION_OPEN_GPS);
		sendBroadcast(intent);
	}
	
	/**
	 * 初始化GPS
	 */
	private void initGps(){
		locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			sendOpenGpsBroadcast();
		}
		  Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
	        criteria.setAltitudeRequired(true);
	        criteria.setBearingRequired(false);
	        criteria.setCostAllowed(true);
	        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
	        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);
	}
	
	
	private LocationListener listener=new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.i(TAG, "经度:"+location.getLongitude()
					+"\n"+"纬度:"+location.getLatitude()
					+"\n"+"海拔:"+location.getAltitude());
		}
	};

	private void saveIpapostion(){
		String wifiString=NetUtils.getWifiName(this);
		if (TextUtils.isEmpty(wifiString)) {
			return;
		}
		if (null!=gpsowifiList) {
			for (int i = 0; i < gpsowifiList.size(); i++) {
				if (gpsowifiList.get(i).getWifi_ssid().equals(wifiString)) {
					return;
				}
			}
		}
		GpsoWifiBean gpsBean=new GpsoWifiBean();
		gpsBean.setWifi_ssid(NetUtils.getWifiName(this));
		gpsBean.setLat(""+lat);
		gpsBean.setLon(""+lon);
		gpsBean.setContext(this);
		updateOrInsertData(gpsBean);
		Log.i(TAG, "===保存===");
	}
	
	
	/**
	 * 更新数据库数据
	 */
	public void updateOrInsertData(FileBean fileBean) {
		FileManager fileManager = new FileManager();
		fileManager.setSqliter(new GpsoWifiSqliter());
//		int count = fileManager.getLocalCount(fileBean);
//		if (count == 0) {
//			Utils.log("insert:" + fileManager.insert(fileBean));
//		} else {
//			Utils.log("update:" + fileManager.update(fileBean));
//		}
		fileManager.insert(fileBean);
	}
	
	/**
	 * 获取数据库数据
	 */
	public List<?> queryData(FileBean fileBean) {

		FileManager fileManager = new FileManager();
		fileManager.setSqliter(new GpsoWifiSqliter());
		return fileManager.query(fileBean);
	}
	
	
	/**
	 * 更新数据库数据
	 */
	public int updateData(FileBean fileBean) {
		FileManager fileManager = new FileManager();
		fileManager.setSqliter(new GpsoWifiSqliter());
		return fileManager.update(fileBean);
	}

	/**
	 * 删除数据库数据
	 */
	public boolean deleteData(FileBean fileBean) {
		FileManager fileManager = new FileManager();
		fileManager.setSqliter(new GpsoWifiSqliter());
		int result = fileManager.delete(fileBean);
		if (result != 0)
			return true;
		else
			return false;
	}
	
	
	@SuppressWarnings("unchecked")
	private void getgpsowifi(){
		sBean.setContext(this);
		sBean.setCurrentPage(1);
		sBean.setPageSize(10);
//		sBean.setOrderBy(TableMateData.STATUS + " DESC , "
//				+ TableMateData._ID + " DESC");
		gpsowifiList=(List<GpsoWifiBean>)queryData(sBean);
		if (null!=gpsowifiList) {
			for (int i = 0; i < gpsowifiList.size(); i++) {
				Log.i(TAG, "经度:"+gpsowifiList.get(i).getLat());
				Log.i(TAG, "经度:"+gpsowifiList.get(i).getWifi_ssid());
			}
		}
		
	}
}

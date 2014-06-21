package tv.luxs.rcassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zhaidian.provider.IPsProviderMateData.TableMateData;
import tv.luxs.config.G;
import tv.luxs.rcassistant.connect.ConnectManager;
import tv.luxs.rcassistant.connect.ServerBean;
import tv.luxs.rcassistant.connect.ServerManager;
import tv.luxs.rcassistant.setting.SettingManager;
import tv.luxs.rcassistant.utils.CodeUtils;
import tv.luxs.rcassistant.utils.IpUtils;
import tv.luxs.rcassistant.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zxing.activity.CaptureActivity;

public class SettingConnectActivity extends Activity {

	private static final String TAG = "SettingConnectActivity";
	Context context;
	Activity activity;

	FrameLayout wrapper;
	View connectView;

	TextView info;
	ListView listView;
	private static List<ServerBean> dataList = new ArrayList<ServerBean>();
	final int CONNECT_ON = 1;
	final int CONNECT_OFF = 0;

	ServerAdapter serverAdapter = new ServerAdapter();
	ServerBean serverBean = new ServerBean();

	SettingManager settingManager = new SettingManager();
	ServerManager serverManager = new ServerManager();

	String host;
	boolean isConnecting = false;
	boolean isScanning = false;
	int status = 0;

	public static boolean is2codcon = false;
	public static boolean isconfirmcon = false;
	private int pagersize = 0;
	private int bingpostion = 0;
	private static SettingConnectActivity settingConnectActivity;
	private static List<Map<String, Object>> connectList=new ArrayList<Map<String,Object>>();
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_connect);
		settingConnectActivity = new SettingConnectActivity();
		initUI();
		initData();
		serverManager.setActivity(this);
		
		IpUtils.initUpCoTro();
		Log.i(TAG, "=====onCreate==随机码==="+G.CURRENT_VALUE);
		System.out.print("=====onCreate==随机码==="+G.CURRENT_VALUE);
		
	}

	public static SettingConnectActivity getSettingConnectActivity() {
		return settingConnectActivity;
	}

	/**
	 * 初始化UI
	 */
	public void initUI() {
		context = this;
		activity = this;

		status = getIntent().getIntExtra("type", 0);

		info = (TextView) findViewById(R.id.info);
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(serverAdapter);

		wrapper = (FrameLayout) findViewById(R.id.wrapper);
		LayoutInflater inflater = getLayoutInflater();
		connectView = inflater.inflate(R.layout.connect, null);
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		serverBean.setContext(context);
		serverBean.setCurrentPage(1);
		// serverBean.setPageSize(10);
		serverBean.setPageSize(settingManager.getServerCount(activity));
		serverBean.setOrderBy(TableMateData.STATUS + " DESC , "
				+ TableMateData._ID + " DESC");
	}

	@Override
	protected void onStart() {
		if (status == 1) {
			Intent openCameraIntent = new Intent(context, CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);
			status = 0;
		}
		super.onStart();
	}

	@Override
	protected void onResume() {
		// 获取默认机顶盒IP
		String host = serverManager.getHostIP(context);
		if (host != null) {
			// 是否已连接？
			boolean isConnected = ConnectManager.isConnected();
			if (isConnected == false||TextUtils.isEmpty(G.CURRENT_VALUE)){
				serverManager.setHostStatus(context, host, 0);
				upOnResumedate();
			}else {
				serverManager.setHostStatus(context, host, 1);
				upOnResumedate();
			}
				
				
			
		}
		super.onResume();
	}

	
	/**
	 * 更新数据
	 */
	public void upOnResumedate() {
		try {
			if (pagersize != 0) {
				serverBean.setPageSize(10);
			}
			// dataList.clear();
			try {
				dataList = (List<ServerBean>) serverManager.queryData(serverBean);
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		
			if (null!=dataList) {
				
//				if (pagersize<dataList.size()&&pagersize!=0) {
//					for (int j = 0; j < pagersize; j++) {
//						for (int i = pagersize; i < dataList.size(); i++) {
//							Log.i(TAG, "IP地址:"+"["+j+"]"+dataList.get(j).getIp());
//							Log.i(TAG, "IP地址:"+"["+i+"]"+dataList.get(i).getIp());
//							if (dataList.get(j).getIp().equals(dataList.get(i).getIp())) {
//								deletem(dataList.get(i).getId());
//								dataList.remove(i);
//							}
//						}
//					}
//					
//				}
			}
			
			
			

			if (null != dataList) {
				Log.i("连接", "列表:" + dataList.size());
			}

			if (dataList != null) {
				info.setVisibility(View.GONE);
				serverAdapter.notifyDataSetChanged();
			} else {
				info.setVisibility(View.VISIBLE);
				serverAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	/**
	 * 更新数据
	 */
	public void update() {
		try {
			if (pagersize != 0) {
				serverBean.setPageSize(10);
			}
			// dataList.clear();
			try {
				dataList = (List<ServerBean>) serverManager.queryData(serverBean);
			} catch (Exception e) {
				// TODO: handle exception
			}
		
			if (null!=dataList) {
				
				if (pagersize<dataList.size()&&pagersize!=0) {
					for (int j = 0; j < pagersize; j++) {
						for (int i = pagersize; i < dataList.size(); i++) {
							Log.i(TAG, "IP地址:"+"["+j+"]"+dataList.get(j).getIp());
							Log.i(TAG, "IP地址:"+"["+i+"]"+dataList.get(i).getIp());
							if (dataList.get(j).getIp().equals(dataList.get(i).getIp())) {
								deletem(dataList.get(i).getId());
								dataList.remove(i);
							}
						}
					}
					
				}
			}
			
			
			
			if (isConnecting&&!TextUtils.isEmpty(G.CURRENT_VALUE)) {
				dataList.get(bingpostion).setFileStatus(1);
			}
//			else {
//				dataList.get(bingpostion).setFileStatus(0);
//			}

			if (null != dataList) {
				Log.i("连接", "列表:" + dataList.size());
			}

			if (dataList != null) {
				info.setVisibility(View.GONE);
				serverAdapter.notifyDataSetChanged();
			} else {
				info.setVisibility(View.VISIBLE);
				serverAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 连接
	 */
	public void connect() {
		if (isConnecting == true)
			return;
		setAlert(getString(R.string.connecting));
		wrapper.addView(connectView);
		new Thread() {
			public void run() {
				isConnecting = true;
				isconfirmcon = true;
				boolean result = ConnectManager.connectServer(host);
				if (result) {
					serverManager.setHostStatus(context, host, 0);
					// serverManager.setHostStatus(context, host, 1);
					Message msg = handler.obtainMessage();
					msg.what = G.LOAD_SUCCESS;
					msg.sendToTarget();
				} else {
					Message msg = handler.obtainMessage();
					msg.what = G.LOAD_FAILED;
					msg.sendToTarget();
				}
			};
		}.start();
	}
	
	
	
	/**
	 * 二维码连接
	 */
	public void qrconnect() {
		if (isConnecting == true)
			return;
		setAlert(getString(R.string.connecting));
		wrapper.addView(connectView);
		new Thread() {
			public void run() {
				isConnecting = true;
				boolean result = ConnectManager.connectServer(host);
				if (result) {
					serverManager.setHostStatus(context, host, 0);
					// serverManager.setHostStatus(context, host, 1);
					Message msg = handler.obtainMessage();
					msg.what = G.LOAD_SUCCESS;
					msg.sendToTarget();
				} else {
					Message msg = handler.obtainMessage();
					msg.what = G.LOAD_FAILED;
					msg.sendToTarget();
				}
			};
		}.start();
	}
	

	/**
	 * 连接
	 */
	public void connectwww() {
		if (isConnecting == true)
			return;
		setAlert(getString(R.string.connecting));
		wrapper.addView(connectView);
		new Thread() {
			public void run() {
				isConnecting = true;
				is2codcon = true;
				boolean result = ConnectManager.connectServer(host);
				if (result) {
					Message msg = handler.obtainMessage();
					msg.what = 0;
					msg.sendToTarget();
				} else {
					Message msg = handler.obtainMessage();
					msg.what = G.LOAD_FAILED;
					msg.sendToTarget();
				}
			};
		}.start();
	}

	/**
	 * 连接后更新UI
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case G.LOAD_SUCCESS:
				Utils.toast(activity, "连接 " + host + " 成功！");
				info.setVisibility(View.GONE);
				saveConnectState();
				update();
				send2rcShare();
				finish();
				break;
			case G.LOAD_FAILED:
				Utils.toast(activity, "连接 " + host + " 失败！");
				break;

			case 0:
				info.setVisibility(View.GONE);
				secsuijiama();
				break;

			default:
				break;
			}
			wrapper.removeView(connectView);
			isConnecting = false;
		};
	};

	/**
	 * 搜索局域网机顶盒
	 */
	public void scan() {
		if (isScanning == true)
			return;
		setAlert("搜索中...");
		wrapper.addView(connectView);
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				isScanning = true;
				serverManager.setActivity(activity);
				// if (null!=dataList) {
				// for (int i = 0; i < dataList.size(); i++) {
				// delete(dataList.get(i).getId());
				// }
				// }

				// List<ServerBean> result = (List<ServerBean>)
				// serverManager.scan(context,scanHandler);

				List<ServerBean> result = (List<ServerBean>) serverManager
						.scan(context, scanHandler);
				Log.i(TAG, "设备数量:" + result.size());
				pagersize = result.size();
				if (result != null && result.size() > 0) {
					Message msg = scanHandler.obtainMessage();
					msg.what = G.LOAD_SUCCESS;
					msg.sendToTarget();
				} else {
					Message msg = scanHandler.obtainMessage();
					msg.what = G.LOAD_FAILED;
					msg.sendToTarget();
				}
			};
		}.start();
	}

	/**
	 * 搜索局域网机顶盒更新UI
	 */
	Handler scanHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case G.LOAD_SUCCESS:
				update();
				wrapper.removeView(connectView);
				isScanning = false;

				break;
			case G.LOAD_FAILED:
				Utils.log("没有找到机顶盒！");
				Utils.toast(activity, "没有找到机顶盒！");
				wrapper.removeView(connectView);
				isScanning = false;
				break;
			case G.LOAD_ING:
				String info = "搜索中...\nIP:" + msg.obj + "\n" + msg.arg1 + "%";
				setAlert(info);
				break;
			default:
				break;
			}

		};
	};

	/**
	 * 设置提示信息
	 * 
	 * @param info
	 */
	private void setAlert(String info) {
		TextView title = (TextView) connectView.findViewById(R.id.title);
		title.setText(info);
	}

	/**
	 * 机顶盒适配器
	 */
	public class ServerAdapter extends BaseAdapter {

		LayoutInflater layoutInflater = null;

		@Override
		public int getCount() {
			if (dataList == null)
				return 0;
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			if (dataList == null)
				return null;
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			final int myPostion = position;
			if (convertView == null) {
				if (layoutInflater == null) {
					layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = layoutInflater.inflate(R.layout.server_list_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.ip = (TextView) convertView.findViewById(R.id.ip);
				viewHolder.status = (TextView) convertView
						.findViewById(R.id.status);
				viewHolder.delete = (Button) convertView
						.findViewById(R.id.delete);
				viewHolder.connect = (Button) convertView
						.findViewById(R.id.connect);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (dataList != null && position < dataList.size()) {
				viewHolder.ip.setText(dataList.get(position).getName()+":"+dataList.get(position).getIp());
				if (dataList.get(position).getFileStatus() == CONNECT_ON) {
					viewHolder.status
							.setText(getString(R.string.status_connected));
					viewHolder.connect.setVisibility(View.GONE);
					viewHolder.delete.setVisibility(View.GONE);
				} else {
					viewHolder.status
							.setText(getString(R.string.status_not_connect));
					viewHolder.connect.setVisibility(View.VISIBLE);
					viewHolder.connect
							.setOnClickListener(new ConnectOnClickListener(
									position));
					viewHolder.delete.setVisibility(View.VISIBLE);
					viewHolder.delete
							.setOnClickListener(new DeleteOnClickListener(
									myPostion));
				}

			}
			return convertView;
		}

	}

	/**
	 * 幻灯列表控件
	 */
	class ViewHolder {
		TextView ip;
		TextView status;
		Button delete;
		Button connect;
	}

	/**
	 * 点击连接按钮
	 */
	class ConnectOnClickListener implements View.OnClickListener {
		int position;

		public ConnectOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (dataList != null && dataList.get(position) != null) {
				host = dataList.get(position).getIp();
				bingpostion = position;
				connectwww();
//				if (!isFirstConnect(host)) {
//					connectwww();
//				} else {
//					connect();
//				}
				
				// connect();
			}
		}
	}

	/**
	 * 点击删除按钮
	 */
	class DeleteOnClickListener implements View.OnClickListener {
		int position;

		public DeleteOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
//			Log.i(TAG, "数据:"+dataList.size());
			if (dataList != null && dataList.get(position) != null) {
				deleteAlert(dataList.get(position).getId());
			}
		}
	}

	private void deleteAlert(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.confirm_delete);
		DeleteListener pl = new DeleteListener(id);
		builder.setPositiveButton(getString(R.string.ok), pl);
		builder.setNegativeButton(getString(R.string.cancel), pl);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	public class DeleteListener implements DialogInterface.OnClickListener {

		int id;

		public DeleteListener(int id) {
			this.id = id;
		}

		@Override
		public void onClick(DialogInterface v, int btnId) {
			if (btnId == DialogInterface.BUTTON_POSITIVE) {
				delete(id);
			}
		}

	}

	private void delete(int id) {
		ServerBean tempBean = new ServerBean();
		tempBean.setContext(context);
		tempBean.setSelection(TableMateData._ID + "=?");
		tempBean.setSelectionArgs(new String[] { "" + id });
		serverManager.deleteData(tempBean);
//		pagersize--;
//		if (pagersize==0) {
//			
//		}
		update();
	}
	
	private void deletem(int id) {
		ServerBean tempBean = new ServerBean();
		tempBean.setContext(context);
		tempBean.setSelection(TableMateData._ID + "=?");
		tempBean.setSelectionArgs(new String[] { "" + id });
		serverManager.deleteData(tempBean);
	}
	

	/**
	 * 返回本页监听
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			host = bundle.getString("result");
			Log.i("设置", "扫描:" + host);
			if (host.indexOf("ip=") != -1) {
				host = host.split("ip=")[1];
			}
			Log.i("设置", "扫描:" + host);

			if (host != null && host.length() >= 7 /*
													 * && host.indexOf("192.")
													 * != -1
													 */) {
				try {
					if (host.indexOf("#") != -1 && host.lastIndexOf("#") != -1) {

						String key = host.split("#")[1]/*
														 * host.substring(host.
														 * indexOf("#") + 1)
														 */;
						if (key == null || (key != null && key.length() < 6))
							return;
						G.CURRENT_VALUE = key;
						Log.i("连接", "秘钥" + key);
						host = host.substring(host.indexOf("=") + 1,
								host.indexOf("#"));
						Utils.toast(activity, "key:" + key + "\nhost:" + host);
						if (null!=dataList) {
							for (int i = 0; i < dataList.size(); i++) {
								if (host.equals(dataList.get(i).getIp())) {
									delete(dataList.get(i).getId());
								}
							}
						}
						ServerBean tempBean = new ServerBean();
						tempBean.setContext(context);
						tempBean.setIp(host);
						tempBean.setFileStatus(0);
						tempBean.setSelection(TableMateData.IP + "=?");
						tempBean.setSelectionArgs(new String[] { host });
						
						
						serverManager.updateOrInsertData(tempBean);
						CodeUtils.saveSuijiCode(SettingConnectActivity.this,
								key);
						update();
						qrconnect();;
					} else {
						G.CURRENT_VALUE = "";
						ServerBean tempBean = new ServerBean();
						tempBean.setContext(context);
						tempBean.setIp(host);
						tempBean.setFileStatus(0);
						tempBean.setSelection(TableMateData.IP + "=?");
						tempBean.setSelectionArgs(new String[] { host });
						serverManager.updateOrInsertData(tempBean);
						update();
						qrconnect();;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Utils.toast(activity, "无效的机顶盒IP地址：" + host);
			}
		}
	}

	/**
	 * 点击事件
	 */
	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnScan:
			Intent openCameraIntent = new Intent(context, CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);
			break;
		case R.id.btnAutoScan:
			scan();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		is2codcon = false;
		isconfirmcon = false;
		IpUtils.destroyUp();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Log.i("连接", "=====onNewIntent=====:" + SuijiaMaActivity.isBing());
		Log.i("连接", "KEY:" + SuijiaMaActivity.getKey() + ":::CURRENT_VALUE:"
				+ G.CURRENT_VALUE);
		if (SuijiaMaActivity.isBing()) {
			SuijiaMaActivity.setBing(false);
			if (!"".equals(SuijiaMaActivity.getKey())
					&& G.CURRENT_VALUE.equals(SuijiaMaActivity.getKey())) {

				connect();
			} else {
				Utils.toast(activity, "连接失败");
				G.CURRENT_VALUE = SuijiaMaActivity.getKey();
			}

		}

		SuijiaMaActivity.setKey("");

	}

	private void secsuijiama() {
		Intent intent = new Intent();
		intent.setClass(SettingConnectActivity.this, SuijiaMaActivity.class);
		startActivity(intent);
	}

	private void send2rcShare() {
		Intent intent = new Intent();
		intent.setAction(G.ACTION_SHARE_);
		sendBroadcast(intent);
	}

	public void deleteHost(String host) {
		for (int i = 0; i < dataList.size(); i++) {
			if (host.equals(dataList.get(i).getIp())) {
				delete(dataList.get(i).getId());
				Log.i(TAG, "删除:" + host);
			}
		}
	}

	public static boolean sameHost(String host) {
		if (null == dataList) {
			return false;
		}
		for (int i = 0; i < dataList.size(); i++) {

			Log.i(TAG, "IP:" + dataList.get(i).getIp() + "host:" + host);

			if (host.equals(dataList.get(i).getIp())) {
				return true;
			}
		}

		return false;

	}

	private void saveConnectState(){
		connectList.clear();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("ip", host);
		map.put("code", G.CURRENT_VALUE);
		connectList.add(map);
	}
	
	
	private boolean isFirstConnect(String host){
		for (int i = 0; i < connectList.size(); i++) {
			Log.i(TAG, "现有IP:"+connectList.get(i).get("ip")+"::"+host);
			if (connectList.get(i).get("ip").toString().equals(host)&&!TextUtils.isEmpty(G.CURRENT_VALUE)) {
				return true;
			}
		}
		
		return false;
		
	}
}

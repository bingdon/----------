package cat.projects.dmc_service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.conn.util.InetAddressUtils;

import tv.luxs.config.G;
import tv.luxs.luxinterface.MInerface;
import tv.luxs.rcassistant.MainActivity;
import tv.luxs.rcassistant.RCActivity;
import tv.luxs.rcassistant.connect.ConnectManager;
import tv.luxs.rcassistant.utils.CodeUtils;
import tv.luxs.rcassistant.utils.Utils;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cat.projects.constant.DMCConstants;
import cat.projects.httpserver.NanoHTTPD;
import cat.projects.mediaplayer_ui.DMCRenderListActivity;

import com.dlna.datadefine.DLNA_ConnectionInfo;
import com.dlna.datadefine.DLNA_DeviceData;
import com.dlna.datadefine.DLNA_MediaInfo;
import com.dlna.datadefine.DLNA_PositionInfo;
import com.dlna.datadefine.DLNA_TransportInfo;
import com.dlna.datadefine.DLNA_TransportSettings;
import com.dlna.dmc.UpnpController;
import com.dlna.dmc.UpnpControllerInterface;

public class DMCMediaService extends Service {
	private static final String TAG = "DMCMediaService";

	private final Messenger mMessenger = new Messenger(new IncomingHandler());
	private Messenger cMessenger;

	private UpnpController upnpInstance = null;
	public List<DLNA_DeviceData> dmrList = new ArrayList<DLNA_DeviceData>();
	public DLNA_DeviceData currentDmr = null;
	private NanoHTTPD nh = null;

	public static final int CONTENT_FMT_UNKNOWN = 0x0001;
	public static final int CONTENT_FMT_VIDEO = 0x0002;
	public static final int CONTENT_FMT_AUDIO = 0x0004;
	public static final int CONTENT_FMT_PHOTO = 0x0008;
	public static final int CONTENT_FMT_DIR = 0x0010;
	public static List<String> videoFmt = null;
	public static List<String> audioFmt = null;
	public static List<String> photoFmt = null;

	public MyServiceHandler mServerHandler = new MyServiceHandler();
	private MyServiceBroadcastReceiver mServerReceiver = new MyServiceBroadcastReceiver();
	private final int MSG_SHOW_DMRLIST = 0x1001;
	private boolean mDlnaFoundingFlag = false; // record the first time called.
	private String mCurrentDMRName = "";
	private int mTotalTime = 0;
	private int mCurrentTime = 0;
	private Timer mGlobalTimer;
	private boolean mIsPlaying = false;
	public String mDLNAServerName = "test";
	private UPnPListener bingListener=new UPnPListener();

	public static int FileFmt(String _filename) {
		if (photoFmt == null) {
			photoFmt = new ArrayList<String>();
			photoFmt.add(".jpg");
			photoFmt.add(".jpeg");
			photoFmt.add(".bmp");
			photoFmt.add(".png");
			photoFmt.add(".tiff");
			photoFmt.add(".tga");
			photoFmt.add(".exif");
			photoFmt.add(".gif");
			photoFmt.add(".tif");
			photoFmt.add(".ppm");
			photoFmt.add(".qti");
			photoFmt.add(".qtf");
			photoFmt.add(".jpe");
			photoFmt.add(".ico");
			photoFmt.add(".pcd");
			photoFmt.add(".pnm");
			photoFmt.add(".qtif");
			photoFmt.add(".psd");
		}
		if (audioFmt == null) {
			audioFmt = new ArrayList<String>();
			audioFmt.add(".mpa");
			audioFmt.add(".wma");
			audioFmt.add(".mp2");
			audioFmt.add(".wav");
			audioFmt.add(".midi");
			audioFmt.add(".acm");
			audioFmt.add(".aif");
			audioFmt.add(".aifc");
			audioFmt.add(".aiff");
			audioFmt.add(".mp3");
			audioFmt.add(".wma");
			audioFmt.add(".ra");
			audioFmt.add(".flac");
			audioFmt.add(".ape");
			audioFmt.add(".ogg");
			audioFmt.add(".oga");
			audioFmt.add(".tta");
			audioFmt.add(".mpc");
			audioFmt.add(".m4a");
			audioFmt.add(".m4r");
			audioFmt.add(".m4p");
			audioFmt.add(".m4b");
			audioFmt.add(".3g2");
			audioFmt.add(".acc");
			audioFmt.add(".ac3");
			audioFmt.add(".pcm");
			audioFmt.add(".snd");
			audioFmt.add(".at3p");
			audioFmt.add(".au");
			audioFmt.add(".dts");
			audioFmt.add(".rmi");
			audioFmt.add(".mid");
			audioFmt.add(".mp1");
			audioFmt.add(".lpcm");
			audioFmt.add(".mka");
			audioFmt.add(".ram");
			audioFmt.add(".m3u");
		}
		if (videoFmt == null) {
			videoFmt = new ArrayList<String>();
			videoFmt.add(".m3u8");
			videoFmt.add(".ts");
			videoFmt.add(".asf");
			videoFmt.add(".avc");
			videoFmt.add(".avi");
			videoFmt.add(".dv");
			videoFmt.add(".divx");
			videoFmt.add(".xvid");
			videoFmt.add(".wmv");
			videoFmt.add(".mjpg");
			videoFmt.add(".mjpeg");
			videoFmt.add(".mpeg");
			videoFmt.add(".mpg");
			videoFmt.add(".mpe");
			videoFmt.add(".mp2p");
			videoFmt.add(".vob");
			videoFmt.add(".mp2t");
			videoFmt.add(".m1v");
			videoFmt.add(".m2v");
			videoFmt.add(".m4v");
			videoFmt.add(".mpg2");
			videoFmt.add(".mpeg2");
			videoFmt.add(".m4p");
			videoFmt.add(".mp4ps");
			videoFmt.add(".ogm");
			videoFmt.add(".xpm");
			videoFmt.add(".mkv");
			videoFmt.add(".rmvb");
			videoFmt.add(".mov");
			videoFmt.add(".hdmov");
			videoFmt.add(".mp4");
			videoFmt.add(".rm");
			videoFmt.add(".3gp");
			videoFmt.add(".3gpp");
			videoFmt.add(".tp");
			videoFmt.add(".m2p");
			videoFmt.add(".m2ts");
			videoFmt.add(".flv");
			videoFmt.add(".f4v");
			videoFmt.add(".hlv");
			videoFmt.add(".h4v");
			videoFmt.add(".dat");
		}
		int _dot = _filename.lastIndexOf(".");
		if (_dot < 0)
			return CONTENT_FMT_UNKNOWN;
		_filename = _filename.substring(_dot);
		int i, size = videoFmt.size();
		for (i = 0; i < size; i++) {
			if (_filename.compareToIgnoreCase(videoFmt.get(i)) == 0)
				return CONTENT_FMT_VIDEO;
		}
		size = audioFmt.size();
		for (i = 0; i < size; i++) {
			if (_filename.compareToIgnoreCase(audioFmt.get(i)) == 0)
				return CONTENT_FMT_AUDIO;
		}
		size = photoFmt.size();
		for (i = 0; i < size; i++) {
			if (_filename.compareToIgnoreCase(photoFmt.get(i)) == 0)
				return CONTENT_FMT_PHOTO;
		}
		return CONTENT_FMT_UNKNOWN;
	}

	public static void do_sleep(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String GetDidlFmtString(String name, String fmtstr, String metastr,
			String uri, long metasize, boolean auth/* Optional */,
			String passwd/* Optional */) {
		String linestr = "";
		linestr = "<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\">";
		linestr += "<item id=\"1\" parentID=\"-1\" restricted=\"1\">";
		linestr += "<upnp:storageMedium>UNKNOWN</upnp:storageMedium>";
		linestr += "<upnp:writeStatus>UNKNOWN</upnp:writeStatus>";
		linestr += "<dc:title>" + name + "</dc:title>";
		linestr += "<upnp:class>" + fmtstr + "</upnp:class>";
		linestr += "<res protocolInfo=\"http-get:*:" + metastr + ":*\"";
		if (metasize > 0) {
			linestr += " size=\"" + metasize + "\"";
		}
		linestr += ">" + uri + "</res>";
		if (auth == true && passwd != null && passwd.length() > 0) {
			linestr += "<upnp:password>" + passwd + "</upnp:password>";
		}
		linestr += "</item>";
		linestr += "</DIDL-Lite>";
		return linestr;
	}

	public static boolean isSameNetwork(String ipsrc, String ipdst) {
		if (ipsrc == null)
			return true;
		if (ipdst == null)
			return false;
		int slash1 = ipsrc.lastIndexOf('.');
		int slash2 = ipdst.lastIndexOf('.');
		if (slash1 > 0 && slash2 > 0) {
			String net1 = ipsrc.substring(0, slash1);
			String net2 = ipdst.substring(0, slash2);
			return net1.equals(net2);
		}
		return false;
	}

	// ???????ж?????????????????????DMR??IP???????IP?????
	public static String getLocalIpAddr(String dstIp) {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					String localIp = inetAddress.getHostAddress().toString();
					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(localIp)
							&& isSameNetwork(localIp, dstIp))
						return localIp;
				}
			}
		} catch (SocketException es) {
			es.printStackTrace();
		}
		return "127.0.0.1";
	}

	public static String convertToTime(int time) {
		int h = time / 3600;
		int m = time % 3600 / 60;
		int s = time % 60;
		String result = String.format("%02d:%02d:%02d", h, m, s);
		return result;
	}

	public void setCurrentPlayer(DLNA_DeviceData _dmr) {
		currentDmr = _dmr;
		if (currentDmr != null){
			upnpInstance.SetMR(_dmr);
		}
	}

	public void startPlay(String filename) {
		// /mnt/sdcard/test.mp4 // 完整的文件路径
		// String res_uri = "http://127.0.0.1/test.mp4";
		String res_uri = filename;
//		 String res_uri = "/sdcard/bing.png";
		int fmt = FileFmt(res_uri);
		String metainfo = "";
		int slash = res_uri.lastIndexOf('/');
		if (slash > 0 && slash < res_uri.length()) {
			nh.setUri(res_uri);
			String fileName = res_uri.substring(slash + 1);
			String s = nh.encodeUri(fileName);
			res_uri = "http://" + getLocalIpAddr(currentDmr.localip) + ":"
					+ nh.getPort() + "/" + s;
		}
		boolean auth = true;
			if (TextUtils.isEmpty(G.CURRENT_VALUE)) {
				CodeUtils.getSuijiCode(RCActivity.activity);
			}
		String passwd =G.CURRENT_VALUE;
		Log.i(TAG, "加密前:"+passwd);
		passwd=DMCMediaService.cd(passwd, 1);
		Log.i(TAG, "加密后:"+passwd);
		if (fmt == CONTENT_FMT_VIDEO) {
			metainfo = GetDidlFmtString(filename,
					"object.item.videoItem.movie", "video/mpeg", res_uri, -1,
					auth, passwd);
		} else if (fmt == CONTENT_FMT_AUDIO) {
			metainfo = GetDidlFmtString(filename,
					"object.item.audioItem.music", "audio/mpeg", res_uri, -1,
					auth, passwd);
		} else if (fmt == CONTENT_FMT_PHOTO) {
			metainfo = GetDidlFmtString(filename,
					"object.item.imageItem.photo", "image/jpg", res_uri, -1,
					auth, passwd);
		}
//		Log.i(TAG, "发送:"+upnpInstance.SetCustomURI(res_uri, metainfo));
		upnpInstance.SetCustomURI(res_uri, metainfo);
	}

	public void pausePlay() {
		if (currentDmr != null)
			upnpInstance.Pause();
	}

	public void resumePlay() {
		if (currentDmr != null)
			upnpInstance.Play();
	}

	public void stopplay(){
		if (currentDmr != null) {
			upnpInstance.Pause();
			upnpInstance.Stop();
		}
	}
	
	public void resumePlay(int pos) {
		if (pos >= 0) {
			String tstring = convertToTime(pos); // 0-duration, 秒为单位
			if (currentDmr != null)
				upnpInstance.Seek(tstring);

		}
	}

	public void stopPlay() {
		if (currentDmr != null)
			upnpInstance.Stop();
	}

	private DLNA_DeviceData clone_DLNA_DeviceData(DLNA_DeviceData _device) {
		DLNA_DeviceData devinst = new DLNA_DeviceData();
		devinst.devicename = new String(_device.devicename);
		devinst.iconurl = new String(_device.iconurl);
		devinst.localip = new String(_device.localip);
		devinst.serialNumber = new String(_device.serialNumber);
		devinst.type = _device.type;
		devinst.URN = new String(_device.URN);
		devinst=_device;
		return devinst;
	}

	public DMCMediaService() {
	}

	@SuppressLint("HandlerLeak")
	class IncomingHandler extends Handler {
		private String mPlayUri = "";

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DMCConstants.MEDIA_DMC_CTL_MSG_GETLIST:
				if (!mDlnaFoundingFlag) { // first time play.
					List<DLNA_DeviceData> _dmrList = upnpInstance.GetMR();
					Log.i(TAG, "设备数量:"+_dmrList.size());
					if (_dmrList != null && _dmrList.size() > 0) {
						List<DLNA_DeviceData> _retList = new ArrayList<DLNA_DeviceData>();
						int size = _dmrList.size();
						for (int i = 0; i < size; i++) {
							DLNA_DeviceData devinst = clone_DLNA_DeviceData(_dmrList
									.get(i));
							
							Log.v(TAG, "DLNA found DMR: " + devinst.localip
									+ "[" + devinst.devicename + "]");
							
							if ( devinst.localip
									.equals(ConnectManager.myhost)) {
								mDlnaFoundingFlag = true;
								mCurrentDMRName = mDLNAServerName;
								setCurrentPlayer(devinst);
								mPlayUri = (String) msg.obj;
//								startPlay(mPlayUri);

								cMessenger = msg.replyTo;
								Message play = Message.obtain(null,
										DMCConstants.MEDIA_DMC_CTL_MSG_PLAY,
										"hi string");
								mPlayUri = (String) msg.obj;
								startPlay(mPlayUri);
								Log.d(TAG, "Got message from UI, MSG_PLAY"
										+ mPlayUri);
								try {
									cMessenger.send(play);
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							Log.v(TAG, "DLNA found DMR: " + devinst.localip
									+ "[" + devinst.devicename + "]");
							_retList.add(devinst);
						}
						dmrList = _retList;
					}
					Log.v(TAG, "MEDIA MSG GETLIST was sent");
					if (!mDlnaFoundingFlag) {
						// send event to notify there is no dmr.
						cMessenger = msg.replyTo;
						Message noDMR = Message.obtain(null,
								DMCConstants.MEDIA_DMC_CTL_MSG_GETLIST,
								"hi string");

						Log.d(TAG, "No dmr was found");
						try {
							cMessenger.send(noDMR);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				break;
			case DMCConstants.MEDIA_DMC_CTL_MSG_PLAY:
				// here need to send another type to configure when user not set
				// dmr
				if (!mDlnaFoundingFlag) { // first time play.
					List<DLNA_DeviceData> _dmrList = upnpInstance.GetMR();
					Log.i(TAG, "设备数量:"+_dmrList.size());
					if (_dmrList != null && _dmrList.size() > 0) {
					
						List<DLNA_DeviceData> _retList = new ArrayList<DLNA_DeviceData>();
						int size = _dmrList.size();
						for (int i = 0; i < size; i++) {
							
							DLNA_DeviceData devinst = clone_DLNA_DeviceData(_dmrList
									.get(i));
							Log.v(TAG, "DLNA found DMR: " + devinst.localip
									+ "[" + devinst.devicename + "]");
							if ( devinst.localip
									.equals(ConnectManager.myhost)) {
								mDlnaFoundingFlag = true;
								mCurrentDMRName = mDLNAServerName;
								setCurrentPlayer(devinst);
								mPlayUri = (String) msg.obj;
//								startPlay(mPlayUri);

								cMessenger = msg.replyTo;
								Message play = Message.obtain(null,
										DMCConstants.MEDIA_DMC_CTL_MSG_PLAY,
										"hi string");
								mPlayUri = (String) msg.obj;
								startPlay(mPlayUri);
								Log.d(TAG, "Got message from UI, MSG_PLAY"
										+ mPlayUri);
								try {
									cMessenger.send(play);
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							_retList.add(devinst);
						}
						dmrList = _retList;
					}
					Log.v(TAG, "MEDIA MSG GETLIST was sent");
					/*
					 * if (dmrList.size() != 0 && !mDlnaFoundingFlag) {
					 * Log.v(TAG, "dmrListSize is not 0"); Message msg1 = new
					 * Message(); msg1.arg1 = MSG_SHOW_DMRLIST; msg1.obj =
					 * dmrList; mServerHandler.sendMessage(msg1);
					 * 
					 * // need to add the exception handler. } else
					 */
					if (!mDlnaFoundingFlag) {
						// send event to notify there is no dmr.
						cMessenger = msg.replyTo;
						Message noDMR = Message.obtain(null,
								DMCConstants.MEDIA_DMC_CTL_MSG_GETLIST,
								"hi string");

						Log.d(TAG, "No dmr was found");
						try {
							cMessenger.send(noDMR);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					cMessenger = msg.replyTo;
					Message play = Message.obtain(null,
							DMCConstants.MEDIA_DMC_CTL_MSG_PLAY, "hi string");
					mPlayUri = (String) msg.obj;
					startPlay(mPlayUri);
					Log.d(TAG, "Got message from UI, MSG_PLAY" + mPlayUri);
					try {
						cMessenger.send(play);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;

			case DMCConstants.MEDIA_DMC_CTL_MSG_PAUSE:
				cMessenger = msg.replyTo;

				Log.d(TAG, "Got message from UI, MSG_PAUSE");
				pausePlay();

				break;
			case DMCConstants.MEDIA_DMC_CTL_MSG_SEEK:
				cMessenger = msg.replyTo;
				Message seek = Message.obtain(null,
						DMCConstants.MEDIA_DMC_CTL_MSG_SEEK, "seek string");
				// mAddress = (String) msg.obj;
				int position = Integer.parseInt((String) msg.obj);
				resumePlay(position);
				Log.d(TAG, "Got message from UI, MSG_SEEK" + position);

				break;
			case DMCConstants.MEDIA_DMC_CTL_MSG_TOTALTIME:
				cMessenger = msg.replyTo;
				upnpInstance.GetPositionInfo();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Message totalTime = Message.obtain(null,
						DMCConstants.MEDIA_DMC_CTL_MSG_TOTALTIME, null);
				totalTime.obj = (mTotalTime);

				Log.d(TAG, "Got message from UI, MSG_TOTALTIME");

				try {
					cMessenger.send(totalTime);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				break;

			case DMCConstants.MEDIA_DMC_CTL_STOP:
				Log.i(TAG, "停止播放");
				stopplay();
				break;
				
				
			default:
				super.handleMessage(msg);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	public void onCreate() {
		super.onCreate();
		mGlobalTimer = new Timer(true);
		initControllerService();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.dmc.renderlist");
		registerReceiver(mServerReceiver, intentFilter);
		Log.d(TAG, "MediaRenderService onCreate");
	}
	
	

	private void initControllerService() {
		startEngine();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "MediaRenderService onDestroy");
		stopEngine();
		unregisterReceiver(mServerReceiver);
		super.onDestroy();
	}

	class MyServiceHandler extends Handler {

		public MyServiceHandler() {

		}

		public MyServiceHandler(Looper L) {
			super(L);
		}

		@Override
		public void handleMessage(Message msg) {

			Log.d("MyHandler", "handleMessage......");
			super.handleMessage(msg);
			switch (msg.arg1) {
			case MSG_SHOW_DMRLIST:
				mDlnaFoundingFlag = true;
				Intent intent = new Intent(getApplicationContext(),
						DMCRenderListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putParcelableArrayListExtra("renderlist",
						(ArrayList<? extends Parcelable>) dmrList);
				startActivity(intent);
				break;
			}

		}
	}

	private class MyServiceBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int position = intent.getIntExtra("renderlist", 0);
			setCurrentPlayer(dmrList.get(position));
			mCurrentDMRName = dmrList.get(position).devicename; // the user have
																// already
			// chose the dmr.
		}
	}

	public class UPnPListener implements UpnpControllerInterface {

		@Override
		public void OnUpdateServerDevice() {
			// δ??
		}

		@Override
		public void OnUpdateRenderDevice() {
			List<DLNA_DeviceData> _dmrList = upnpInstance.GetMR(); // ???????DMR?豸????AirPin?????
			if (_dmrList != null && _dmrList.size() > 0) {
				List<DLNA_DeviceData> _retList = new ArrayList<DLNA_DeviceData>();
				int size = _dmrList.size();
				boolean flag = false;
				for (int i = 0; i < size; i++) {
					// ???clone????????????????????á?????JNI?????????Щ????clone???????????????list??UI???
					DLNA_DeviceData devinst = clone_DLNA_DeviceData(_dmrList
							.get(i));
					Log.v(TAG, "DLNA found DMR: " + devinst.localip + "["
							+ devinst.devicename + "]");
					_retList.add(devinst);
					if (devinst.devicename.equals(mCurrentDMRName)) {
						flag = true;
					}
				}
				dmrList = _retList;
				if (!flag) {
					mDlnaFoundingFlag = false;
					mIsPlaying = false;
					Message dmrlost = Message.obtain(null,
							DMCConstants.MEDIA_DMC_CTL_MSG_DMRLOST,
							"dmr lost string");
					// mAddress = (String) msg.obj;
					Log.d(TAG, "send message to UI, MSG_dmr lost");
					try {
						if (cMessenger != null) {
							cMessenger.send(dmrlost);
						}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			// if dmrList lost, send event to notify chose a new one.

			Log.d(TAG, "Got message from UI, MSG_GETLIST");

		}

		@Override
		public void OnStopResult(int res, DLNA_DeviceData device) {
			// ????? Stop ??????res == 0 ??????
		}

		@Override
		public void OnPauseResult(int res, DLNA_DeviceData device) {
			// ????? Pause ??????res == 0 ??????
			if (res == 0) {
				Message pause = Message.obtain(null,
						DMCConstants.MEDIA_DMC_CTL_MSG_PAUSE, "pause string");
				// mAddress = (String) msg.obj;
				Log.d(TAG, "Got message from UI, MSG_PAUSE");
				try {
					if (cMessenger != null) {
						cMessenger.send(pause);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void OnSetAVTransportURIResult(int res, DLNA_DeviceData device) {
			// ????? SetAVTransportURI ??????res == 0 ??????
			Log.i(TAG, "OnSetAVTransportURIResult:"+res);
			
//			Toast.makeText(getApplicationContext(), "结果:"+res, Toast.LENGTH_LONG).show();
			if (res == 0) {
				upnpInstance.Play(); // ?????????? Play
				ConnectManager.sendPlayStatue();						// ???????????????????????????????????UI??????UI???????????
										// Play
			} else if (res == 401) {
				Intent intent =new Intent();
				intent.setAction(G.ACTION_ERRO_PASSWORD);
				sendBroadcast(intent);
				ConnectManager.sendGetPassword();
				// AirPin
				// ?????DLNA????????????????????????????????????????????ú????????
				// auth ?? passwd ??????
				// ??passwd ?????????????????????????й???????????
				
			}
		}

		@Override
		public void OnPlayResult(int res, DLNA_DeviceData device) {
			// ????? Play ??????res == 0
			// ????????????????UI???????????????????????
			Log.i(TAG, "OnPlayResult:"+res);
			if (res == 0) {
				Message play = Message.obtain(null,
						DMCConstants.MEDIA_DMC_CTL_MSG_PLAY, "hi string");
				try {
					if (cMessenger != null) {
						cMessenger.send(play);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		
		
		@Override
		public void OnGetPositionInfoResult(int res, DLNA_DeviceData device,
				DLNA_PositionInfo info) {
			Log.i(TAG, "OnGetPositionInfoResult:"+res);
			if (res == 0) {
				mTotalTime = (int) info.track_duration;
				mCurrentTime = (int) info.rel_time / 1000000;
				Log.d(TAG, info.abs_time + "..." + info.rel_time + "dddd"
						+ info.abs_count);
				Message totalTime = Message.obtain(null,
						DMCConstants.MEDIA_DMC_CTL_MSG_TOTALTIME, null);
				totalTime.obj = (mTotalTime);
				Log.d(TAG, "Got Total time form dmr" + mTotalTime);

				try {
					if (cMessenger != null)
						cMessenger.send(totalTime);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message currentTime = Message.obtain(null,
						DMCConstants.MEDIA_DMC_CTL_MSG_CURRENTTIME, null);
				currentTime.obj = (mCurrentTime);

				try {
					if (cMessenger != null)
						cMessenger.send(currentTime);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void OnGetTransportInfoResult(int res, DLNA_DeviceData device,
				DLNA_TransportInfo info) {
			Log.i(TAG, "OnGetTransportInfoResult:"+res);
			if (info.cur_transport_state.equalsIgnoreCase("PLAYING")) {
				// ??????
				mIsPlaying = true;
				Message play = Message.obtain(null,
						DMCConstants.MEDIA_DMC_CTL_MSG_PLAY, "hi string");
				try {
					if (cMessenger != null) {
						cMessenger.send(play);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (info.cur_transport_state
					.equalsIgnoreCase("PAUSED_PLAYBACK")) {
				Message pause = Message.obtain(null,
						DMCConstants.MEDIA_DMC_CTL_MSG_PAUSE, "pause string");
				// mAddress = (String) msg.obj;
				Log.d(TAG, "Got message from UI, MSG_PAUSE");
				try {
					if (cMessenger != null) {
						cMessenger.send(pause);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// ?????
			} else if (info.cur_transport_state.equalsIgnoreCase("STOPPED")) {
				// ???
				mIsPlaying = false;
				Log.d(TAG, "playing session is over.");
			}
		}

		@Override
		public void OnSeekResult(int res, DLNA_DeviceData device) {
			// ????? Seek ??????res == 0 ??????
			Log.d(TAG, "getCurrent Seek result" + res);
		}

		@Override
		public void OnSetVolumeResult(int res, DLNA_DeviceData device) {
			// ????? SetVolume ??????res == 0 ??????
		}

		@Override
		public void OnGetVolumeResult(int res, DLNA_DeviceData device,
				String channel, int volume) {
			// ????? GetVolume ??????res == 0 ??????
			// volume ????豸??????? 0-100
		}

		@Override
		public void OnGetCurrentTransportActionsResult(int res,
				DLNA_DeviceData device) {
			// δ??
		}

		@Override
		public void OnGetDeviceCapabilitiesResult(int res,
				DLNA_DeviceData device) {
			// δ??
		}

		@Override
		public void OnGetMediaInfoResult(int res, DLNA_DeviceData device,
				DLNA_MediaInfo info) {
			// δ??
		}

		@Override
		public void OnMRStateVariablesChanged(int state, String value) {
			// δ??
		}

		@Override
		public void OnGetTransportSettingsResult(int res,
				DLNA_DeviceData device, DLNA_TransportSettings settings) {
			// δ??
		}

		@Override
		public void OnNextResult(int res, DLNA_DeviceData device) {
			// δ??
		}

		@Override
		public void OnPreviousResult(int res, DLNA_DeviceData device) {
			// δ??
		}

		@Override
		public void OnSetPlayModeResult(int res, DLNA_DeviceData device) {
			// δ??
		}

		@Override
		public void OnGetCurrentConnectionIDsResult(int res,
				DLNA_DeviceData device, String ids) {
			// δ??
		}

		@Override
		public void OnGetCurrentConnectionInfoResult(int res,
				DLNA_DeviceData device, DLNA_ConnectionInfo info) {
			// δ??
		}

		@Override
		public void OnGetProtocolInfoResult(int res, DLNA_DeviceData device,
				List<String> sources, List<String> sinks) {
			// δ??
		}

		@Override
		public void OnSetMuteResult(int res, DLNA_DeviceData device) {
			// δ??
		}

		@Override
		public void OnGetMuteResult(int res, DLNA_DeviceData device,
				String channel, boolean mute) {
			// δ??
		}

		@Override
		public void OnX_SlideShowResult(int res, DLNA_DeviceData device) {
			// δ??
		}

		@Override
		public void OnX_FastForwardResult(int res, DLNA_DeviceData device) {
			// δ??
		}

		@Override
		public void OnX_RewindResult(int res, DLNA_DeviceData device) {
			// δ??
		}

		@Override
		public void OnSetKeyResult(int res, DLNA_DeviceData device) {
			// δ??
			Log.i(TAG, "OnSetKeyResult:"+res);
		}

		@Override
		public void OnSetMouseResult(int res, DLNA_DeviceData device) {
			// δ??
		}

		@Override
		public void OnSetMessageResult(int res, DLNA_DeviceData device) {
			// δ??
		}
	}

	public boolean startEngine() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				if (upnpInstance == null)
					upnpInstance = new UpnpController(); // ???????????
															// System.load()
				if (upnpInstance != null)
					upnpInstance.initUpnpProtocolControllerStack(bingListener, "", "AirPinSender", true, "");
				if (nh == null) {
					try {
						nh = new NanoHTTPD(55666);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}.start();
		// call the upnp interface every one second.
		TimerTask task = new TimerTask() {
			public void run() {

				if (upnpInstance != null) {
					upnpInstance.GetPositionInfo();
					if (mIsPlaying) {
						upnpInstance.GetTransportInfo();
					}
				}
//				Log.d(TAG, "upnp get positioninfo was called...");
			}
		};
		mGlobalTimer.schedule(task, 3000, 1000);
		return true;
	}

	public boolean stopEngine() {
		// TODO Auto-generated method stub
		currentDmr = null;
		new Thread() {
			public void run() {
				Log.v(TAG, "UPnP Destroy...\n");
				if (nh != null) {
					nh.stop();
					nh = null;
				}
				if (upnpInstance != null) {
					
					upnpInstance.deinitUpnpProtocolControllerStack();
					upnpInstance = null;
				}
				Log.v(TAG, "UPnP Destroy done\n");
			}
		}.start();
		// while (eventHandler != null)
		do_sleep(100);
		return true;
	}

	public boolean restartEngine() {
		// TODO Auto-generated method stub
		return false;
	}

	public static native String cd(String origString, int codec);

	static{
		System.loadLibrary("codec");
	}
	
	
}

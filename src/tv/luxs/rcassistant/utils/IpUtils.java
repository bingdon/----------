package tv.luxs.rcassistant.utils;

import java.util.List;

import tv.luxs.luxinterface.MInerface;
import cat.projects.httpserver.NanoHTTPD;

import com.dlna.datadefine.DLNA_ConnectionInfo;
import com.dlna.datadefine.DLNA_DeviceData;
import com.dlna.datadefine.DLNA_MediaInfo;
import com.dlna.datadefine.DLNA_PositionInfo;
import com.dlna.datadefine.DLNA_TransportInfo;
import com.dlna.datadefine.DLNA_TransportSettings;
import com.dlna.dmc.UpnpController;
import com.dlna.dmc.UpnpControllerInterface;

public class IpUtils {

	public static UpnpController upnpController = null;
	public static UpnpController getUpnpController() {
		return upnpController;
	}

	public static void setUpnpController(UpnpController upnpController) {
		IpUtils.upnpController = upnpController;
	}

	public static NanoHTTPD nanoHTTPD=null;
	
	
	public static void searchIp() {

	}

	/**
	 * ip数量
	 * 
	 * @return 数量
	 */
	public static int IpNum() {
		return upnpController.GetMR().size();
	}

	/**
	 * 获取列表
	 * 
	 * @return 发乎列表
	 */
	public static List<DLNA_DeviceData> getDeviceData() {
		return upnpController.GetMR();
	}

	/**
	 * 初始化
	 */
	public static void initUpCoTro() {
		upnpController = new UpnpController();
//		upnpController.initUpnpProtocolControllerStack(
//				new MInerface(), "", "AirPinSender", true, "");
	}

	/**
	 * 停止
	 */
	public static void destroyUp() {
		new Thread(){
			public void run() {
				
				if (null!=upnpController) {
//					upnpController.deinitUpnpProtocolControllerStack();
					upnpController = null;
					
				}
			};
		}.start();
		

	}

}

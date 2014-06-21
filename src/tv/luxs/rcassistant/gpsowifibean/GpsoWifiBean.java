package tv.luxs.rcassistant.gpsowifibean;

import tv.luxs.rcassistant.connect.ServerBean;

public class GpsoWifiBean extends ServerBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 纬度
	private String lat;
	// 经度
	private String lon;
	// wifi名称
	private String wifi_ssid;

	public String getWifi_ssid() {
		return wifi_ssid;
	}

	public void setWifi_ssid(String wifi_ssid) {
		this.wifi_ssid = wifi_ssid;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	
	

}

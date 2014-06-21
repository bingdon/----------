package net.zhaidian.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class GpsoWifiData {

	public static final String AUTHORTY = "net.zhaidian.provider.GpsowifiProvider";
	public static final class GpsTableMateData implements BaseColumns{
		public static final String TABLE_NAME = "gpsowifi";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORTY + "/" + TABLE_NAME);
		public static final String CONTENT_URI_ID = "content://" + AUTHORTY + "/" + TABLE_NAME + "/";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhaidian.gpsowifi.";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.zhaidian.provider.gpsowifi";
		
		public static final String DEFAULT_SORT_ORDER = _ID;
		
//		public static final String GPSOWIFI = "gpsowifi";
		
		public static final String LAT="lat";
		
		public static final String LON="lon";
		
		public static final String WIFI_SSID="ssid";
	}
}

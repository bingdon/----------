package net.zhaidian.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class IPsProviderMateData {
	public static final String AUTHORTY = "net.zhaidian.provider.IPsProvider";
	
	public static final class TableMateData implements BaseColumns{
		public static final String TABLE_NAME = "ips";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORTY + "/" + TABLE_NAME);
		public static final String CONTENT_URI_ID = "content://" + AUTHORTY + "/" + TABLE_NAME + "/";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhaidian.ips.";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.zhaidian.provider.ips";
		
		public static final String STATUS = "status";
		public static final String IP = "ip";
		public static final String NAME = "name";

		public static final String DEFAULT_SORT_ORDER = _ID;
		
		
//		public static final String GPSOWIFI = "gpsowifi";
//		
//		public static final String LAT="lat";
//		
//		public static final String LON="lon";
//		
//		public static final String WIFI_SSID="ssid";
	}
}

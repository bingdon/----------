package tv.luxs.rcassistant.gpsowifibean;

import java.util.ArrayList;
import java.util.List;

import tv.luxs.config.G;
import tv.luxs.rcassistant.connect.ServerBean;
import tv.luxs.rcassistant.utils.Utils;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import net.zhaidian.file.FileBean;
import net.zhaidian.file.ISqliter;
import net.zhaidian.provider.GpsoWifiData.GpsTableMateData;

public class GpsoWifiSqliter implements ISqliter {

	@Override
	public List<?> query(FileBean fileBean) {
		// TODO Auto-generated method stub
		if (fileBean != null) {
			String selection = fileBean.getSelection();
			String[] selectionArgs = fileBean.getSelectionArgs();
			String orderBy = fileBean.getOrderBy();

			int startPosition = 0;
			int pageSize = 10;
			if (fileBean.getCurrentPage() != 0) {
				startPosition = (fileBean.getCurrentPage() - 1)
						* fileBean.getPageSize();
			}
			if (fileBean.getPageSize() != 0) {
				pageSize = fileBean.getPageSize();
			}

			Uri uri = GpsTableMateData.CONTENT_URI;
			ContentResolver cr = fileBean.getContext().getContentResolver();
			Cursor cursor = cr.query(uri, null, selection, selectionArgs,
					orderBy);
			Utils.log("cursor:" + cursor);
			if (null==cursor) {
				return null;
			}
			Utils.log("cursor:" + cursor.getCount());
			List<ServerBean> resultLists = null;
			Log.i("IPsSqliter", "开始:" + startPosition + "====pageSize:"
					+ pageSize);
			if (cursor.getCount() > 0) {
				resultLists = new ArrayList<ServerBean>();
				for (int i = 0, j = startPosition; i < pageSize
						&& cursor.moveToPosition(j); i++, j++) {
					GpsoWifiBean tempBean = new GpsoWifiBean();
					tempBean.setId(cursor.getInt(cursor
							.getColumnIndex(GpsTableMateData._ID)));
					tempBean.setLat(cursor.getString(cursor
							.getColumnIndex(GpsTableMateData.LAT)));
					tempBean.setLon(cursor.getString(cursor
							.getColumnIndex(GpsTableMateData.LON)));
					tempBean.setWifi_ssid(cursor.getString(cursor
							.getColumnIndex(GpsTableMateData.WIFI_SSID)));
					resultLists.add(tempBean);
				}
			}

			cursor.close();
			return resultLists;
		} else {
			return null;
		}
	}

	@Override
	public long insert(FileBean fileBean) {
		// TODO Auto-generated method stub
		if (fileBean != null) {
			ContentValues values = new ContentValues();
			GpsoWifiBean tempBean = (GpsoWifiBean) fileBean;
			values.put(GpsTableMateData.LAT, tempBean.getLat());
			values.put(GpsTableMateData.LON, tempBean.getLon());
			values.put(GpsTableMateData.WIFI_SSID, tempBean.getWifi_ssid());

			ContentResolver cr = fileBean.getContext().getContentResolver();
			Uri uri = GpsTableMateData.CONTENT_URI;
			Uri insertedUri = cr.insert(uri, values);

			long result = 0;
			if (insertedUri != null) {
				result = G.SQL_INSERT_SUCCESS;
			}
			return result;
		} else {
			return G.SQL_ERROR;
		}
	}

	@Override
	public int update(FileBean fileBean) {
		// TODO Auto-generated method stub

		if (fileBean != null) {
			String selection = fileBean.getSelection();
			String[] selectionArgs = fileBean.getSelectionArgs();
			// Log.d(G.TAG,"selection:" + selection + " | selectionArgs:" +
			// selectionArgs[0]);
			ContentValues values = new ContentValues();
			ServerBean tempBean = (ServerBean) fileBean;

//			values.put(GpsTableMateData.STATUS, tempBean.getFileStatus());

			ContentResolver cr = fileBean.getContext().getContentResolver();
			Uri uri = GpsTableMateData.CONTENT_URI;
			int result = cr.update(uri, values, selection, selectionArgs);
			return result;
		} else {
			return G.SQL_ERROR;
		}
	}

	@Override
	public int delete(FileBean fileBean) {
		// TODO Auto-generated method stub
		if (fileBean != null) {
			String selection = fileBean.getSelection();
			String[] selectionArgs = fileBean.getSelectionArgs();

			ContentResolver cr = fileBean.getContext().getContentResolver();
			Uri uri = GpsTableMateData.CONTENT_URI;
			int result = cr.delete(uri, selection, selectionArgs);
			return result;
		} else {
			return G.SQL_ERROR;
		}
	}

	@Override
	public int getLocalCount(FileBean fileBean) {
		// TODO Auto-generated method stub
		if (fileBean != null) {
			String selection = fileBean.getSelection();
			String[] selectionArgs = fileBean.getSelectionArgs();
			// Log.d(G.TAG,"selection:" + selection + " | selectionArgs:" +
			// selectionArgs[0]);
			Uri uri = GpsTableMateData.CONTENT_URI;
			ContentResolver cr = fileBean.getContext().getContentResolver();
			Cursor cursor = cr.query(uri, null, selection, selectionArgs, null);
			int count = cursor.getCount();
			cursor.close();
			return count;
		} else {
			return 0;
		}
	}

	@Override
	public int getPageCount(FileBean fileBean) {
		// TODO Auto-generated method stub

		if (fileBean != null) {
			int count = getLocalCount(fileBean);
			if (count == 0) {
				return 0;
			} else {
				int allPage = count % fileBean.getPageSize() == 0 ? count
						/ fileBean.getPageSize() : count
						/ fileBean.getPageSize() + 1;
				return allPage;
			}
		} else {
			return 0;
		}
	}

}

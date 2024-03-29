package net.zhaidian.provider;

import java.sql.SQLException;
import java.util.HashMap;

import net.zhaidian.db.MyDatabase;
import net.zhaidian.provider.GpsoWifiData.GpsTableMateData;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class GpsowifiProvider extends ContentProvider{

	//FileSqliteHelper dh ;
	SQLiteDatabase db;
	public static final UriMatcher uriMatcher;
	public static final int ITEM = 1;
	public static final int ID = 2;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(GpsoWifiData.AUTHORTY, GpsTableMateData.TABLE_NAME, ITEM);
		uriMatcher.addURI(GpsoWifiData.AUTHORTY, GpsTableMateData.TABLE_NAME + "/#", ID);
	}
	
	public static HashMap<String , String> projectionMap;
	
	static{
		projectionMap = new HashMap<String, String>();
		projectionMap.put(GpsTableMateData._ID, GpsTableMateData._ID);
		projectionMap.put(GpsTableMateData.LAT, GpsTableMateData.LAT);
		projectionMap.put(GpsTableMateData.LON, GpsTableMateData.LON);
		projectionMap.put(GpsTableMateData.WIFI_SSID, GpsTableMateData.WIFI_SSID);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		//SQLiteDatabase db = dh.getWritableDatabase();
		
		int count;
		switch (uriMatcher.match(uri)) {
		case ITEM:
			count = db.delete(GpsTableMateData.TABLE_NAME, selection, selectionArgs);
			break;
		case ID:
			String rowId = uri.getPathSegments().get(1);
			String where;
			if(!TextUtils.isEmpty(selection)){
				where = GpsTableMateData._ID + "=" + rowId + " AND " + selection;
			}else{
				where = GpsTableMateData._ID + "=" + rowId;
			}
			count = db.delete(GpsTableMateData.TABLE_NAME, where , selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case ITEM:
			return GpsTableMateData.CONTENT_TYPE;
		case ID:
			return GpsTableMateData.CONTENT_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		//SQLiteDatabase db = dh.getWritableDatabase();
		long rowId = db.insert(GpsTableMateData.TABLE_NAME, null, values);
		if(rowId > 0){
			Uri insertedUri = ContentUris.withAppendedId(GpsTableMateData.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(insertedUri, null);
			return insertedUri;
		}else{
			try {
				throw new SQLException("Failed to insert " + uri);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
 
	}

	@Override
	public boolean onCreate() {
		//dh = new FileSqliteHelper(getContext(),G.DB_NAME,null,G.DB_VERSION);
		db = MyDatabase.getDatabaseInstance(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
		switch (uriMatcher.match(uri)) {
		case ITEM:
			sqb.setTables(GpsTableMateData.TABLE_NAME);
			sqb.setProjectionMap(projectionMap);
			break;
		case ID:
			sqb.setTables(GpsTableMateData.TABLE_NAME);
			sqb.setProjectionMap(projectionMap);
			sqb.appendWhere(GpsTableMateData._ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			break;
		}
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)){
			orderBy = GpsTableMateData.DEFAULT_SORT_ORDER;
		}else{
			orderBy = sortOrder;
		}
		
		//SQLiteDatabase db = dh.getWritableDatabase();
		Cursor cursor = sqb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		//SQLiteDatabase db = dh.getWritableDatabase();
		
		int count;
		switch (uriMatcher.match(uri)) {
		case ITEM:
			count = db.update(GpsTableMateData.TABLE_NAME, values, selection, selectionArgs);
			break;
		case ID:
			String rowId = uri.getPathSegments().get(1);
			String where;
			if(!TextUtils.isEmpty(selection)){
				where = GpsTableMateData._ID + "=" + rowId + " AND " + selection;
			}else{
				where = GpsTableMateData._ID + "=" + rowId;
			}
			count = db.update(GpsTableMateData.TABLE_NAME, values,where , selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}


}

package net.zhaidian.db;

import net.zhaidian.provider.FileProviderMateData.TableFileMateData;
import net.zhaidian.provider.GpsoWifiData.GpsTableMateData;
import net.zhaidian.provider.IPsProviderMateData.TableMateData;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class FileSqliteHelper extends SQLiteOpenHelper {

	public FileSqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE " + TableMateData.TABLE_NAME + "(" +
				TableMateData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
				TableMateData.IP + " VARCHAR(15) NOT NULL," +
				TableMateData.NAME + " VARCHAR(50)," +
				TableMateData.STATUS + " INTEGER" +
				");"
				);
		
		db.execSQL("CREATE TABLE " + GpsTableMateData.TABLE_NAME + "(" +
				GpsTableMateData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
				GpsTableMateData.LAT + " VARCHAR(15) NOT NULL," +
				GpsTableMateData.LON + " VARCHAR(15) NOT NULL," +
				GpsTableMateData.WIFI_SSID + " VARCHAR(50)" +
				");"
				);
		
		
		db.execSQL("CREATE TABLE " + TableFileMateData.TABLE_NAME + "(" +
				TableFileMateData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
				TableFileMateData.NAME + " VARCHAR(50) NOT NULL," +
				TableFileMateData.PATH + " VARCHAR(200) NOT NULL," +
				TableFileMateData.TYPE + " INTEGER NOT NULL," +
				TableFileMateData.ADD_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
				TableFileMateData.STATUS + " INTEGER" +
				");"
				);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}

}

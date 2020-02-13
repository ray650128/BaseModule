package tw.com.lig.module_base.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "light_adventure.db";
	private static final int DB_VERSION = 1;
	static final String CACHE_TABLE = "cache";

//	static final String RECENT_TABLE = "recent";
	public MyDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists " + CACHE_TABLE +
				" (url text, params text, response text,token text,other text)";
		db.execSQL(sql);

		/*String sql_recent="create table if not exists " + RECENT_TABLE +
				" (key text, type text)";//type為0代表是资讯，type為1代表是知道
		db.execSQL(sql_recent);*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + CACHE_TABLE;
		db.execSQL(sql);
		/*String sql2="DROP TABLE IF EXISTS " + RECENT_TABLE;
		db.execSQL(sql2);*/
		onCreate(db);
	}
}

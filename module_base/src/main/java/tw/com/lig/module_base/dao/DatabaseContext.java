package tw.com.lig.module_base.dao;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class DatabaseContext extends ContextWrapper {

	public DatabaseContext(Context base) {
		super(base);
		// TODO Auto-generated constructor stub
	}
	 /**
     * 獲得資料库路径，如果不存在，则创建對象對象
     * @param    name
     * @param    mode
     * @param    factory
     */
    @Override
    public File getDatabasePath(String name) {
        //判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if(!sdExist){//如果不存在,
            Log.e("SD卡管理：", "SD卡不存在，請載入SD卡");
            return null;
        } else{//如果存在
            //獲取sd卡路径
            String dbDir=android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            dbDir += "/database";//資料库所在目錄
            String dbPath = dbDir+"/"+name;//資料库路径
            //判断目錄是否存在，不存在则创建該目錄
            File dirFile = new File(dbDir);
            if(!dirFile.exists())
                dirFile.mkdirs();
            
            //資料库文件是否创建成功
            boolean isFileCreateSuccess = false; 
            //判断文件是否存在，不存在则创建該文件
            File dbFile = new File(dbPath);
            if(!dbFile.exists()){
                try {                    
                    isFileCreateSuccess = dbFile.createNewFile();//创建文件
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else     
                isFileCreateSuccess = true;
            
            //返回資料库文件對象
            if(isFileCreateSuccess)
                return dbFile;
            else 
                return null;
        }
    }
 
    /**
     * 重载这個方法，是用来打开SD卡上的資料库的，android 2.3及以下會調用这個方法。
     * 
     * @param    name
     * @param    mode
     * @param    factory
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, 
            CursorFactory factory) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }
    
    /**
     * Android 4.0會調用此方法獲取資料库。
     * 
     * @see ContextWrapper#openOrCreateDatabase(String, int,
     *              CursorFactory,
     *              DatabaseErrorHandler)
     * @param    name
     * @param    mode
     * @param    factory
     * @param     errorHandler
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory,
            DatabaseErrorHandler errorHandler) {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

}

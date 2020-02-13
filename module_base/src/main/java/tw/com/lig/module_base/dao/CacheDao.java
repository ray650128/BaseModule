package tw.com.lig.module_base.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import tw.com.lig.module_base.BuildConfig;
import tw.com.lig.module_base.data.SPConstant;
import tw.com.lig.module_base.utils.MD5;
import tw.com.lig.module_base.utils.SPutils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class CacheDao {
    private static volatile CacheDao cacheDao;

    private MyDBHelper helper;
    private SQLiteDatabase database;

    private CacheDao(Context context){
        if(BuildConfig.DEBUG){
            helper = new MyDBHelper(new DatabaseContext(context.getApplicationContext()));
        }else{
            helper = new MyDBHelper(context.getApplicationContext());
        }
//        helper = new MyDBHelper(context.getApplicationContext());


    }
    public static CacheDao getInstance(Context context) {
        if (cacheDao == null) {
            synchronized (CacheDao.class) {
                if (cacheDao == null) {
                    cacheDao = new CacheDao(context);
                }
            }
        }
        return cacheDao;
    }
    //查
    public String queryResponse(String urlKey, String params) throws UnsupportedEncodingException {
        database = helper.getWritableDatabase();
        String params_decode = URLDecoder.decode(params, "utf-8");
//        String token= getToken();//正式環境需要對token進行md5加密
        Cursor cursor = database.rawQuery("select response from cache where url =? and params =? and token = ?", new String[]{urlKey, params_decode,getToken()});
        while(cursor.moveToNext()){

            String response = cursor.getString(cursor.getColumnIndex("response"));//所有的打標記的點的進度值的集合
           return response;
        }

        return null;
    }
    //查get
    public String queryGetResponse(String urlKey) throws UnsupportedEncodingException {
        database = helper.getWritableDatabase();
//        String decode = URLDecoder.decode(params, "utf-8");
//        String params_decode = URLDecoder.decode(params, "utf-8");
//        String token= getToken();//正式環境需要對token進行md5加密

        Cursor cursor = database.rawQuery("select response from cache where url =? and token = ?", new String[]{urlKey,getToken()});
        while(cursor.moveToNext()){

            String response = cursor.getString(cursor.getColumnIndex("response"));//所有的打標記的點的進度值的集合
            return response;
        }

        return null;
    }
    //增
    public void insertResponse(String urlKey, String params, String response) throws UnsupportedEncodingException {
        database = helper.getWritableDatabase();
//        String token= getToken();//正式環境需要對token進行md5加密

        if(!isExist(urlKey,params)){
                String decode = URLDecoder.decode(params, "utf-8");
                database.execSQL("insert into cache  (url,params,response,token) values (?,?,?,?)",new Object[]{urlKey, decode, response,getToken()});
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
        }else{

//            String sql_reset_sequence="update cache set response =? where url =? and params =?";

            String decode = URLDecoder.decode(params, "utf-8");


//            database.execSQL("update cache set response ='"+response+"' where url ='"+urlKey+"' and params ='"+decode+"'");
            database.execSQL("update cache set response = ? where url = ? and params = ? and token = ?",new String []{response,urlKey,decode,getToken()});

        }
//        database.close();
    }

    private String getToken() {
        ////正式環境需要對token進行md5加密
        return BuildConfig.DEBUG ? SPutils.getString(SPConstant.KEY_TOKEN) : MD5.md5Encode(SPutils.getString(SPConstant.KEY_TOKEN));
    }


    //儲存get請求的網路響應
    public void insertGetResponse(String urlKey,  String value) throws UnsupportedEncodingException {
        database = helper.getWritableDatabase();
//        String token= getToken();//正式環境需要對token進行md5加密

        if(!isExist(urlKey)){
//            String decode = URLDecoder.decode(params, "utf-8");
            database.execSQL("insert into cache  (url,response,token) values (?,?,?)",new Object[]{urlKey, value,getToken()});
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
        }else{







//            String sql_reset_sequence="update cache set response =? where url =? and params =?";

//            String decode = URLDecoder.decode(params, "utf-8");


//            database.execSQL("update cache set response ='"+value+"' where url ='"+urlKey+"' and params ='"+decode+"'");
            database.execSQL("update cache set response = ? where url = ? and token = ?",new String []{value,urlKey,getToken()});

        }
//        database.close();
    }
    public boolean isExist(String urlKey, String params) throws UnsupportedEncodingException {
        database = helper.getWritableDatabase();
//        String token= getToken();//正式環境需要對token進行md5加密

        String decode = URLDecoder.decode(params, "utf-8");
        Cursor cursor = database.rawQuery("select url from cache where url =? and params= ? and token = ?", new String[]{urlKey,decode,getToken()});

        while(cursor.moveToNext()){
            return true;
        }
        return false;
    };
    public boolean isExist(String urlKey) throws UnsupportedEncodingException {
//        String token= getToken();//正式環境需要對token進行md5加密

        database = helper.getWritableDatabase();
//        String decode = URLDecoder.decode(params, "utf-8");
        Cursor cursor = database.rawQuery("select url from cache where url =? and token = ?", new String[]{urlKey,getToken()});

        while(cursor.moveToNext()){
            return true;
        }
        return false;
    };
    //改
    public void updateResponse(String urlKey, String params, String value) {
    }
    //刪
    public void deleteResponse(String urlKey, String params) {
    }
    //清除所有資料
    public void clearAll(){
        database = helper.getWritableDatabase();
        database.execSQL("delete from cache");
//        database.execSQL("update sqlite_sequence set seq = 0 where name = 'cache'");





    }
}

package tw.com.lig.module_base.base;


import tw.com.lig.module_base.utils.GsonUtils;
import tw.com.lig.module_base.utils.TDevice;

import java.util.HashMap;

//import com.whyhow.global.ShareData;

//import com.alibaba.android.arouter.facade.annotation.Param;

public class ParamsMap {
//    private HashMap mMap;
//    public ParamsMap(Builder builder){
//        mMap=builder.map;
//
//    }

    public static class Builder {
        private boolean isEncode;
        private  HashMap map;
        public Builder() {
            map=new HashMap();
            isEncode=false;
        }
        public Builder setParam(String key,Object value){
            map.put(key,value);
            return this;
        }
//        public String getParam(String key){
//
//            return (String)map.get(key);
//        }

        public String build(){
//            return GsonUtils.toJson(map);


          /*  if(isEncode){
                return ParamUtil.generalSignParam(map);
            }else{
//                map.put("appId", ParamUtil.APP_ID);
//                map.put("appChannel", ParamUtil.APP_CHANNEL);
//                map.put("plat",ParamUtil.PLAT);
                map.put("version", TDevice.getVersionName());
                String token = (String) SPutils.get(SPConstant.KEY_TOKEN, "");
                if(!TextUtils.isEmpty(token)){
                    map.put("token",token);
                }

                return GsonUtils.toJson(map);
            }*/


            map.put("version", TDevice.getVersionCode()+"");
            map.put("platformId","1");
            map.put("appChannel","41");
            map.put("appId","41");

            map.put("device",TDevice.getIMEI());
            String token;

           /* if (BuildConfig.DEBUG){
                token=GlobalConstant.DEBUG_CPS_TOKEN; //暫時寫死
            }else{
                token = (String) SPutils.get(ShareData.token, "");
            }*/

//            token = (String) SPutils.get(ShareData.token, "");
//            Log.e("token",token);
   //         token = "AUTH_TOKEN_ef9aeea8-75fd-4d17-93c7-39ee44d88e3c";
//            String token=GlobalConstant.DEBUG_CPS_TOKEN; //暫時寫死
//            String token = (String) SPutils.get(ShareData.token, "");
//            if(!TextUtils.isEmpty(token)){
//                map.put("token",token);
//            }

            return GsonUtils.toJson(map);

        }
        public Builder encode(){
            isEncode=true;
            return this;
        }
        public HashMap getMap(){
            return map;
        }

    }

}

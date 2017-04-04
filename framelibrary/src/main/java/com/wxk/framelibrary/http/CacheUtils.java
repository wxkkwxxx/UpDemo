package com.wxk.framelibrary.http;

import com.wxk.baselibrary.MD5Util;
import com.wxk.baselibrary.log.LogUtils;
import com.wxk.framelibrary.db.DaoSupportFactory;
import com.wxk.framelibrary.db.IDaoSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/4/4
 */

public class CacheUtils {

    private static final String TAG = "CACHEUTILS";

    public static String getCacheResultJson(String url){

        IDaoSupport<CacheData> daoSupport = DaoSupportFactory.getFactory().getDao(CacheData.class);
        List<CacheData> cacheDatas = daoSupport.querySupport()
                .selection("mUrlKey = ?")
                .selectionArgs(MD5Util.string2MD5(url))
                .query();
        if(!cacheDatas.isEmpty()){

            CacheData cacheData = cacheDatas.get(0);
            String resultJson = cacheData.getResultJson();

            return resultJson;
        }
        return null;
    }

    public static long cacheData(String finalUrl, String resultJson){

        IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactory().
                getDao(CacheData.class);
        dataDaoSupport.delete("mUrlKey=?", MD5Util.string2MD5(finalUrl));
        long number = dataDaoSupport.insert(new CacheData(MD5Util.string2MD5(finalUrl), resultJson));
        LogUtils.e(TAG, " ---> " + number);
        return number;
    }
}

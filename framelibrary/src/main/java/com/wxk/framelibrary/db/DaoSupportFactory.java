package com.wxk.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/4/3
 */

public class DaoSupportFactory {

    private static DaoSupportFactory mFactory;
    private static SQLiteDatabase mSQLiteDatabase;

    //此处最好一直持有外部数据库的引用
    private DaoSupportFactory(){

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            File dbRoot = new File(Environment.getExternalStorageDirectory() + File.separator
                    + "UpDemo" + File.separator + "database");
            if(!dbRoot.exists()){
                dbRoot.mkdirs();
            }

            File dbFile = new File(dbRoot, "upDemo.db");
            mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        }
    }

    public static DaoSupportFactory getFactory(){

        if(mFactory == null){
            synchronized (DaoSupportFactory.class){
                if(mFactory == null){
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    public <T>IDaoSupport getDao(Class<T> clazz){

        IDaoSupport daoSupport = new DaoSupport();
        daoSupport.init(mSQLiteDatabase, clazz);
        return daoSupport;
    }
}

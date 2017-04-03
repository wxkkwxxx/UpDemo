package com.wxk.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import com.wxk.baselibrary.log.LogUtils;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/4/3
 */

public class DaoSupport<T> implements IDaoSupport<T>{

    private static final String TAG = "DaoSupport";

    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> mClazz;
    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz){
         this.mSQLiteDatabase = sqLiteDatabase;
         this.mClazz = clazz;

        /*"create table if not exists Person ("
                + "id integer primary key autoincrement, "
                + "name text, "
                + "age integer, "
                + "flag boolean)";*/

        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists ")
                .append(DaoUtils.getTableName(mClazz))
                .append("(id integer primary key autoincrement, ");

        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {

            field.setAccessible(true);
            String type = field.getType().getSimpleName(); //int string boolean
            String name = field.getName();

            sb.append(name).append(DaoUtils.getColumnType(type)).append(", ");
        }

        sb.replace(sb.length() - 2, sb.length(), ")");

        String createTable = sb.toString();
        LogUtils.e(TAG, "表语句--->" + createTable);

        mSQLiteDatabase.execSQL(createTable);
    }


    //插入数据库
    @Override
    public int insert(T t) {
        return 0;
    }
}

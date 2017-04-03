package com.wxk.framelibrary.db;


import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/4/3
 */

public interface IDaoSupport<T> {

    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);

    //插入数据
    int insert(T t);
}

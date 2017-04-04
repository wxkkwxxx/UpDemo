package com.wxk.framelibrary.db;


import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Administrator on 2017/4/3
 */

public interface IDaoSupport<T> {

    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);

    //插入数据
    long insert(T t);

    //批量插入
    void inset(List<T> datas);

    //查询需要些一个support
    QuerySupport<T> querySupport();

    //删除
    int delete(String whereClause, String... whereArgs);

    //更新
    int update(T obj, String whereClause, String... whereArgs);
}

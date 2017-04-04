package com.wxk.framelibrary.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;

import com.wxk.baselibrary.log.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/3
 */

public class DaoSupport<T> implements IDaoSupport<T>{

    private static final String TAG = "DaoSupport<T>";

    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> mClazz;

    private static final Object[] mPutMethodArgs = new Object[2];
    private static final Map<String, Method> mPutMethods
            = new ArrayMap<>();

    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz){
         this.mSQLiteDatabase = sqLiteDatabase;
         this.mClazz = clazz;

        /*"create table if not exists Person("
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
    public long insert(T t) {

        ContentValues values = contentValueByObj(t);
        return mSQLiteDatabase.insert(DaoUtils.getTableName(mClazz), null, values);
    }

    @Override
    public void insert(List<T> datas) {
        mSQLiteDatabase.beginTransaction();
        for (T data : datas) {
            insert(data);
        }
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    private QuerySupport<T> mQuerySupport;

    @Override
    public QuerySupport<T> querySupport() {
        if(mQuerySupport == null){
            mQuerySupport = new QuerySupport<>(mSQLiteDatabase, mClazz);
        }
        return mQuerySupport;
    }

    @Override
    public int delete(String whereClause, String... whereArgs) {

//        示例
//        ContentValues cv = new ContentValues();
//        String[] args = {"wxk", String.valueOf(12)};
//        delete("user", "name = ? and age= ?", args);

        return mSQLiteDatabase.delete(DaoUtils.getTableName(mClazz), whereClause, whereArgs);
    }

    @Override
    public int update(T t, String whereClause, String... whereArgs) {

//        示例
//        ContentValues cv = new ContentValues();
//        String[] args = {"wxk", String.valueOf(12)};
//        delete("user", "name = ? and age= ?", args);

        ContentValues values = contentValueByObj(t);
        return mSQLiteDatabase.update(DaoUtils.getTableName(mClazz), values, whereClause, whereArgs);
    }

    private ContentValues contentValueByObj(T t) {

        ContentValues values = new ContentValues();
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {

            try {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(t);

                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;

//                //这里通过反射执行方法,因为不想通过判断来确定value类型，其他第三方也是通过反射
////                if(value instanceof Integer){
////
////                }else if(value instanceof String){
////
////                }
//                //这里获取方法第一个参数方法名,后面的传参数的类型,因为一个类中重载的方法可以根据根据参数个数和参数类型来确定
//                Method putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
//                //这个方法不解释了
//                putMethod.invoke(values, key, value);

                //缓存方法,可以不用每次都反射获取方法
                String fieldTypeName = field.getType().getName();

                Method putMethod = mPutMethods.get(fieldTypeName);
                if(putMethod == null){

                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    mPutMethods.put(fieldTypeName, putMethod);
                }

                putMethod.invoke(values, mPutMethodArgs);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        LogUtils.e(TAG, "values--->" + values);
        return values;
    }
}

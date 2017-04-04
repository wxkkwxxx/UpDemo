package com.wxk.framelibrary.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/4
 * 查询api参数:http://blog.csdn.net/scorplopan/article/details/6303559
 * http://blog.csdn.net/jing110fei/article/details/38491289
 */
public class QuerySupport<T> {

    //查询的列
    private String[] mQueryColumns;
    //查询的条件
    private String mQuerySelection;
    //查询的参数
    private String[] mQuerySelectionArgs;
    //查询的分组
    private String mQueryGroupBy;
    //查询对结果集进行过滤
    private String mQueryHaving;
    //查询排序
    private String mQueryOrderBy;
    //分页查询
    private String mQueryLimit;

    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> mClazz;

    public QuerySupport(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        this.mClazz = clazz;
        this.mSQLiteDatabase = sqLiteDatabase;
//        mSQLiteDatabase.query(DaoUtils.getTableName(mClazz), null, null, null, null, null, null, null);
    }

    public QuerySupport columns(String... columns) {
        this.mQueryColumns = columns;
        return this;
    }

    public QuerySupport selection(String selection) {
        this.mQuerySelection = selection;
        return this;
    }

    public QuerySupport selectionArgs(String... selectionArgs) {
        this.mQuerySelectionArgs = selectionArgs;
        return this;
    }

    public QuerySupport groupBy(String groupBy) {
        this.mQueryGroupBy = groupBy;
        return this;
    }

    public QuerySupport having(String having) {
        this.mQueryHaving = having;
        return this;
    }

    public QuerySupport orderBy(String orderBy) {
        this.mQueryOrderBy = orderBy;
        return this;
    }

    public QuerySupport limit(String limit) {
        this.mQueryLimit = limit;
        return this;
    }

    //条件查询
    public List<T> query() {
        Cursor cursor = mSQLiteDatabase.query(DaoUtils.getTableName(mClazz), mQueryColumns, mQuerySelection,
                mQuerySelectionArgs, mQueryGroupBy, mQueryHaving, mQueryOrderBy, mQueryLimit);
        clearQueryParams();
        return cursorToList(cursor);
    }

    //查询所有
    public List<T> queryAll() {
        Cursor cursor = mSQLiteDatabase.query(DaoUtils.getTableName(mClazz), null, null, null, null, null, null);
        return cursorToList(cursor);
    }

    /**
     * 清空参数
     */
    private void clearQueryParams() {
        mQueryColumns = null;
        mQuerySelection = null;
        mQuerySelectionArgs = null;
        mQueryGroupBy = null;
        mQueryHaving = null;
        mQueryOrderBy = null;
        mQueryLimit = null;
    }

    /**
     * 将Cursor封装成查找对象
     */
    private List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<>();
        if(cursor != null && cursor.moveToFirst()){
            do {
                try {

//                    int age = cursor.getInt(cursor.getColumnIndex("age"));
//                    String names = cursor.getString(cursor.getColumnIndex("name"));
                    T instance = mClazz.newInstance();
                    Field[] fields = mClazz.getDeclaredFields();
                    for (Field field : fields) {

                        field.setAccessible(true);
                        String name = field.getName();
                        //获取角标在第几列
                        int index = cursor.getColumnIndex(name);
                        if(index == -1){
                            continue;
                        }

//                        cursor.getInt(index);
//                        cursor.getString(index);
//                        cursor.getLong(index);
//                        cursor.getDouble(index);
//                        cursor.getFloat(index);

                        //通过反射获取方法
                        // 通过反射获取 游标的方法
                        Method cursorMethod = cursorMethod(field.getType());
                        if(cursorMethod != null){

                            Object value = cursorMethod.invoke(cursor, index);
                            if(value == null){
                                continue;
                            }
                            // 处理一些特殊的部分
                            if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                                if ("0".equals(String.valueOf(value))) {
                                    value = false;
                                } else if ("1".equals(String.valueOf(value))) {
                                    value = true;
                                }
                            } else if (field.getType() == char.class || field.getType() == Character.class) {
                                value = ((String) value).charAt(0);
                            } else if (field.getType() == Date.class) {
                                long date = (Long) value;
                                if (date <= 0) {
                                    value = null;
                                } else {
                                    value = new Date(date);
                                }
                            }
                            field.set(instance, value);
                        }
                    }
                    list.add(instance);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    //通过
    private Method cursorMethod(Class<?> type) throws Exception{
        String methodName = getColumnMethodName(type);
        Method method = Cursor.class.getMethod(methodName, int.class);
        return method;
    }

    private String getColumnMethodName(Class<?> fieldType) {
        String typeName;
        if (fieldType.isPrimitive()) {
            typeName = DaoUtils.capitalize(fieldType.getName());
        } else {
            typeName = fieldType.getSimpleName();
        }
        String methodName = "get" + typeName;
        if("getBoolean".equals(methodName)){
            methodName = "getInt";
        }else if("getChar".equals(methodName) || "getCharacter".equals(methodName)){
            methodName = "getString";
        }else if ("getDate".equals(methodName)) {
            methodName = "getLong";
        } else if ("getInteger".equals(methodName)) {
            methodName = "getInt";
        }
        return methodName;
    }
}

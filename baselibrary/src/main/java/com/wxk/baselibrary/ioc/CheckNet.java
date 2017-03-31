package com.wxk.baselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/3/31
 * 判断是否有网络
 */
@Retention(RetentionPolicy.RUNTIME) //何时生效
@Target(ElementType.METHOD) //Annotation的位置
public @interface CheckNet {
}

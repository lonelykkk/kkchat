package com.kkk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lonelykkk
 * @email 2765314967@qq.com
 * @date 2024/11/15 10:30
 * @Version V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalInterceptor {

    boolean checkLogin() default true;

    boolean checkAdmin() default false;


}

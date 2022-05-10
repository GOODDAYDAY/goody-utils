package com.goody.utils.xueya.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * component scan path and init bean
 *
 * @author Goody
 * @version 1.0, 2022/5/9
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {
    /** path array to scan */
    String[] path();
}

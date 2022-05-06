package com.goody.utils.qianliang.example;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link AddVariableProcessor} annotation
 *
 * @author Goody
 * @version 1.0, 2022/5/5
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface AddVariable {
    String name();

    Class<? extends Object> clazz();
}

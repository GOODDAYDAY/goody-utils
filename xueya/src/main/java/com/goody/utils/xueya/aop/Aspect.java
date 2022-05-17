package com.goody.utils.xueya.aop;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * aspect for
 *
 * @author Goody
 * @version 1.0, 2022/5/16
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Aspect {
    /** cut annotation */
    Class<? extends Annotation> cut();
}

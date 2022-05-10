package com.goody.utils.xueya.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark the class is a component
 *
 * @author Goody
 * @version 1.0, 2022/5/9
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
    /** name */
    String value();

    /** scope default singletion */
    Scope scope() default Scope.SINGLETON;
}

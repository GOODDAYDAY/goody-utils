package com.goody.utils.xueya.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation to mark the bean to wire
 *
 * @author Goody
 * @version 1.0, 2022/5/10
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Autowired {
    /** value name, when is "" or default, value will be field's name */
    String value() default "";
}

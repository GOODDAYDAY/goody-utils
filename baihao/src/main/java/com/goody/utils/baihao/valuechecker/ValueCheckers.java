package com.goody.utils.baihao.valuechecker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * value checkers
 *
 * @author Goody
 * @version 1.0, 2022/5/5
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValueCheckers {

    /** value checkers araay */
    ValueChecker[] checkers();

    /**
     * valueChecker limiter
     */
    @interface ValueChecker {
        /** the implement service type - bean name is uncapitalize class name */
        Class<? extends IValueCheckerHandler> handler();

        /** spel - invoke method name - method */
        String method() default "verify";

        /** spel - invoke paras - keys */
        String[] keys() default "";
    }
}

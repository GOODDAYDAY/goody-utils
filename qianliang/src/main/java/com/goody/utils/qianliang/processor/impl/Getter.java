package com.goody.utils.qianliang.processor.impl;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link ZMethodGetterProcessor} annotation
 *
 * @author Goody
 * @version 1.0, 2022/5/4
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface Getter {
}

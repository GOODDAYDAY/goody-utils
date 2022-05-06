package com.goody.utils.baihao.valuechecker;


/**
 * value checker handler. Aspect will get the bean map of IValueCheckerHandler.
 * <p>
 * <ol>
 *     <li>when using {@link MockBean}, object should verify the implement class.</li>
 *     <li>when implement, must not specify the bean name.Aspect get the right handler with default name</li>
 * </ol>
 *
 * @author Goody
 * @version 1.0, 2022/5/6
 * @since 1.0.0
 */
public interface IValueCheckerHandler {
}

package com.goody.utils.xueya.context;

/**
 * context interface
 *
 * <ol>
 *     <li>doScan for all file scanned</li>
 *     <li>handle for all bean to handle such as proxy</li>
 * </ol>
 *
 * @author Goody
 * @version 1.0, 2022/5/16
 * @since 1.0.0
 */
public interface Context {

    /**
     * scan all `.class`
     *
     * @param beanName bean name
     * @param clazz    bean class
     */
    void doScan(String beanName, Class<?> clazz);

    /**
     * handle bean object after scan
     *
     * @param object bean object
     * @return object
     */
    Object handle(Object object);
}

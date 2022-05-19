package com.goody.utils.xueya.aop;

import com.goody.utils.xueya.bean.XueyaApplicationContext;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * context for aop
 *
 * @author Goody
 * @version 1.0, 2022/5/16
 * @since 1.0.0
 */
public class AopContext {
    private static final Logger log = LoggerFactory.getLogger(AopContext.class);
    private final Map<Class<? extends Annotation>, Node> aopObject;

    public AopContext() {
        this.aopObject = new HashMap<>();
    }

    /**
     * scan all `.class`
     *
     * @param beanName bean name
     * @param clazz    bean class
     */
    public void doScan(String beanName, Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Aspect.class)) {
            return;
        }
        final Class<? extends Annotation> aop = clazz.getDeclaredAnnotation(Aspect.class).cut();
        Method before = null, after = null;
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                before = method;
            }
            if (method.isAnnotationPresent(After.class)) {
                after = method;
            }
        }
        this.aopObject.put(aop, new Node(beanName, before, after));
    }

    /**
     * charge if the object is need proxy
     *
     * @param object object
     * @return true -> need ; false -> not
     */
    public boolean isNeedProxy(Object object) {
        // not contain means is needs not proxy
        // object.getClass().getAnnotations() will generate class com.sun.proxy.$Proxy3
        // is makes this.aopObject.containsKey(anno.getClass()) = false.
        // So search by map
        for (Map.Entry<Class<? extends Annotation>, Node> entry : this.aopObject.entrySet()) {
            if (object.getClass().isAnnotationPresent(entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    /**
     * do proxy logic, charge if the object needs to be proxy
     *
     * @param object bean object
     * @return object or proxy object
     */
    public Object proxy(Object object) {
        // not contain means is needs not proxy
        // object.getClass().getAnnotations() will generate class com.sun.proxy.$Proxy3
        // is makes this.aopObject.containsKey(anno.getClass()) = false.
        // So search by map
        for (Map.Entry<Class<? extends Annotation>, Node> entry : this.aopObject.entrySet()) {
            if (object.getClass().isAnnotationPresent(entry.getKey())) {
                final Node node = entry.getValue();
                object = this.proxy(object, node);
            }
        }
        return object;
    }

    /**
     * create proxy object
     *
     * @param object bean object
     * @param node   proxy node
     * @return proxy object
     */
    private Object proxy(Object object, Node node) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(object.getClass());
        enhancer.setCallback((MethodInterceptor) (proxy, method, objects, methodProxy) -> {
            node.invokeBefore();
            final Object result = methodProxy.invoke(object, objects);
            node.invokeAfter();
            return result;
        });
        return enhancer.create();
    }

    private static class Node {
        private String beanName;
        private Method before;
        private Method after;

        public Node(String beanName, Method before, Method after) {
            this.beanName = beanName;
            this.before = before;
            this.after = after;
        }

        private void invokeBefore() {
            if (null != this.before) {
                try {
                    this.before.invoke(XueyaApplicationContext.getBean(beanName));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        private void invokeAfter() {
            if (null != this.after) {
                try {
                    this.after.invoke(XueyaApplicationContext.getBean(beanName));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

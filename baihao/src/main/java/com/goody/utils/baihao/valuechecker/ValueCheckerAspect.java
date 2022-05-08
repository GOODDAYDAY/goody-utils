package com.goody.utils.baihao.valuechecker;

import com.goody.utils.baihao.util.CommonUtils;
import com.goody.utils.baihao.util.SeplUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ValueCheckers} aspect
 *
 * @author Goody
 * @version 1.0, 2022/5/5
 * @since 1.0.0
 */
@Aspect
@Component
public class ValueCheckerAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValueCheckerAspect.class);
    private static final String OBJECT_METHOD_FORMAT = "%s#%s#%s";
    private static final ConcurrentHashMap<String, Method> OBJECT_METHOD_MAP = new ConcurrentHashMap<>();
    private final Map<String, IValueCheckerHandler> handlerMap;

    @Autowired
    public ValueCheckerAspect(Map<String, IValueCheckerHandler> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /** cut point */
    @Pointcut("@annotation(com.goody.utils.baihao.valuechecker.ValueCheckers)")
    private void handleValueCheckerPoint() {
    }

    /**
     * main method. Traverse the checkers to check point
     *
     * @param point         point
     * @param valueCheckers checkers
     * @return meta object
     * @throws Throwable error
     */
    @Around("handleValueCheckerPoint() && @annotation(valueCheckers)")
    public Object around(ProceedingJoinPoint point, ValueCheckers valueCheckers) throws Throwable {
        for (ValueCheckers.ValueChecker checker : valueCheckers.checkers()) {
            valueCheck(checker, point);
        }
        return point.proceed();
    }

    private void valueCheck(ValueCheckers.ValueChecker checker, ProceedingJoinPoint point) throws InvocationTargetException, IllegalAccessException {
        try {
            // get the handler with class name
            Object instance = getBean(checker.handler());
            Object[] params = SeplUtil.getValue(point, checker.keys());
            methodInvoke(instance, checker.method(), params);
        } catch (UndeclaredThrowableException | InvocationTargetException | IllegalAccessException e) {
            // catch the exception which has ValueIllegalException as cause.
            // otherwise throw exception as exception display in upside.
            if (e.getCause() instanceof ValueIllegalException) {
                throw (ValueIllegalException) e.getCause();
            }
            LOGGER.error("method invoke exception", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("method invoke exception", e);
            throw e;
        }

    }

    /**
     * get the {@link Method} by reflect. choose the correct {@link Method} to invoke
     *
     * @param instance instance
     * @param method   method name
     * @param paras    paras of method
     * @throws InvocationTargetException invocation target exception
     * @throws IllegalAccessException    illegal access exception
     * @throws ValueIllegalException     custom exception
     */
    private void methodInvoke(Object instance, String method, Object[] paras) throws InvocationTargetException, IllegalAccessException {
        final Class<?> instanceClass = instance.getClass();
        // generate the special name. Only the first time to invoke needs traverse
        final String parasName = objectTypeName(paras);
        final String objectMethodName = String.format(OBJECT_METHOD_FORMAT, instanceClass.getSimpleName(), method, parasName);
        if (OBJECT_METHOD_MAP.containsKey(objectMethodName)) {
            final Method pointMethod = OBJECT_METHOD_MAP.get(objectMethodName);
            LOGGER.debug("choose exist method {}", objectMethodName);
            pointMethod.invoke(instance, paras);
            return;
        }

        for (Method subMethod : instanceClass.getMethods()) {
            // skip for wrong method name
            if (!subMethod.getName().equals(method)) {
                continue;
            }
            // skip for wrong length
            if (subMethod.getParameterTypes().length != paras.length) {
                continue;
            }
            // skip for wrong type order
            final String typeName = methodTypeName(subMethod.getParameterTypes());
            if (!parasName.equals(typeName)) {
                continue;
            }
            // objectMethodName is a special string for method. Put in map for getting method faster.
            OBJECT_METHOD_MAP.put(objectMethodName, subMethod);
            LOGGER.debug("choose first method {}", objectMethodName);
            subMethod.invoke(instance, paras);
            return;
        }
        throw new ValueIllegalException("method input error");
    }

    /**
     * generate the name by class and get the implement instance
     *
     * @param clazz valueChecker class
     * @return value checker handler implement
     */
    private IValueCheckerHandler getBean(Class<?> clazz) {
        return handlerMap.get(CommonUtils.uncapitalize(clazz.getSimpleName()));
    }

    private String objectTypeName(Object[] objects) {
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {
            sb.append("/");
            sb.append(object.getClass().getName());
        }
        return sb.toString();
    }

    private String methodTypeName(Class[] clazzs) {
        StringBuilder sb = new StringBuilder();
        for (Class clazz : clazzs) {
            sb.append("/");
            sb.append(clazz.getName());
        }
        return sb.toString();
    }
}

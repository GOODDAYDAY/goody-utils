package com.goody.utils.baihao.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * spring spel utils
 *
 * @author Goody
 * @version 1.0, 2022/5/6
 * @since 1.0.0
 */
public final class SeplUtil {
    private static final ExpressionParser SPEL_PARSER = new SpelExpressionParser();

    private SeplUtil() {
    }

    /**
     * 获取spel定义的参数值
     *
     * @param point 切点
     * @param keys  keys
     * @return 参数值
     */
    public static Object[] getValue(ProceedingJoinPoint point, String[] keys) {
        // 当不需要传入字段时，直接返回空对象
        if (CommonUtils.isArrayEmpty(keys) || CommonUtils.isStringBlank(keys[0])) {
            return new Object[0];
        }
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String[] params = methodSignature.getParameterNames();
        // 此时需要解析但是无参数则必定无法解析
        if (CommonUtils.isArrayEmpty(params)) {
            return null;
        }
        Object[] args = point.getArgs();
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        Object[] values = new Object[keys.length];
        for (int i = 0; i < keys.length; i++) {
            Expression expression = SPEL_PARSER.parseExpression(keys[i]);
            values[i] = expression.getValue(context, Object.class);
        }
        return values;
    }
}

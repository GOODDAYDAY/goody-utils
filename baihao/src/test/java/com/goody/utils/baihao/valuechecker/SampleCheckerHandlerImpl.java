package com.goody.utils.baihao.valuechecker;

import org.springframework.stereotype.Service;

/**
 * example {@link IValueCheckerHandler}
 *
 * @author Goody
 * @version 1.0, 2021/4/19
 * @since 1.0.0
 */
@Service
public class SampleCheckerHandlerImpl implements IValueCheckerHandler {

    public static final Long ERROR_ID = 1L;
    public static final String ERROR_NAME = "errorName";
    public static final Long CORRECT_ID = 2L;
    public static final String CORRECT_NAME = "correctName";

    public void verify(Long id, String name) {
        if (!CORRECT_ID.equals(id) || !CORRECT_NAME.equals(name)) {
            throw new ValueIllegalException("error");
        }
    }

    public void verify(Long id) {
        if (!CORRECT_ID.equals(id)) {
            throw new ValueIllegalException("error");
        }
    }

    public void verify(String name) {
        if (!CORRECT_NAME.equals(name)) {
            throw new ValueIllegalException("error");
        }
    }

    public void verify() {
    }

    public void verifyPutThreadValue(String name) {
        // set default value with method get
        ValueCheckerReentrantThreadLocal.getOrDefault(String.class, name);

        // verify value
        if (!ValueCheckerReentrantThreadLocal.getOrDefault(String.class, "").equals(name)) {
            throw new ValueIllegalException("error");
        }
    }

    public void verifyGetRightThreadValue(String name) {
        if (!ValueCheckerReentrantThreadLocal.getOrDefault(String.class, name).equals(name)) {
            throw new ValueIllegalException("error");
        }
    }

    public void verifyGetWrongThreadValue(String name) {
        if (!ValueCheckerReentrantThreadLocal.getOrDefault(String.class, "").equals(name)) {
            throw new ValueIllegalException("error");
        }
    }

    public void verifyError() {
        throw new ValueIllegalException("error");
    }
}

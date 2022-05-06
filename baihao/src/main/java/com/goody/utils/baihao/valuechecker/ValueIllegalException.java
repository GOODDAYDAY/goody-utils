package com.goody.utils.baihao.valuechecker;

/**
 * value illegal exception,no suppression and strace
 *
 * @author Goody
 * @version 1.0, 2022/5/6
 * @since 1.0.0
 */
public class ValueIllegalException extends RuntimeException {

    public ValueIllegalException(String message) {
        super(message, null, true, false);
    }
}

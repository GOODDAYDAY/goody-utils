package com.goody.utils.longjing.akka.base;

/**
 * {@link java.util.function.Consumer} without input
 *
 * @author Goody
 * @version 1.0, 2022/8/31 15:12
 * @since 1.0.0
 */
@FunctionalInterface
public interface RunFunction {

    /**
     * Performs this operation on the given argument.
     */
    void run();
}

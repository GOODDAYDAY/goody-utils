package com.goody.utils.longjing.akka.util;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import com.goody.utils.longjing.akka.base.RunFunction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Akka util
 *
 * @author Goody
 * @version 1.0, 2022/8/31 15:37
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AkkaUtil {

    /**
     * same behavior
     *
     * @param runFunction biz operate
     * @param <T>         command
     * @return same behavior
     */
    public static <T> Behavior<T> same(RunFunction runFunction) {
        runFunction.run();
        return Behaviors.same();
    }
}

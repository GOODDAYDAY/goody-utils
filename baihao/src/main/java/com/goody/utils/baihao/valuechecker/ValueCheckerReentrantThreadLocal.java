package com.goody.utils.baihao.valuechecker;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * ThreadLocal - util for save obj to context
 * <p/><pre>{@code
 * // code in one IValueCheckerHandler#verify
 * // data will be set into ThreadLocal automatically
 * ValueCheckerThreadLocal.get(Integer.class, () -> new Integer(1));
 * // ValueCheckerAspect will clear ThreadLocal automatically
 * // com.goody.utils.baihao.valuechecker.ValueCheckerAspect#around(org.aspectj.lang.ProceedingJoinPoint, com.goody.utils.baihao.valuechecker.ValueCheckers)
 * }</pre>
 * <p/>
 * The ThreadLocal value will exist from the value generated to the {@link ValueCheckers} method ended.
 * <p/>
 * When thread changing, the ThreadLocal value will lose in other thread. Additionally, the business within the thread is unrelated to the original thread.
 * <p/>
 * To ensure reentrant functionality, the clear method and init method need to maintain an additional {@code `counter`} variable.
 *
 * @author Goody
 * @version 1.0, 2023/12/25
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValueCheckerReentrantThreadLocal {

    /** thread-local for all obj generated in {@link IValueCheckerHandler} */
    private static final ThreadLocal<ConcurrentHashMap<Class<?>, Object>> VALUE_CHECKER_THREAD_LOCAL = new ThreadLocal<>();
    /** the counter to provider value will not be clear in sub ValueChecker */
    private static final ThreadLocal<AtomicInteger> VALUE_CHECKER_THREAD_LOCAL_COUNTER = new ThreadLocal<>();

    /**
     * put obj into ThreadLocal & init automatically
     *
     * @param obj obj
     */
    public static void put(Object obj) {
        if (null == obj) {
            throw new IllegalArgumentException();
        }
        if (null == VALUE_CHECKER_THREAD_LOCAL.get()) {
            // init ThreadLocal will not be called here
            throw new IllegalStateException();
        }
        VALUE_CHECKER_THREAD_LOCAL.get().put(obj.getClass(), obj);
    }

    /**
     * same as {@link #get(Class, Supplier)}
     *
     * @param clazz        class of T
     * @param defaultValue default value
     * @param <T>          value class needed
     * @return default value or the value generated before
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> T getOrDefault(@NonNull Class<T> clazz, @NonNull T defaultValue) {
        return get(clazz, () -> defaultValue);
    }

    /**
     * everytime calls method #get should pass the {@link Supplier} which generate the default value.
     * Because the caller can not provider the ThreadLocal contains the value needed
     *
     * @param clazz           class of T
     * @param defaultSupplier default value supplier
     * @param <T>             value class needed
     * @return default value or the value generated before
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> T get(@NonNull Class<T> clazz, @NonNull Supplier<T> defaultSupplier) {
        // must obtain the map here. To avoid the value VALUE_CHECKER_THREAD_LOCAL.get() has changed while running
        final ConcurrentHashMap<Class<?>, Object> map = VALUE_CHECKER_THREAD_LOCAL.get();
        // no data in VALUE_CHECKER_THREAD_LOCAL
        if (!map.containsKey(clazz)) {
            // default value & put to ThreadLocal
            final T data = defaultSupplier.get();
            put(data);
            return data;
        }
        final Object obj = map.get(clazz);
        if (obj.getClass() != clazz) {
            // wrong data in VALUE_CHECKER_THREAD_LOCAL
            map.remove(clazz);
            // default value & put to ThreadLocal
            final T data = defaultSupplier.get();
            put(data);
            return data;
        }
        return (T) obj;
    }

    /**
     * To ensure reentrant functionality, the clear method and init method need to maintain
     * an additional {@code `counter`} variable.
     * <p/>
     * method clear will do clear when {@code `counter`} is 0
     */
    public static void clear() {
        final AtomicInteger counter = VALUE_CHECKER_THREAD_LOCAL_COUNTER.get();
        // counter is null or counter is 0 means it is the first time for clear
        if (null == counter || counter.get() <= 0) {
            VALUE_CHECKER_THREAD_LOCAL.remove();
            VALUE_CHECKER_THREAD_LOCAL_COUNTER.remove();
            return;
        }
        // counter add -1
        counter.addAndGet(-1);
    }


    /**
     * To ensure reentrant functionality, the clear method and init method need to maintain
     * an additional {@code `counter`} variable.
     * <p/>
     * method init will do init when {@code `counter`} is null
     */
    public static void init() {
        final AtomicInteger counter = VALUE_CHECKER_THREAD_LOCAL_COUNTER.get();
        // counter is null means it is the first time for counter
        if (null == counter) {
            VALUE_CHECKER_THREAD_LOCAL.set(new ConcurrentHashMap<>());
            VALUE_CHECKER_THREAD_LOCAL_COUNTER.set(new AtomicInteger());
            return;
        }
        // counter add 1
        counter.addAndGet(1);
    }
}

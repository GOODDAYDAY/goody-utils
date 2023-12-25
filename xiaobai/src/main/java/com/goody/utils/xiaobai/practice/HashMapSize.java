package com.goody.utils.xiaobai.practice;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * init size 16 factor 1.0
 * add value 1 length 16
 * add value 2 length 16
 * add value 3 length 16
 * add value 4 length 16
 * add value 5 length 16
 * add value 6 length 16
 * add value 7 length 16
 * add value 8 length 16
 * add value 9 length 16
 * add value 10 length 16
 * add value 11 length 16
 * add value 12 length 16
 * add value 13 length 16
 * add value 14 length 16
 * add value 15 length 16
 * add value 16 length 16
 * add value 17 length 32
 * <p>
 * init size 16 factor 0.75
 * add value 1 length 16
 * add value 2 length 16
 * add value 3 length 16
 * add value 4 length 16
 * add value 5 length 16
 * add value 6 length 16
 * add value 7 length 16
 * add value 8 length 16
 * add value 9 length 16
 * add value 10 length 16
 * add value 11 length 16
 * add value 12 length 16
 * add value 13 length 32
 * add value 14 length 32
 * add value 15 length 32
 * add value 16 length 32
 * add value 17 length 32
 * <p>
 * init size 16 factor 0.5
 * add value 1 length 16
 * add value 2 length 16
 * add value 3 length 16
 * add value 4 length 16
 * add value 5 length 16
 * add value 6 length 16
 * add value 7 length 16
 * add value 8 length 16
 * add value 9 length 32
 * add value 10 length 32
 * add value 11 length 32
 * add value 12 length 32
 * add value 13 length 32
 * add value 14 length 32
 * add value 15 length 32
 * add value 16 length 32
 * add value 17 length 64
 *
 * @author Goody
 * @version 1.0, 2022/11/29 15:23
 * @since 1.0.0
 */
public class HashMapSize {

    public static void run1(int size, float factor) throws NoSuchFieldException, IllegalAccessException {
        final Field field = HashMap.class.getDeclaredField("table");
        field.setAccessible(true);
        System.out.println("init size " + size + " factor " + factor);
        final Map<Integer, Integer> data = new HashMap<>(size, factor);
        for (int i = 1; i < size + 2; i++) {
            data.put(i, i);
            System.out.println("add value " + i + " length " + ((Map.Entry<String, String>[]) field.get(data)).length);
        }
        System.out.println();
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        run1(16, 1F);
        run1(16, 0.75F);
        run1(16, 0.5F);
    }
}

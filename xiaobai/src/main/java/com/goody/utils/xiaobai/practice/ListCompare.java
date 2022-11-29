package com.goody.utils.xiaobai.practice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 10000000 |Node|new ArrayList<>()|add(DATA)
 * 143
 * 10000000 |Node|new ArrayList<>()|add(new Node())
 * 815
 * 10000000 |Node|new ArrayList<>(size)|add(DATA)
 * 50
 * 10000000 |Node|new ArrayList<>(size)|add(new Node())
 * 92
 * 10000000 |Node|new LinkedList<>()|add(DATA)
 * 143
 * 10000000 |Node|new LinkedList<>()|add(new Node())
 * 2347
 * <p>
 * 10000000 |Node|new ArrayList<>()|add(DATA_STR)
 * 175
 * 10000000 |Node|new ArrayList<>()|add(new String())
 * 70
 * 10000000 |Node|new ArrayList<>(size)|add(DATA_STR)
 * 35
 * 10000000 |Node|new ArrayList<>(size)|add(new String())
 * 31
 * 10000000 |Node|new LinkedList<>()|add(DATA_STR)
 * 198
 * 10000000 |Node|new LinkedList<>()|add(new String())
 * 259
 *
 * @author Goody
 * @version 1.0, 2022/11/29 15:23
 * @since 1.0.0
 */
public class ListCompare {

    private static final Node DATA = new Node();
    private static final String DATA_STR = "DATA";

    public static void run1(int size, int cycle) {
        System.out.println(size + " |Node|new ArrayList<>()|add(DATA)");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<Node> datas = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                datas.add(DATA);
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void run2(int size, int cycle) {
        System.out.println(size + " |Node|new ArrayList<>()|add(new Node())");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<Node> datas = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                datas.add(new Node());
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void run3(int size, int cycle) {
        System.out.println(size + " |Node|new ArrayList<>(size)|add(DATA)");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<Node> datas = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                datas.add(DATA);
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void run4(int size, int cycle) {
        System.out.println(size + " |Node|new ArrayList<>(size)|add(new Node())");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<Node> datas = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                datas.add(new Node());
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void run5(int size, int cycle) {
        System.out.println(size + " |Node|new LinkedList<>()|add(DATA)");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<Node> datas = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                datas.add(DATA);
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void run6(int size, int cycle) {
        System.out.println(size + " |Node|new LinkedList<>()|add(new Node())");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<Node> datas = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                datas.add(new Node());
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void run7(int size, int cycle) {
        System.out.println(size + " |Node|new ArrayList<>()|add(DATA_STR)");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<String> datas = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                datas.add(DATA_STR);
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void run8(int size, int cycle) {
        System.out.println(size + " |Node|new ArrayList<>()|add(new String())");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<String> datas = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                datas.add("DATA");
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void run9(int size, int cycle) {
        System.out.println(size + " |Node|new ArrayList<>(size)|add(DATA_STR)");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<String> datas = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                datas.add(DATA_STR);
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void run10(int size, int cycle) {
        System.out.println(size + " |Node|new ArrayList<>(size)|add(new String())");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<String> datas = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                datas.add("DATA");
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void run11(int size, int cycle) {
        System.out.println(size + " |Node|new LinkedList<>()|add(DATA_STR)");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<String> datas = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                datas.add(DATA_STR);
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void run12(int size, int cycle) {
        System.out.println(size + " |Node|new LinkedList<>()|add(new String())");
        final long now = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            final List<String> datas = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                datas.add("DATA");
            }
        }
        System.out.println((System.currentTimeMillis() - now) / cycle);
    }

    public static void main(String[] args) {
        final int size = 10000000;
        final int cycle = 10;
        run1(size, cycle);
        run2(size, cycle);
        run3(size, cycle);
        run4(size, cycle);
        run5(size, cycle);
        run6(size, cycle);
        run7(size, cycle);
        run8(size, cycle);
        run9(size, cycle);
        run10(size, cycle);
        run11(size, cycle);
        run12(size, cycle);
    }

    public static class Node {
    }
}

package com.xq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        list();
    }


    static void arrays() {
        int[] array = new int[10];
        array[0] = 1;
        array[1] = 2;
        array[2] = 3;
        array[3] = 4;
        array[4] = 5;
        array[5] = 6;
        array[6] = 7;
        array[7] = 8;
        array[8] = 9;
        array[9] = 10;

        for (int item : array) {
            System.out.println(item);
        }

        Arrays.stream(array).forEach(System.out::println);
    }

    static void array2D() {
        char[][] array = new char[][]{
                {'_', '_', '_',},
                {'_', '_', '_',},
                {'_', '_', '_',}
        };

        array[0][0] = '0';
        array[1][0] = '0';
        array[2][0] = '0';
        System.out.println(Arrays.deepToString(array));
        System.out.println(array.length);
    }

    static void list() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        System.out.println(list);
        System.out.println(list.size());
        System.out.println(list.contains("1"));

        for (String s : list) {
            System.out.println(s);
        }

        list.forEach(System.out::println);
    }
}
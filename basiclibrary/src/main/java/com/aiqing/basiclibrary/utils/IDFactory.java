package com.aiqing.basiclibrary.utils;

import java.util.HashSet;
import java.util.Random;

public class IDFactory {
    private static Random random = new Random();
    private static HashSet<Integer> ids = new HashSet();

    public IDFactory() {
    }

    public static int getRandomNumber(int min, int max) {
        int tmp = Math.abs(random.nextInt());
        int result = tmp % (max - min + 1) + min;
        return result;
    }

    public static int generateId() {
        int id;
        for (id = getRandomNumber(10000000, 99999999); ids.contains(id); id = getRandomNumber(10000000, 99999999)) {
        }

        ids.add(id);
        return id;
    }
}
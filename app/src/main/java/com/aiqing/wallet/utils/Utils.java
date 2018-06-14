package com.aiqing.wallet.utils;

import java.math.BigDecimal;

public class Utils {
    public static String keepTwoBits(double d) {
        return String.format("%.2f", d);
    }

    public static String keepFourBits(double d) {
        return String.format("%.4f", d);
    }

    public static Double adddouble(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return new Double(b1.add(b2).doubleValue());
    }
    public static Double adddouble(Double d1, Double d2, Double d3) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        BigDecimal b3 = new BigDecimal(d3.toString());
        return new Double(b1.add(b2).add(b3).doubleValue());
    }

    public static double Multiply(double numA, double numB) {
        BigDecimal bigA = new BigDecimal(Double.toString(numA));
        BigDecimal bigB = new BigDecimal(Double.toString(numB));
        return bigA.multiply(bigB).doubleValue();
    }
}
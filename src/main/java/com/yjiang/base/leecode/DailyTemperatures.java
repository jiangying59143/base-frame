package com.yjiang.base.leecode;

import java.util.Arrays;

public class DailyTemperatures {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();

        System.out.println(Arrays.toString(dailyTemperatures.dailyTemperatures(new int[]{0})));
    }

    public int[] dailyTemperatures(int[] T) {
        int[] r = new int[T.length];
        for (int i = 0; i < T.length; i++) {
            int x = T[i];
            for (int j = i+1; j < T.length; j++) {
                int y = T[j];
                if(y>x){
                    r[i] = j-i;
                    break;
                }
            }
        }
        return r;
    }
}

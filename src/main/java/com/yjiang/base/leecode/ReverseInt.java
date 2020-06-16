package com.yjiang.base.leecode;

public class ReverseInt {
    public static void main(String[] args) {
        System.out.println(reverse(1534236469));
        System.out.println(2000000000+147483648 );
    }

    public static int reverse(int x) {
        boolean negativeFlag = false;
        if(x < 0){
            if(x == -Math.pow(2, 31)){
                return 0;
            }
            x = -x;
            negativeFlag = true;
        }
        int[] ai = new int[10];
        int i = 0;
        while(x/10 > 0){
            ai[i] = x % 10;
            x = x / 10;
            i++;
        }
        ai[i] = x;
        x = 0;


        for(int a : ai){
            int y = a * (int)Math.pow(10, i);
            if((int)Math.pow(10, i) > 0 && !(y % (int)Math.pow(10, i) ==0 && y/(int)Math.pow(10, i)==a)){
                return 0;
            }
            x += y;
            if(x < 0){
                return 0;
            }
            i--;
        }

        if(negativeFlag){
            x = -x;
        }

        return x;
    }

}

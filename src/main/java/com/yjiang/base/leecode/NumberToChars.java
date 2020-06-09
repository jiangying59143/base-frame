package com.yjiang.base.leecode;


public class NumberToChars {
    public static void main(String[] args) {
        NumberToChars numberToChars = new NumberToChars();
        System.out.println(numberToChars.translateNum(817230807));
//        System.out.println(numberToChars.f(4));
    }

    public int translateNum(int num) {
        String numStr = String.valueOf(num);
        int returnCount = 1;
        int slash=0;
        for (int i = 1; i <  numStr.length(); i++) {
            if(numStr.charAt(i)=='0'){
                if(Integer.parseInt(numStr.substring(i-1, i+1)) < 25) {
                    returnCount *= f(i + 1 - slash);
                }else{
                    returnCount *= f(i - slash);
                }
                slash = i + 1;
            }else {
                if (Integer.parseInt(numStr.substring(i - 1, i + 1)) > 25) {
                    returnCount *= f(i - slash);
                    slash = i;
                }
            }
        }
        if(slash < numStr.length()-1){
            returnCount *= f(numStr.length()-slash);
        }
        return returnCount;
    }

    public int f(int n){
        if(n==1){
            return 1;
        }
        if(n==2){
            return 2;
        }
        if(n>2){
            return f(n-1) + f(n-2);
        }
        return 0;
    }
}

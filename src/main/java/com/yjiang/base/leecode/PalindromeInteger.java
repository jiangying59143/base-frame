package com.yjiang.base.leecode;

import java.util.ArrayList;
import java.util.List;

public class PalindromeInteger {
    public static void main(String[] args) {
        PalindromeInteger palindromeInteger = new PalindromeInteger();
        System.out.println(palindromeInteger.isPalindrome(123321));
    }

    public boolean isPalindrome(int x) {
        if(x < 0){
            return false;
        }
        int y = x;
        List<Integer> list = new ArrayList<>();
        while(x/10>0){
            list.add(x%10);
            x = x/10;
        }
        list.add(x);

        int z = 0;
        for (int i = 0; i < list.size(); i++) {
            z +=  list.get(i)*(int)Math.pow(10, list.size()-i-1);
        }
        if(z == y){
            return true;
        }
        return false;
    }
}

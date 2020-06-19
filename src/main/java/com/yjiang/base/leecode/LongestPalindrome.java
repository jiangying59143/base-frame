package com.yjiang.base.leecode;

public class LongestPalindrome {
    public static void main(String[] args) {
        System.out.println(longestPalindrome("abcb"));
    }
    public static String longestPalindrome(String s) {
        if(s==null){
            return null;
        }
        String ls = "";
        for (int i = s.length() / 2; i > 0; i--) {
            for (int j = 0; j <= i; j++) {
                ls = longestPalindrome(i, j, s, ls);
            }
        }
        for (int i = s.length() / 2 + 1; i < s.length(); i++) {
            for (int j = 0; j <= s.length()-1-i; j++) {
                ls = longestPalindrome(i, j, s, ls);
            }
        }
        return ls;
    }

    private static String longestPalindrome(int i, int j, String s, String ls) {
        String y = s.substring(i-j,i+j+1);
        if(i-j >=0 && i+j < s.length()) {
            if (s.charAt(i - j) != s.charAt(i + j) && y.length()>s.length()) {
                ls = y;
                return ls;
            }
        }else{
            ls = y;
            return ls;
        }
        return ls;
    }
}

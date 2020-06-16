package com.yjiang.base.leecode;

public class LongestCommonPrefix {
    public static void main(String[] args) {
        System.out.println(longestCommonPrefix(null));
    }
    public static String longestCommonPrefix(String[] strs) {
        if(strs == null){
            return null;
        }
        String s1 = strs[0];
        for(int j=0; j<s1.length();j++){
            boolean allEqual = true;
            for(int i=1; i<strs.length; i++){
                if(s1.charAt(j) != strs[i].charAt(j)){
                    return s1.substring(0,j);
                }
            }
        }
        return s1;
    }
}

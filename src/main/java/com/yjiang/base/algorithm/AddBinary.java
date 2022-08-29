package com.yjiang.base.algorithm;

public class AddBinary {
    public static void main(String[] args) {
        System.out.println(addBinary("11", "1"));
//        System.out.println((0 & 0 | 1 & 0));
    }
    public static String addBinary(String a, String b) {
        int al = a.length()-1;
        int bl = b.length()-1;
        StringBuilder sb = new StringBuilder();
        char carry = '0';
        char x;
        char y;
        while(al >= 0 || bl >= 0 || carry == '1'){
            x = al >= 0 ? a.charAt(al--) : '0';
            y = bl >= 0 ? b.charAt(bl--) : '0';
            sb.append((char)(x ^ y ^ carry));
            carry = (char) (x & y | x & carry | carry & y);
        }
        return sb.reverse().toString();

    }
}

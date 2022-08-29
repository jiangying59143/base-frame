package com.yjiang.base.algorithm;

public class ClimbStairs {
    public static void main(String[] args) {
        System.out.println(climbStairs(4));
    }

    public static int climbStairs(int n) {
        if(n <= 1){
            return 1;
        }
        int i=2, pre=1, next=1, ans = -1;
        while(i <= n){
            ans = pre + next;
            pre=next;
            next = ans;
            i++;
        }
        return ans;
    }
}

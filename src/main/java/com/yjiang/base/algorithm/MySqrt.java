package com.yjiang.base.algorithm;

public class MySqrt {
    public static void main(String[] args) {
        System.out.println();
        System.out.println(mySqrt(8));
        System.out.println(3<<1|1);
    }

    public static int mySqrt(int x) {
        int l=0, r=x, ans=-1;
        int mid;
        while(l<=r){
            mid=l + ((r-l)>>1);
            if((long)mid*mid <= x){
                ans=mid;
                l=mid+1;
            }else{
                r=mid-1;
            }
        }
        return ans;
    }
}

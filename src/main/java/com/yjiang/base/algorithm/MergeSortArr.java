package com.yjiang.base.algorithm;

import java.util.Arrays;

public class MergeSortArr {

    public static void main(String[] args) {
        int a[] = new int[]{2,0,0,0,0};
        merge(a, 1, new int[]{1,3,4,5}, 4);
        System.out.println(Arrays.toString(a));
    }

    public static void merge1(int[] nums1, int m, int[] nums2, int n) {
        System.arraycopy(nums2, 0, nums1, m, n);
        Arrays.sort(nums1);
    }


    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int p3=m-- +n-- -1;
        while(n >= 0) {
            if(m<0){
                nums1[p3--] = nums2[n--];
                continue;
            }
            if (nums1[m] < nums2[n]) {
                nums1[p3--] = nums2[n--];
            } else {
                nums1[p3--] = nums1[m];
                nums1[m--] = 0;
            }
        }

    }

}

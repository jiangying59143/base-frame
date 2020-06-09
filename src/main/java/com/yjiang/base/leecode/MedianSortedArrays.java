package com.yjiang.base.leecode;

public class MedianSortedArrays {
    public static double findMedianSortedArrays(int[] nums1, int[] nums2, int i, int j, int k) {
        int k1 = i + k/2 -1;
        int k2 = k-k1-1;

        if(k1>=0 && k1<=nums1.length-1 && k2>=0 && k2<=nums2.length-1) {
            if (nums1[k1] <= nums2[k2 + 1] && nums1[k1 + 1] >= nums2[k2]) {
                return (Math.max(nums1[k1], nums2[k2]) + Math.min(nums1[k1 + 1], nums2[k2 + 1])) / 2d;
            } else if (nums1[k1] > nums2[k2 + 1]) {
                findMedianSortedArrays(nums1, nums2, i, k2, k / 2);
            } else if (nums1[k1 + 1] < nums2[k2]) {
                findMedianSortedArrays(nums1, nums2, k1, j, k / 2);
            }
        }

        return 0d;
    }

    public static void main(String[] args) {
        int[] a = new int[]{1, 2, 7, 8};
        int[] b = new int[]{3, 4, 5, 6};

        int middle1 = (a.length + b.length)/2;
        int middle2 = (a.length + b.length-1)/2;

        double l = (findMedianSortedArrays(a, b, 0, 0,  middle1) + findMedianSortedArrays(a, b, 0, 0,  middle2))/2d;

        System.out.println(l);
    }
}

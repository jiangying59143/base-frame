package com.yjiang.base.leecode;


import java.util.*;

public class ThreeSum {
    public static void main(String[] args) {
        ThreeSum threeSum = new ThreeSum();
        List<List<Integer>> list = threeSum.threeSum(new int[]{-7,2,1,10,9,-10,-5,4,13,-9,-4,6,11,-12,-6,-9,-6,-9,-11,-4,10,10,-3,-1,-4,-7,-12,-15,11,5,14,11,-7,-8,6,9,-2,9,-10,-12,-15,2,10,4,5,11,10,6,-13,6,-13,12,-7,-9,-12,4,-9,13,-4,10,4,-12,6,4,-5,-10,-2,0,14,4,4,6,13,-9,-5,-5,-13,12,-14,11,3,10,8,11,-13,4,-8,-7,2,4,10,13,7,2,2,9,-1,8,-5,-10,-3,6,3,-5,12,6,-3,6,3,-2,2,14,-7,-13,10,-13,-2,-12,-4,8,-1,13,6,-9,0,-14,-15,6,9});
        list.forEach(l-> System.out.println(Arrays.toString(l.toArray())));
    }

    public List<List<Integer>> threeSum(int[] nums) {
        for (int i = 0; i < nums.length-1; i++) {
            for (int j = i+1; j < nums.length; j++) {
                if(nums[i]>nums[j]){
                    int x = nums[i];
                    nums[i]=nums[j];
                    nums[j]=x;
                }
            }
        }

        List<List<Integer>> list = new ArrayList<>();
        List<String> dl = new ArrayList<>();

        for (int i = 0; i < nums.length-2; i++) {
            int n1 = nums[i];
            for (int j = i+1; j < nums.length-1; j++) {
                int n2 = nums[j];
                int n3 = 0-n1-n2;
                if(dl.contains(String.valueOf(n1) + "||" + String.valueOf(n2) + "||" + String.valueOf(n3))){
                    continue;
                }
                for (int z = j+1; z < nums.length; z++) {
                    if(n3 == nums[z]){
                        List<Integer> tempList = new ArrayList<>();
                        tempList.add(n1);
                        tempList.add(n2);
                        tempList.add(n3);
                        list.add(tempList);
                        dl.add(String.valueOf(n1) + "||" + String.valueOf(n2) + "||" + String.valueOf(n3));
                    }
                }
            }
        }
        return list;
    }



}

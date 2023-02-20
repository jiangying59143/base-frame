package com.yjiang.base.leecode;

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] matrix = new int[][]{
                {1,   4,  7, 11, 15},
                {2,   5,  8, 12, 19},
                {3,   6,  9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}
        };
        boolean flag = solution.findNumberIn2DArray(matrix, 5);
        System.out.println(flag);
    }
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if(matrix==null || matrix.length==0 || matrix[0] != null && matrix[0].length==0){
            return false;
        }
        int lx = 0;
        int ly= 0 ;
        int rx = matrix.length-1;
        int ry = matrix[0].length-1;
        int mx = (lx+rx)/2;
        int my = (ly+ry)/2;
        while(lx <= rx && ly <= ry){
            if(matrix[mx][my] == target){
                return true;
            }else{
                if(matrix[lx][ly] <= target && matrix[mx][my] > target){
                    rx= ry==0 ? rx-1 : rx;
                    ry= ry==0 ? matrix[mx].length-1 : my;
                }else if(matrix[mx][my] < target && matrix[rx][ry] >= target){
                    lx = my==matrix[mx].length-1 ? mx + 1 : mx;
                    ly = my==matrix[mx].length-1 ? 0 : my + 1;
                }else{
                    return false;
                }
                mx = (lx+rx)/2;
                my = (ly+ry)/2;
            }
        }
        return false;
    }
}

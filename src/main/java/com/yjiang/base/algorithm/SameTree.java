package com.yjiang.base.algorithm;

import java.util.Stack;

public class SameTree {
    public static void main(String[] args) {
    }

    public static boolean isSameTree(TreeNode p, TreeNode q) {
        Stack<TreeNode> pstack = new Stack<>();
        Stack<TreeNode> qstack = new Stack<>();
        while(p != null || q != null){
            if(!isSameNode(p, q) || !isSameNode(p.left, q.left) || !isSameNode(p.right, q.right)){
                return false;
            }else{
                if(p.left != null){
                    p = p.left;
                    q = q.left;
                }
                if(p.right != null){
                    p = p.right;
                    q = q.right;
                }
            }
        }
        return true;
    }

    private static boolean isSameNode(TreeNode p, TreeNode q){
        if(p == null && q== null || p != null && q != null && p.val == q.val){
            return true;
        }
        return false;
    }


     private class TreeNode {
         int val;
         TreeNode left;
         TreeNode right;
         TreeNode() {}
         TreeNode(int val) { this.val = val; }
         TreeNode(int val, TreeNode left, TreeNode right) {
             this.val = val;
             this.left = left;
             this.right = right;
         }
     }
}

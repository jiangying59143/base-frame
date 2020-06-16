package com.yjiang.base.leecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */

class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }

    @Override
    public String toString() {
        return "TreeNode{" +
                "val=" + val +
                ", left=" + left +
                ", right=" + right +
                '}';
    }
}
public class TreeCodec {

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        List<Object> list = new ArrayList<>();
        if(root == null){
            list.add(null);
        }else {
            list.add(root.val);
            subSerialize(list, root);
        }
        return Arrays.toString(list.toArray());
    }

    private void subSerialize(List<Object> list, TreeNode treeNode){
        if(treeNode.left == null){
            list.add(null);
        }else{
            list.add(treeNode.left.val);
        }
        if(treeNode.right == null){
            list.add(null);
        }else{
            list.add(treeNode.right.val);
        }
        if(treeNode.left != null){
            subSerialize(list, treeNode.left);
        }
        if(treeNode.right != null){
            subSerialize(list, treeNode.right);
        }
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        List<String> list = Arrays.asList(data.substring(1, data.length()-1).split(", "));
        Iterator<String> iterator = list.iterator();
        TreeNode treeNode = null;
        if(iterator.hasNext()) {
            String s = iterator.next();
            if(!"null".equals(s)) {
                treeNode = new TreeNode(Integer.parseInt(s));
                subDeserialize(iterator, treeNode);
            }
        }
        return treeNode;
    }

    private void subDeserialize(Iterator<String> iterator, TreeNode treeNode){
        if(iterator.hasNext()) {
            String sl = iterator.next();
            if(!"null".equals(sl)) {
                treeNode.left = new TreeNode(Integer.parseInt(sl));
            }
        }
        if(iterator.hasNext()) {
            String sr = iterator.next();
            if(!"null".equals(sr)) {
                treeNode.right = new TreeNode(Integer.parseInt(sr));
            }
        }
        if(treeNode.left != null){
            subDeserialize(iterator, treeNode.left);
        }
        if(treeNode.right != null){
            subDeserialize(iterator, treeNode.right);
        }
    }

    public static void main(String[] args) {
        TreeCodec treeCodec = new TreeCodec();
        TreeNode treeNode = new TreeNode(1);
        treeNode.left = new TreeNode(2);
        treeNode.right = new TreeNode(3);
        treeNode.right.left = new TreeNode(4);
        treeNode.right.right = new TreeNode(5);
        System.out.println(treeCodec.serialize(treeNode));

        System.out.println(treeCodec.deserialize("[1, 2, 3, null, null, 4, 5, null, null, null, null]"));
    }
}

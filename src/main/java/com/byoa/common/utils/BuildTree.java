package com.byoa.common.utils;
import com.byoa.common.domain.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTree {


    public static <T> Tree<T> build(List<Tree<T>> nodes) {

        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<Tree<T>>();

        for (Tree<T> children : nodes) {

            String pid = children.getParentId();
            if (pid == null || "0".equals(pid)) {
                topNodes.add(children);
                continue;
            }

            for (Tree<T> parent : nodes) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChildren().add(children);
                    children.setHasParent(true);
                    parent.setChildren(true);
                    continue;
                }
            }

        }

        Tree<T> root = new Tree<T>();
        if (topNodes.size() == 1) {
            root = topNodes.get(0);
        } else {
            root.setId("-1");
            root.setParentId("");
            root.setHasParent(false);
            root.setChildren(true);
            root.setChecked(true);
            root.setChildren(topNodes);
            root.setText("顶级节点");
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened" , true);
            root.setState(state);
        }

        return root;
    }


    //先找根节点
    public static  <T> Tree<T> getRoot(List<Tree<T>> nodes,String id){
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<Tree<T>>();
        for (Tree<T> children : nodes) {

            String pid = children.getParentId();
            if (pid == null || id.equals(pid)) {
                List<Tree<T>> t = getChild(nodes, children.getId());
                if(t.size() > 0){
                    children.setHasParent(false);
                    children.setChildren(true);
                    topNodes.add(children);
                }
                continue;
            }
        }

        Tree<T> root = new Tree<T>();
        if (topNodes.size() == 1) {
            root = topNodes.get(0);
        } else {
            root.setId("-1");
            root.setParentId("");
            root.setHasParent(false);
            root.setChildren(true);
            root.setChecked(true);
            root.setChildren(topNodes);
            root.setText("顶级节点");
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened" , true);
            root.setState(state);
        }
        return root;
    }

    //找字节点
    private static <T> List<Tree<T>> getChild(List<Tree<T>> nodes,String id){
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> trees = new ArrayList<>();
        for (Tree<T> children : nodes) {
            String pid = children.getParentId();
            if (pid == null || id.equals(pid)) {
                //匹配到父节点
                children.setHasParent(true);
                //找到此节点的子节点
                List<Tree<T>> t = getChild(nodes, children.getId());
                //如果此节点的有子节点 设置说明此节点的字节点
                if(t.size() > 0){
                    children.setChildren(true);
                    children.setChildren(t);
                }
                trees.add(children);
                continue;
            }
        }
        return trees;
    }


    public static <T> List<Tree<T>> buildList(List<Tree<T>> nodes, String idParam) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<Tree<T>>();

        for (Tree<T> children : nodes) {

            String pid = children.getParentId();
            if (pid == null || idParam.equals(pid)) {
                topNodes.add(children);
                continue;
            }

            for (Tree<T> parent : nodes) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    if(id.equals("32")){

                    }
                    parent.getChildren().add(children);
                    children.setHasParent(true);
                    parent.setChildren(true);
                    continue;
                }
            }

        }
        return topNodes;
    }

}
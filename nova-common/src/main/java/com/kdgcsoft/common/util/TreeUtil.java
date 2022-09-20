package com.kdgcsoft.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.kdgcsoft.common.interfaces.ITreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fyin
 * @date 2020-04-03 13:50
 */
public class TreeUtil {
    public static <T extends ITreeNode> List<T> buildTree(List<? extends ITreeNode> nodeList) {
        return buildTree(nodeList, false);
    }

    /**
     * 根据节点列表构造树形结构,通过cascade控制是否强制级联,如果为true的情况下 如果节点未找到上级节点则不会加入到树中
     *
     * @param nodeList
     * @param cascade
     * @param <T>
     * @return
     */
    public static <T extends ITreeNode> List<T> buildTree(List<? extends ITreeNode> nodeList, boolean cascade) {
        List<T> tree = new ArrayList<>();
        //先找到所有的根节点,所有找不到父节点的均认为是根节点
        if (CollUtil.isNotEmpty(nodeList)) {
            for (ITreeNode node : nodeList) {
                ITreeNode pnode = CollUtil.findOne(nodeList, n -> ObjectUtil.equals(n.id(), node.pid()));
                if (pnode == null) {
                    //只会分析父节点为null或0的,如果强制级联为true则不会分析找不到父节点的node
                    if (node.pid() == null || node.pid().toString().equals("0") || !cascade) {
                        tree.add((T) node);
                    }
                }
            }
        }
        //根据根节点创建一个待处理列表
        List<T> todoList = CollUtil.newArrayList(tree);
        while (!todoList.isEmpty()) {
            T todoNode = todoList.get(0);
            todoList.remove(0);
            nodeList.forEach(node -> {
                //从原始列表中查找子节点,找到后添加给当前节点作为子节点,同时将该子节点添加到待处理列表中
                //待处理列表会不断的增加减少,等到待处理列表中没有要处理的对象了 代表所有的节点都查找完毕
                if (ObjectUtil.equals(todoNode.id(), node.pid())) {
                    todoNode.addChild(node);
                    todoList.add((T) node);
                }
            });
        }
        return tree;
    }

    /**
     * 从树形节点列表中查找到指定ID的节点
     *
     * @param nodeList
     * @param id
     * @param <T>
     * @return
     */
    public static <T extends ITreeNode> T findOneById(List<? extends ITreeNode> nodeList, Object id) {
        if (CollUtil.isEmpty(nodeList)) {
            return null;
        } else {
            for (ITreeNode node : nodeList) {
                if (node.id().equals(id)) {
                    return (T) node;
                } else {
                    if (CollUtil.isNotEmpty(node.getChildren())) {
                        return TreeUtil.findOneById((List<? extends ITreeNode>) node.getChildren(), id);
                    }
                }
            }
        }
        return null;
    }


    public static <T> List<T> getChildrenIds(ITreeNode node, Class<T> idType) {
        List<T> childIds = new ArrayList<>();
        if (node != null) {
            getChildrenIds(node, childIds);
        }
        return childIds;
    }

    public static <T> void getChildrenIds(ITreeNode<? extends ITreeNode> node, List<T> list) {
        if (CollUtil.isNotEmpty(node.getChildren())) {
            for (ITreeNode iTreeNode : node.getChildren()) {
                if (iTreeNode.id() != null) {
                    list.add((T) iTreeNode.id());
                }
                getChildrenIds((ITreeNode<? extends ITreeNode>) iTreeNode.getChildren(), list);
            }
        }
    }
}

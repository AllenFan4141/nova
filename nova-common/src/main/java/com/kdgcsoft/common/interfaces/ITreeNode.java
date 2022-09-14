package com.kdgcsoft.common.interfaces;

/**
 * @author fyin
 * @date 2020-04-03 13:50
 * 树形节点接口 实现此接口的类 可以使TreeBuilder将List<ITreeNode>构建成树形结构
 */
public interface ITreeNode<T> {
    /**
     * 节点ID
     *
     * @return
     */
    Object id();

    /**
     * 父节点ID
     *
     * @return
     */
    Object pid();

    /**
     * 添加子节点
     *
     * @param node
     */
    void addChild(T node);
}

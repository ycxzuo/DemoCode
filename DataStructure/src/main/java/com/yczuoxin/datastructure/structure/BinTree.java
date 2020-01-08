package com.yczuoxin.datastructure.structure;

public class BinTree<E extends Comparable> {

    Node<E> root;

    private class Node<E> {
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;

        public Node(E element) {
            this.element = element;
        }
    }

    public void add (E e) {
        Node<E> newNode = new Node<>(e);
        if (root == null) {
            root = newNode;
        } else {
            Node<E> current = root;
            Node<E> prev = root;
            while (current != null) {
                prev = current;
                if (current.element.compareTo(e) > 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
            if (prev.element.compareTo(newNode.element) > 0) {
                prev.left = newNode;
            } else {
                prev.right = newNode;
            }
            newNode.parent = prev;
        }
    }

    /**
     * 1. 子节点
     *      直接删除
     * 2. 父节点
     *      只有左子树
     *      只有右子树
     *      既有左子树，又有右子树，将左子树挂载到右子树的最左节点上
     * 3. 根节点
     * @param e
     * @return
     */
    public boolean remove (E e) {
        Node<E> node = search(e);
        if (null != node) {
            // 子节点
            if (null == node.left && null == node.right) {
                if (node == root) {
                    root = null;
                    return true;
                }
                Node<E> parent = node.parent;
                if (parent.right == node) {
                    parent.right = null;
                } else {
                    parent.left = null;
                }
                return true;
            }
            // 左子树为空
            if (null == node.left) {
                if (node == root) {
                    root = node.right;
                    return true;
                }
                Node<E> parent = node.parent;
                if (parent.right == node) {
                    parent.right = null;
                } else {
                    parent.left = null;
                }
            }
        }
        return false;
    }

    public Node<E> search (E e) {
        if (null == root) {
            return null;
        }
        Node<E> current = root;
        while (current != null) {
            if (current.element == e) {
                return current;
            }
            if (current.element.compareTo(e) > 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

}

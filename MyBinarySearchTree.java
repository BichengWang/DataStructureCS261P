package multiTrees;

import java.util.LinkedList;
import java.util.Queue;

public class MyBinarySearchTree<T> extends MyTree<T> {

    private class MyTreeNode {
        int key;
        MyTreeNode left;
        MyTreeNode right;
        MyTreeNode parent;
        T data;

        public MyTreeNode(int key, T data, MyTreeNode parent, MyTreeNode left, MyTreeNode right) {
            this.key = key;
            this.data = data;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public MyTreeNode(){

        }

        public MyTreeNode(int key, int value) {
            this.key = key;
            this.data = (T)new Integer(value);
        }

        public MyTreeNode(int key, T data){
            this.key = key;
            this.data = data;
        }

        public void copyKeyValue(MyTreeNode other) {
            this.key = other.key;
            this.data = other.data;
        }

    }

    MyTreeNode root;

    public MyBinarySearchTree() {
        this.root = null;
    }

    @Override
    public T search(int key) {
        MyTreeNode cur = this.root;
        while (cur != null) {
            if (cur.key > key) {
                cur = cur.left;
            } else if (cur.key < key) {
                cur = cur.right;
            } else {
                return cur.data;
            }
        }
        return null;
    }

    @Override
    public T insert(int key) {
        if (this.root == null) {
            this.root = new MyTreeNode(key, key);
            return null;
        }
        MyTreeNode parent = null;
        MyTreeNode cur = this.root;
        while (cur != null) {
            parent = cur;
            if (cur.key > key) {
                cur = cur.left;
            } else if (cur.key < key) {
                cur = cur.right;
            } else {
                return cur.data;
            }
        }
        if (parent.key > key) {
            parent.left = new MyTreeNode(key, key);
        } else if (parent.key < key) {
            parent.right = new MyTreeNode(key, key);
        }
        return null;
    }

    @Override
    public T delete(int key) {
        if (this.root == null) {
            return null;
        }
        if (key == this.root.key) {
            T res = this.root.data;
            if (this.root.left == null) {
                this.root = this.root.right;
            } else if (this.root.right == null) {
                this.root = this.root.left;
            } else {
                MyTreeNode pred = findPredeccessorAndRemove(this.root);
                if (pred != null) {
                    this.root.copyKeyValue(pred);
                } else {
                    this.root = null;
                }
            }
            return res;
        }

        MyTreeNode prev = null;
        MyTreeNode cur = this.root;
        while (cur != null) {
            if (cur.key > key) {
                prev = cur;
                cur = cur.left;
            } else if (cur.key < key) {
                prev = cur;
                cur = cur.right;
            } else {
                break;
            }
        }

        if (cur != null) {
            T res = cur.data;
            if (cur.left == null) {
                if (prev.left == cur) {
                    prev.left = cur.right;
                } else {
                    prev.right = cur.right;
                }
            } else if (cur.right == null) {
                if (prev.left == cur) {
                    prev.left = cur.left;
                } else {
                    prev.right = cur.left;
                }
            } else {
                MyTreeNode pred = findPredeccessorAndRemove(cur);
                if (pred != null) {
                    cur.copyKeyValue(pred);
                } else {
                    System.err.println("pred impossible in delete");
                }
            }
            return res;
        }
        return null;
    }

    /**
     * find predeccessor node
     * if no predeccessor: return null.
     * else: remove node from tree, return it.
     *
     * @param cur
     * @return
     */
    private MyTreeNode findPredeccessorAndRemove(MyTreeNode cur) {
        MyTreeNode parent = cur;
        cur = cur.left;
        if (cur == null) {
            return null;
        }

        while (cur.right != null) {
            parent = cur;
            cur = cur.right;
        }

        if (parent.left == cur) {
            parent.left = cur.left;
        } else if (parent.right == cur) {
            parent.right = cur.left;
        } else {
            System.err.println("find predeceesor error");
        }

        // GC recycle and protect other
        cur.left = null;
        cur.right = null;
        return cur;
    }

    @Override
    public void print(){
        Queue<MyTreeNode> q = new LinkedList<>();
        q.offer(this.root);
        while(!q.isEmpty()){
            int size = q.size();
            for(int i = 0; i < size; i++){
                MyTreeNode cur = q.poll();
                if(cur == null){
                    System.out.print("\t\t");
                } else {
                    System.out.print("\t" + cur.key + "\t");
                    q.offer(cur.left);
                    q.offer(cur.right);
                }
            }
            System.out.println();
        }
        return;
    }

    @Override
    public void printDepthNNumber() {

        Queue<MyTreeNode> q = new LinkedList<>();
        q.offer(this.root);
        int depth = 0;
        long eNum = 0;
        while(!q.isEmpty()){
            int size = q.size();
            depth++;
            for(int i = 0; i < size; i++){
                MyTreeNode cur = q.poll();
                    eNum++;
                    if(cur.left != null) {
                        q.offer(cur.left);
                    }
                    if(cur.right != null) {
                        q.offer(cur.right);
                    }
            }
        }
        System.out.println("depth: " + depth + "\telements: " + eNum);
        return;
    }
}

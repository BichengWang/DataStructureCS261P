package multiTrees;

import java.util.LinkedList;
import java.util.Queue;

/**
 * TODO: not yet implemented
 * @param <T>
 */
public class MyRBTree<T> extends MyTree<T> {

    private class MyRBTreeNode {
        int key = 0;
        Object data = null;
        MyRBTreeNode left = null;
        MyRBTreeNode right = null;
        MyRBTreeNode parent = null;
        boolean isRed = false;

        public MyRBTreeNode(){

        }

        public MyRBTreeNode(int key, Object data, MyRBTreeNode parent, MyRBTreeNode left, MyRBTreeNode right) {
            this.key = key;
            this.data = data;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public MyRBTreeNode(int key, int value, MyRBTreeNode parent, MyRBTreeNode left, MyRBTreeNode right, boolean isRed) {
            this(key, value);
            this.left = left;
            this.right = right;
            this.parent = parent;
            this.isRed = isRed;
        }

        public MyRBTreeNode(int key, int value) {
            this.key = key;
            this.data = new Integer(value);
        }


    }

    private MyRBTreeNode root;
    private MyRBTreeNode guard;

    public MyRBTree() {
        this.root = null;
        this.guard = new MyRBTreeNode();
    }

    @Override
    public T search(int key) {
        MyRBTreeNode cur = this.root;
        while (cur != null) {
            if (cur.key > key) {
                cur = cur.left;
            } else if (cur.key < key) {
                cur = cur.right;
            } else {
                return (T) cur.data;
            }
        }
        return null;
    }

    @Override
    public T insert(int key) {
        if(this.root == null){
            this.root = new MyRBTreeNode(key, key, this.guard, this.guard, this.guard, false);
            return null;
        }

        MyRBTreeNode prev = this.guard;
        MyRBTreeNode cur = this.root;
        while(cur != this.guard){
            prev = cur;
            if(key < cur.key){
                cur = cur.left;
            }else if(key > cur.key){
                cur = cur.right;
            }else{
                return (T)cur.data;
            }
        }
        cur = new MyRBTreeNode(key, key, prev, this.guard, this.guard, true);
        if(key < prev.key){
            prev.left = cur;
        }else{
            prev.right = cur;
        }
        insertFixup(cur);
        return null;
    }

    private void insertFixup(MyRBTreeNode cur){
        MyRBTreeNode prev = null;
        while(cur.parent.isRed){
            if(cur.parent == cur.parent.parent.left){
                prev = cur.parent.parent.right;
                if(prev.isRed){
                    cur.parent.isRed = false;
                    prev.isRed = false;
                    cur.parent.parent.isRed = true;
                    cur = cur.parent.parent;
                }else if(cur == cur.parent.right){
                    cur = cur.parent;
                    leftRotated(cur);
                }
                cur.parent.isRed = false;
                cur.parent.parent.isRed = true;
                rightRotated(cur.parent.parent);
            }else{
                prev = cur.parent.parent.left;
                if(prev.isRed){
                    cur.parent.isRed = false;
                    prev.isRed = false;
                    cur.parent.parent.isRed = true;
                    cur = cur.parent.parent;
                }else if(cur == cur.parent.left){
                    cur = cur.parent;
                    rightRotated(cur);
                }
                cur.parent.isRed = false;
                cur.parent.parent.isRed = true;
                leftRotated(cur.parent.parent);
            }
        }
        this.root.isRed = false;
    }

    private void leftRotated(MyRBTreeNode cur){
        MyRBTreeNode prev = cur.right;
        cur.right = prev.left;
        if(prev.left != this.guard){
            prev.left.parent = cur;
        }
        prev.parent = cur.parent;
        if(cur.parent == this.guard){
            this.root = prev;
        }else if(cur == cur.parent.left){
            cur.parent.left = prev;
        }else{
            cur.parent.right = prev;
            prev.left = cur;
            cur.parent = prev;
        }
    }

    private void rightRotated(MyRBTreeNode cur){
        MyRBTreeNode prev = cur.left;
        cur.right = prev.right;
        if(prev.right != this.guard){
            prev.right.parent = cur;
        }
        prev.parent = cur.parent;
        if(cur.parent == this.guard){
            this.root = prev;
        }else if(cur == cur.parent.right){
            cur.parent.right = prev;
        }else{
            cur.parent.left = prev;
            prev.right = cur;
            cur.parent = prev;
        }
    }

    @Override
    public T delete(int key) {
        if(this.root.left == null && this.root.right == null){
            if(this.root.key == key) {
                T res = (T) this.root.data;
                this.root = null;
                return res;
            }else{
                return null;
            }
        }




        return null;
    }

    private void deleteNode(MyRBTreeNode cur){
        MyRBTreeNode temp = null;
        MyRBTreeNode prev = cur;
        boolean prevIsRed = prev.isRed;
        if(cur.left == this.guard){
            temp = cur.right;
            transplant(cur, cur.right);
        }else if(cur.right == this.guard){
            temp = cur.left;
            transplant(cur, cur.left);
        }else{
            prev = minimumNode(cur.right);
            prevIsRed = prev.isRed;
            cur = prev.right;
            if(prev.parent == cur){
                temp = prev;
            }else{
                transplant(prev, prev.right);
                prev.right = cur.right;
                prev.right.parent = prev;
            }
            transplant(cur, prev);
            prev.left = cur.left;
            prev.left.parent = prev;
            prev.isRed = cur.isRed;
        }
        if(!prevIsRed){
            deleteFixup(temp);
        }
    }

    private void deleteFixup(MyRBTreeNode cur){
        while(cur != this.root && !cur.isRed){
            if(cur == cur.parent.left){
                MyRBTreeNode temp = cur.parent.right;
                if(temp.isRed){
                    temp.isRed = false;
                    cur.parent.isRed = true;
                    leftRotated(cur.parent);
                    temp = cur.parent.right;
                }
                if(!temp.left.isRed && !temp.right.isRed){
                    temp.isRed = true;
                    cur = cur.parent;
                }else if(!temp.right.isRed){
                    temp.left.isRed = false;
                    temp.isRed = true;
                    rightRotated(temp);
                    temp = cur.parent.right;
                }
                temp.isRed = cur.parent.isRed;
                cur.parent.isRed = false;
                temp.right.isRed = false;
                leftRotated(cur.parent);
                cur = this.root;
            }else{
                MyRBTreeNode temp = cur.parent.left;
                if(temp.isRed){
                    temp.isRed = false;
                    cur.parent.isRed = true;
                    leftRotated(cur.parent);
                    temp = cur.parent.left;
                }
                if(!temp.right.isRed && !temp.left.isRed){
                    temp.isRed = true;
                    cur = cur.parent;
                }else if(!temp.left.isRed){
                    temp.right.isRed = false;
                    temp.isRed = true;
                    leftRotated(temp);
                    temp = cur.parent.left;
                }
                temp.isRed = cur.parent.isRed;
                cur.parent.isRed = false;
                temp.left.isRed = false;
                rightRotated(cur.parent);
                cur = this.root;
            }
        }
        cur.isRed = false;
        return;
    }

    private void transplant(MyRBTreeNode a, MyRBTreeNode b){
        if(a.parent == this.guard){
            this.root = b;
        }else if(a == a.parent.left){
            a.parent.left = b;
        }else {
            a.parent.right = b;
            b.parent = a.parent;
        }
    }

    private MyRBTreeNode minimumNode(MyRBTreeNode cur){
        while(cur.right != null){
            cur = cur.right;
        }
        return cur;
    }

    @Override
    public void print() {
        Queue<MyRBTreeNode> q = new LinkedList<>();
        q.offer(this.guard.left);
        while(!q.isEmpty()){
            int size = q.size();
            for(int i = 0; i < size; i++){
                MyRBTreeNode cur = q.poll();
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

    }
}

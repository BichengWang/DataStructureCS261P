package multiTrees;

import java.util.LinkedList;
import java.util.Queue;

public class MySplayTree<T> extends MyTree<T> {

    private class MyTreeNode{
        int key;
        T data;
        MyTreeNode left;
        MyTreeNode right;
        MyTreeNode parent;

        MyTreeNode(){

        }
        MyTreeNode(int key, T data){
            this.key = key;
            this.data = data;
        }

        void clear(){
            this.data = null;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }

    MyTreeNode guard;

    public MySplayTree() {
        this.guard = new MyTreeNode();
    }

    @Override
    public T search(int key) {
        MyTreeNode resNode = searchNode(key);
        if(resNode == null){
            return null;
        }
        fixup(resNode, this.guard);
        return resNode.data;
    }

    private MyTreeNode searchNode(int key){
        MyTreeNode cur = this.guard.left;
        while(cur != null){
            if(key < cur.key){
                cur = cur.left;
            }else if(cur.key < key){
                cur = cur.right;
            }else{
                return cur;
            }
        }
        return null;
    }

    @Override
    public T insert(int key) {
        MyTreeNode newNode = new MyTreeNode(key, (T)new Integer(key));
        MyTreeNode cur = this.guard.left;
        MyTreeNode prev = this.guard;

        while(cur != null){
            prev = cur;
            if(key < cur.key){
                cur = cur.left;
            }else if(cur.key < key){
                cur = cur.right;
            }else{
                return cur.data;
            }
        }

        if(prev == this.guard){
            prev.left = newNode;
            newNode.parent = prev;
            return null;
        }

        if(key < prev.key){
            prev.left = newNode;
        }else if(prev.key < key){
            prev.right = newNode;
        }else{
            System.err.println("Unexpected Error");
        }
        newNode.parent = prev;
        insertFixup(newNode);
        return null;
    }

    private void insertFixup(MyTreeNode cur){
        fixup(cur, this.guard);
        return;
    }

    private void fixup(MyTreeNode cur, MyTreeNode stopNode){
        MyTreeNode p = cur.parent;
        if(p == stopNode){
            return;
        }
        MyTreeNode pp = p.parent;
        // zig or zag
        if(pp == stopNode){
            if(p.left == cur){
                rotateRight(p);
            }else{
                rotateLeft(p);
            }
            return;
        }

        if(pp.left == p){
            if(p.left == cur){// zig zig
                rotateRight(pp);
                rotateRight(p);
//                rotateRight(p);
//                rotateRight(pp);
            }else{// zig zag
                rotateLeft(p);
                rotateRight(pp);
            }
        }else{
            if(p.right == cur){// zig zig
                rotateLeft(pp);
                rotateLeft(p);
//                rotateLeft(p);
//                rotateLeft(pp);
            }else{// zig zag
                rotateRight(p);
                rotateLeft(pp);
            }
        }
        fixup(cur, stopNode);
        return;
    }

    private void rotateLeft(MyTreeNode cur){
        MyTreeNode parent = cur.parent;
        MyTreeNode next = cur.right;

        if(parent.left == cur){
            parent.left = next;
        }else{
            parent.right = next;
        }
        next.parent = parent;

        cur.right = next.left;
        if(next.left != null){
            next.left.parent = cur;
        }

        cur.parent = next;
        next.left = cur;

        return;
    }

    private void rotateRight(MyTreeNode cur){
        MyTreeNode parent = cur.parent;
        MyTreeNode next = cur.left;

        if(parent.left == cur){
            parent.left = next;
        }else{
            parent.right = next;
        }
        next.parent = parent;

        cur.left = next.right;
        if(next.right != null){
            next.right.parent = cur;
        }

        cur.parent = next;
        next.right = cur;

        return;
    }

    @Override
    public T delete(int key) {
        MyTreeNode deleteNode = searchNode(key);
        if(deleteNode == null){
            return null;
        }
        T res = deleteNode.data;
        removeNode(deleteNode);
        return res;
    }

    private void removeNode(MyTreeNode deleteNode){
        fixup(deleteNode, this.guard);
        MyTreeNode pred = findPredecessor(deleteNode);
        if(pred == null){
            this.guard.left = deleteNode.right;
            if(deleteNode.right != null){
                deleteNode.right.parent = this.guard;
            }
            return;
        }

        // remove
        fixup(pred, deleteNode);
        this.guard.left = pred;
        pred.parent = this.guard;
        pred.right = deleteNode.right;
        if(deleteNode.right != null){
            deleteNode.right.parent = pred;
        }
        deleteNode.clear();
    }

    private MyTreeNode findPredecessor(MyTreeNode cur){
        cur = cur.left;
        if(cur == null){
            return null;
        }
        while(cur.right != null){
            cur = cur.right;
        }
        return cur;
    }


    @Override
    public void print() {
        Queue<MyTreeNode> q = new LinkedList<>();
        q.offer(this.guard.left);
        int depth = 0;
        long eNum = 0;
        while(!q.isEmpty()){
            int size = q.size();
            depth++;
            for(int i = 0; i < size; i++){
                MyTreeNode cur = q.poll();

                if(cur == null){
                    System.out.print("\t\t");
                } else {
                    eNum++;
                    System.out.print("\t" + cur.key + "\t");
                    q.offer(cur.left);
                    q.offer(cur.right);
                }
            }
            System.out.println();
        }
        System.out.println("depth: " + depth + "\telements:" + eNum);
        return;
    }

    @Override
    public void printDepthNNumber() {
        Queue<MyTreeNode> q = new LinkedList<>();
        q.offer(this.guard.left);
        int depth = 0;
        long eNum = 0;
        while(!q.isEmpty()){
            int size = q.size();
            depth++;
            for(int i = 0; i < size; i++){
                MyTreeNode cur = q.poll();
                eNum++;
                if(cur.left!=null) {
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

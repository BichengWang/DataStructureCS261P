package multiTrees;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MyTreap<T> extends MyTree<T> {
    private class MyTreeNode{
        int key;
        T data;
        Double priority;
        MyTreeNode left;
        MyTreeNode right;
        MyTreeNode parent;
        MyTreeNode(){}
        MyTreeNode(int key, T data, Double priority, MyTreeNode parent, MyTreeNode left, MyTreeNode right){
            this.key = key;
            this.data = data;
            this.priority = priority;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
        MyTreeNode(int key, Double priority){
            this.key = key;
            this.data = (T) new Integer(key);
            this.priority = priority;
        }
        void clear(){
            this.data = null;
            this.priority = null;
            this.parent = null;
            this.left = null;
            this.right = null;
        }
    }

    private MyTreeNode guard;
    private Random r;

    public MyTreap(){
        this.guard = new MyTreeNode();
        this.r = new Random();
    }

    @Override
    public T search(int key) {
        if(this.guard.left == null){
            return null;
        }
        MyTreeNode cur = this.guard.left;
        while(cur != null){
            if(key < cur.key){
                cur = cur.left;
            }else if(cur.key < key){
                cur = cur.right;
            }else{
                return cur.data;
            }
        }
        return null;
    }

    @Override
    public T insert(int key) {
        T res = search(key);
        if(res != null){
            return res;
        }

        MyTreeNode newNode = new MyTreeNode(key, this.r.nextDouble());
        if(this.guard.left == null){
            this.guard.left = newNode;
            newNode.parent = this.guard;
            return null;
        }

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

        if(key < prev.key){
            prev.left = newNode;
        }else{
            prev.right = newNode;
        }

        newNode.parent = prev;

        insertFixup(newNode);
        return null;
    }

    private void insertFixup(MyTreeNode cur){
        if(cur == this.guard.left){
            return;
        }
        MyTreeNode parent = cur.parent;
        try {
            if (parent.priority < cur.priority) {
                if (parent.left == cur) {
                    rotateRight(parent);
                } else if (parent.right == cur) {
                    rotateLeft(parent);
                }
                insertFixup(cur);
            }
        }catch(Exception e){
            print();
            System.out.println("parent: " + parent);
            System.out.println("cur: " + cur);
            if(parent != null){
                System.out.println("parent key: " + parent.key);
            }
            if(cur != null){
                System.out.println("cur key: " + cur.key);
            }

            System.out.println("Debug");
        }
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
        if(this.guard.left == null){
            return null;
        }

        MyTreeNode prev = this.guard;
        MyTreeNode cur = this.guard.left;
        while(cur != null){
            prev = cur;
            if(key < cur.key){
                cur = cur.left;
            }else if(cur.key < key){
                cur = cur.right;
            }else{
                break;
            }
        }

        // not found
        if(cur == null){
            return null;
        }

        T res = cur.data;

        while(cur.left != null && cur.right != null){
            if(cur.left.priority.compareTo(cur.right.priority) < 0){
                rotateRight(cur);
            }else{
                rotateLeft(cur);
            }
        }
        removeNode(cur);
        cur.clear();
        return res;
    }

    private void removeNode(MyTreeNode cur){
        MyTreeNode parent = cur.parent;
        if(cur.left == null){
            if(parent.left == cur){
                parent.left = cur.right;
            }else{
                parent.right = cur.right;
            }
            if(cur.right != null){
                cur.right.parent = parent;
            }
        }else if(cur.right == null){
            if(parent.left == cur){
                parent.left = cur.left;
            }else{
                parent.right = cur.left;
            }
            cur.left.parent = parent;
        }else{
            System.err.println("Unexpected Error");
        }
        return;
    }

    private void deleteFixdown(MyTreeNode cur){

    }

    @Override
    public void print() {
        Queue<MyTreeNode> q = new LinkedList<>();
        q.offer(this.guard.left);
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
        q.offer(this.guard.left);
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

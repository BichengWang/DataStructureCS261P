package multiTrees;

import java.util.LinkedList;
import java.util.Queue;

public class MyAVLTree<T> extends MyTree<T> {

    /**
     * AVL Node define
     * the balance is left depth - right depth
     */
    private class MyAVLNode {
        MyAVLNode left;
        MyAVLNode right;
        MyAVLNode parent;
        int balance = 0; // left - right
        int key;
        T data;

        public MyAVLNode() {
        }

        public MyAVLNode(int key, int value) {
            this.key = key;
            this.data = (T)new Integer(value);
        }

        public MyAVLNode(MyAVLNode left, MyAVLNode right, MyAVLNode parent, int key, T data) {
            this.left = left;
            this.right = right;
            this.parent = parent;
            this.key = key;
            this.data = data;
        }

        public void replaceBy(MyAVLNode other){
            this.key = other.key;
            this.data = other.data;
        }

        public void clear(){
            this.key = 0;
            this.data = null;
            this.left = null;
            this.right = null;
            this.parent = null;
        }
    }


    private MyAVLNode guard;
    private MyAVLNode root;

    public MyAVLTree() {
        guard = new MyAVLNode();
    }

    @Override
    public T search(int key) {
        MyAVLNode cur = this.root;
        while (cur != null) {
            if (cur.key > key) {
                cur = cur.left;
            } else if (cur.key < key) {
                cur = cur.right;
            } else {
                return (T)cur.data;
            }
        }
        return null;
    }

    @Override
    public T insert(int key) {
        if(this.root == null){
            this.root = new MyAVLNode(key, key);
            guard.left = this.root;
            this.root.parent = guard;
            return null;
        }

        MyAVLNode prev = null;
        MyAVLNode cur = this.root;
        while(cur != null){
            if(cur.key > key){
                prev = cur;
                cur = cur.left;
            }else if(cur.key < key) {
                prev = cur;
                cur = cur.right;
            }else{
                return cur.data;
            }
        }
        cur = new MyAVLNode(key,key);
        cur.parent = prev;
        if(key < prev.key){
            prev.left = cur;
        }else{
            prev.right = cur;
        }
        insertFix(prev, cur);
        this.root = this.guard.left;
        return null;
    }

    /**
     * There are four different cases, for easy to say, the grandparent node is a, parent node is b, node is c.
     * 		In LL structure,
     * 		a balance is 2, b balance is 1, c balance will keep after rotated, rotate right b node to keep balance.
     * 		In RR structure,
     * 		same as above, only balance and operation are mirror imange.
     * 		In LR structure,
     * 		a balance is 2, b balance is 1, c balance need to be consider because it would influnce after rotated balance, use rotate left b, and then, rotate right a to keep balance.
     * 		In RL structure,
     * 		same as above, only balance and operation are mirror image.
     * 		InsertFix: because of insert,
     * 		need to consider the subtree high would increase or not to decide should we keep recursive to fixup the parent tree
     * 		after use the above four operation.
     * @param prev
     * @param cur
     */
    private void insertFix(MyAVLNode prev, MyAVLNode cur){
        // terminal condition
        if(prev == this.guard){
            return;
        }
        // update prev balance
        if(prev.left == cur){
            prev.balance++;
            // already balanced
            if(prev.balance == 0){
                return;
            }
        }else{
            prev.balance--;
            // already balanced
            if(prev.balance == 0){
                return;
            }
        }

        if(cur.balance == -1 && prev.balance == -2){
            prev.balance = 0;
            cur.balance = 0;
            rotateLeft(prev);
            return;
        }else if(cur.balance == 1 && prev.balance == 2){
            prev.balance = 0;
            cur.balance = 0;
            rotateRight(prev);
            return;
        }else if(cur.balance == -1 && prev.balance == 2){
            MyAVLNode a = prev;
            MyAVLNode b = cur;
            MyAVLNode c = cur.right;
            if(c.balance == 1){
                c.balance = 0;
                b.balance = 0;
                a.balance = -1;
            }else if(c.balance == -1){
                c.balance = 0;
                b.balance = 1;
                a.balance = 0;
            }else if(c.balance == 0){
                c.balance = 0;
                b.balance = 0;
                a.balance = 0;
            }else{
                System.err.println("Unexpected error 4");
            }
            rotateLeft(b);
            rotateRight(a);
            return;
        }else if(cur.balance == 1 && prev.balance == -2){
            MyAVLNode a = prev;
            MyAVLNode b = cur;
            MyAVLNode c = cur.left;
            if(c.balance == 1){
                c.balance = 0;
                b.balance = -1;
                a.balance = 0;
            }else if(c.balance == -1){
                c.balance = 0;
                b.balance = 0;
                a.balance = 1;
            }else if(c.balance == 0){
                c.balance = 0;
                b.balance = 0;
                a.balance = 0;
            }else{
                System.err.println("Unexpected error 4");
            }
            rotateRight(b);
            rotateLeft(a);
            return;
        }else{
            if(cur.balance < 2 && cur.balance > -2 && prev.balance < 2 && prev.balance > -2) {
                insertFix(prev.parent, prev);
            }else{
                System.err.println("Unexpected Exception4");
            }
        }
        return;

    }

    private void rotateRight(MyAVLNode cur){
        MyAVLNode parent = cur.parent;
        MyAVLNode left = cur.left;
        if(parent.left == cur){
            parent.left = left;
        }else{
            parent.right = left;
        }
        left.parent = parent;

        cur.left = left.right;
        if(left.right != null){
            left.right.parent = cur;
        }

        left.right = cur;
        cur.parent = left;
    }

    private void rotateLeft(MyAVLNode cur){
        MyAVLNode parent = cur.parent;
        MyAVLNode right = cur.right;
        if(parent.left == cur){
            parent.left = right;
        }else{
            parent.right = right;
        }
        right.parent = parent;

        cur.right = right.left;
        if(right.left != null){
            right.left.parent = cur;
        }

        right.left = cur;
        cur.parent = right;
    }

    @Override
    public T delete(int key) {
        MyAVLNode cur = this.root;
        if(this.root == null){
            return null;
        }

        while(cur != null){
            if(key < cur.key){
                cur = cur.left;
            }else if(cur.key < key){
                cur = cur.right;
            }else{
                break;
            }
        }
        if(cur == null){
            return null;
        }
        T res = (T)cur.data;
        if(cur.left != null && cur.right != null) {
            if(cur.balance >= 0){
                MyAVLNode pred = findPredecessor(cur.left);
                cur.replaceBy(pred);
                cur = pred;
            }else{
                MyAVLNode succ = findSuccessor(cur.right);
                cur.replaceBy(succ);
                cur = succ;
            }
        }
        removeNode(cur);
        this.root = this.guard.left;
        return res;
    }

    private void removeNode(MyAVLNode cur){
        if(cur.left == null){
            if(cur.parent.left == cur){
                cur.parent.left = cur.right;
                cur.parent.balance--;
            }else{
                cur.parent.right = cur.right;
                cur.parent.balance++;
            }
            if(cur.right != null) {
                cur.right.parent = cur.parent;
            }
        } else if(cur.right == null){
            if(cur.parent.left == cur){
                cur.parent.left = cur.left;
                cur.parent.balance--;
            }else{
                cur.parent.right = cur.left;
                cur.parent.balance++;
            }
            cur.left.parent = cur.parent;
        }else{
            System.err.println("error calling convention");
        }
        deleteFixup(cur.parent);
        cur.clear();
        return;
    }

    /**
     * adapt cur balance first
     * @param cur
     */
    private void deleteFixup(MyAVLNode cur){
        if(cur == this.guard){
            this.guard.balance = 0;
            this.root = this.guard.left;
            return;
        }
        if(cur.balance == 1 || cur.balance == -1){
            return;
        }
        if(cur.balance == 0) {
            if (cur == cur.parent.left) {
                cur.parent.balance--;
            } else {
                cur.parent.balance++;
            }
            cur = cur.parent;
            deleteFixup(cur);
            return;
        }

        MyAVLNode a = cur;
        if(cur.balance == 2){

                MyAVLNode b = cur.left;
                if (cur.left.balance == 1) {
                    a.balance = 0;
                    b.balance = 0;
                    rotateRight(a);
                    cur = b;
                } else if (cur.left.balance == -1) {
                    MyAVLNode c = cur.left.right;
                    if (c.balance == 1) {
                        a.balance = -1;
                        b.balance = 0;
                    } else if (c.balance == -1) {
                        a.balance = 0;
                        b.balance = 1;
                    } else if (c.balance == 0) {
                        a.balance = 0;
                        b.balance = 0;
                    }
                    c.balance = 0;
                    rotateLeft(b);
                    rotateRight(a);
                    cur = c;
                } else if (cur.left.balance == 0) {
                    a.balance = 1;
                    b.balance = -1;
                    // not decrease high, so stop recursive
                    rotateRight(a);
                    return;
                } else {
                    System.out.println("Unexpected Exception 5");
                }
        }else if(cur.balance == -2){
                MyAVLNode b = cur.right;
                if (cur.right.balance == -1) {
                    a.balance = 0;
                    b.balance = 0;
                    rotateLeft(a);
                    cur = b;
                } else if (cur.right.balance == 1) {
                    MyAVLNode c = cur.right.left;
                    if (c.balance == 1) {
                        a.balance = 0;
                        b.balance = -1;
                    } else if (c.balance == -1) {
                        a.balance = 1;
                        b.balance = 0;
                    } else if (c.balance == 0) {
                        a.balance = 0;
                        b.balance = 0;
                    }
                    c.balance = 0;
                    rotateRight(b);
                    rotateLeft(a);
                    cur = c;
                } else if (cur.right.balance == 0) {
                    a.balance = -1;
                    b.balance = 1;
                    // not decrease high, so and stop recursive
                    rotateLeft(a);
                    return;
                } else {
                    System.out.println("Unexpected Exception 5");
                }
        }else{
            System.err.println("Unexpected Exception 3");
        }
        if(cur.parent == guard){
            return;
        }
        if(cur.parent.left == cur){
            cur.parent.balance--;
        }else{
            cur.parent.balance++;
        }
        deleteFixup(cur.parent);
        return;
    }

    /**
     * find a predecessor, attention the input is the left
     * @param left
     * @return predecessor
     */
    private MyAVLNode findPredecessor(MyAVLNode left){
        while(left.right != null){
            left = left.right;
        }
        return left;
    }

    /**
     * find a successor, the input is right
     * @param right
     * @return
     */
    private MyAVLNode findSuccessor(MyAVLNode right){
        while(right.left != null){
            right = right.left;
        }
        return right;
    }

    @Override
    public void print(){
        Queue<MyAVLNode> q = new LinkedList<>();
        q.offer(this.root);
        while(!q.isEmpty()){
            int size = q.size();
            for(int i = 0; i < size; i++){
                MyAVLNode cur = q.poll();
                if(cur == null){
                    System.out.print("\tnull\t");
                } else {
                    System.out.print("\t" + cur.key + " b:" + cur.balance + "\t");
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
        Queue<MyAVLNode> q = new LinkedList<>();
        q.offer(this.root);
        int depth = 0;
        long eNum = 0;
        while(!q.isEmpty()){
            int size = q.size();
            depth++;
            for(int i = 0; i < size; i++){
                MyAVLNode cur = q.poll();
                    eNum++;
                    if(cur.left != null) {
                        q.offer(cur.left);
                    }
                    if(cur.right != null){
                        q.offer(cur.right);
                    }
            }
        }
        System.out.println("depth: " + depth + "\telements: " + eNum);
        return;
    }

}

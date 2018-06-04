package multiTrees;

import java.util.*;

public class TreeTest {

    private int[] randomKey;
    private int[] randomOperation;
    private int[] randomOpKey;

    public TreeTest() {
    }

    public static void main(String[] args) {

        TreeTest test = new TreeTest();
        test.testMyTree();
        // for myself testing
//        test.deleteTesting(elementsNum);
//        test.init2();
//        test.testing(new MyAVLTree<>(), true);
    }

    /**
     * testing body
     * TODO: please replace class name which you want to test
     */
    public void testMyTree() {
        Class<?> myTreeClass = new MyAVLTree<Integer>().getClass();
        int[] eNums = new int[10];
        for(int i = 0; i < 10; i++){
            eNums[i] = (i + 1) * 10000;
        }
        try {
            System.out.println("random insert tree depth");
            this.insertTesting(100000);
            showDepth((MyTree<Integer>) myTreeClass.newInstance());
            this.inorderInsertInit(100000);
            System.out.println("order insert tree depth");
            showDepth((MyTree<Integer>) myTreeClass.newInstance());

            for(int k = 0; k < eNums.length; k++){
                int elementsNum = eNums[k];
                // random testing
                long insertTime = 0;
                long deleteTime = 0;
                long searchTime = 0;
                long randomTime = 0;
                long inorderInsertTime = 0;
                long inorderDeleteTime = 0;
                long inorderSearchTime = 0;
                int time = 10;

                for (int i = 0; i < time; i++) {
                    this.insertTesting(elementsNum);
                    insertTime += testing((MyTree<Integer>) myTreeClass.newInstance(), true);
                    this.deleteTesting(elementsNum);
                    deleteTime += testing((MyTree<Integer>) myTreeClass.newInstance(), false);
                    this.searchTesting(elementsNum);
                    searchTime += testing((MyTree<Integer>) myTreeClass.newInstance(), false);
                    this.randomTesting(elementsNum);
                    randomTime += testing((MyTree<Integer>) myTreeClass.newInstance(), false);
                    this.inorderInsertInit(elementsNum);
                    inorderInsertTime += testing((MyTree<Integer>) myTreeClass.newInstance(), true);
                    this.inorderDeleteInit(elementsNum);
                    inorderDeleteTime += testing((MyTree<Integer>) myTreeClass.newInstance(), false);
                    this.inorderSearchInit(elementsNum);
                    inorderSearchTime += testing((MyTree<Integer>) myTreeClass.newInstance(), false);
                }

                System.out.println("eNum: " + "\tinsert\tdelete\tsearch\trandom\torderIn\torderDe\torderSe");
                System.out.println(elementsNum + "\t" +
                        (insertTime / time) + "\t" +
                        (deleteTime / time) + "\t" +
                        (searchTime / time) + "\t" +
                        (randomTime / time) + "\t" +
                        (inorderInsertTime / time) + "\t" +
                        (inorderDeleteTime / time) + "\t" +
                        (inorderSearchTime / time)
                );
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return;
    }

    public void inorderInsertInit(int n){
        randomKey = new int[n];
        randomOperation = new int[0];
        randomOpKey = new int[0];
        for(int i = 0; i < n; i++){
            randomKey[i] = i;
        }
        return;
    }

    public void inorderDeleteInit(int n){
        randomKey = new int[n];
        randomOperation = new int[n];
        randomOpKey = new int[n];
        for(int i = 0; i < n; i++){
            randomKey[i] = i;
            randomOperation[i] = 1;
            randomOpKey[i] = n - i - 1;
        }
        return;
    }

    public void inorderSearchInit(int n){
        randomKey = new int[n];
        randomOperation = new int[n];
        randomOpKey = new int[n];
        for(int i = 0; i < n; i++){
            randomKey[i] = i;
            randomOperation[i] = 2;
            randomOpKey[i] = n - i - 1;
        }
        return;
    }

    public void insertTesting(int n){
        Random r = new Random();
        randomKey = new int[n];
        randomOperation = new int[0];
        randomOpKey = new int[0];
        for(int i = 0; i < n; i++){
            randomKey[i] = r.nextInt();
        }
        return;
    }

    public void deleteTesting(int n){
        Random r = new Random();
        randomKey = new int[n];
        randomOperation = new int[3*n];
        randomOpKey = new int[3*n];
        for(int i = 0; i < n; i++){
            randomKey[i] = r.nextInt();
        }
        for(int i = 0; i < 3 * n; i++){
            randomOperation[i] = 1;
            randomOpKey[i] = randomKey[r.nextInt(n)];
        }
        return;
    }

    public void searchTesting(int n){
        Random r = new Random();
        randomKey = new int[n];
        randomOperation = new int[3*n];
        randomOpKey = new int[3*n];
        for(int i = 0; i < n; i++){
            randomKey[i] = r.nextInt();
        }
        for(int i = 0; i < 3 * n; i++){
            randomOperation[i] = 2;
            randomOpKey[i] = randomKey[r.nextInt(n)];
        }
        return;
    }

    public void randomTesting(int n) {
        Random r = new Random();
        randomKey = new int[n];
        randomOperation = new int[3 * n];
        randomOpKey = new int[3 * n];
        for (int i = 0; i < n; i++) {
            randomKey[i] = r.nextInt();
        }
        for (int i = 0; i < 3 * n; i++) {
            randomOperation[i] = r.nextInt(3);
            randomOpKey[i] = randomKey[r.nextInt(n)];
        }
    }

    /**
     * for myself testing
     */
    private void init2(){
        randomKey = new int[]{169,115,188,61,140,177,191,20,90,127,153,171,190,193,3,29,102,124};
        randomOpKey = new int[]{193,20,193,171,188,190,90,102,61,102,190,191,20,177};
        randomOperation = new int[randomOpKey.length];
        for(int i = 0; i < randomOperation.length; i++){
            randomOperation[i] = 1;
        }
    }

    private long testing(MyTree<Integer> tree, boolean isInsert){
        Set<Integer> set = new HashSet<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < randomKey.length; i++) {
            tree.insert(randomKey[i]);
            set.add(randomKey[i]);
        }
        long insertTamp = System.currentTimeMillis();
        if(isInsert){
//            System.out.println("operation time: " + (insertTamp - start) + " ms");
            return insertTamp - start;
        }
//        tree.print();
        for (int i = 0; i < randomOperation.length; i++) {
            switch (randomOperation[i]) {
                case 0:
                    insert(set, tree, randomOpKey[i]);
                    break;
                case 1:
                    delete(set, tree, randomOpKey[i]);
                    break;
                case 2:
                    search(set, tree, randomOpKey[i]);
                    break;
            }
        }
        long end = System.currentTimeMillis();
//        System.out.println("operation time: " + (end - insertTamp) + " ms");
        return end - insertTamp;
    }
    private void insert(Set<Integer> set, MyTree<Integer> tree, int key) {
//        System..println("Insert target: " + key);
        set.add(key);
        tree.insert(key);
        return;
    }

    private void delete(Set<Integer> set, MyTree<Integer> tree, int key) {
//        System.out.println("Delete target: " + key);
        if((set.remove(key)) != (tree.delete(key) != null)){
            System.out.println("Error");
        }
//        tree.print();
    }

    private void search(Set<Integer> set, MyTree<Integer> tree, int key) {
        if (set.contains(key) != (tree.search(key) != null)) {
            System.err.println("search error");
            System.err.println("key: " + key + " set: " + set.contains(key) + " tree:" + (tree.search(key) != null));
        }
    }

    private void showDepth(MyTree<Integer> tree){
        int temp = 64;
        for (int i = 0; i < randomKey.length; i++) {
            tree.insert(randomKey[i]);
            if(i % temp == 0){
                temp *= 2;
                tree.printDepthNNumber();
            }
        }
    }
}

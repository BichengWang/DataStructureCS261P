package multiTrees;

public abstract class MyTree<T>{

    public abstract T search(int key);

    public abstract T insert(int key);

    public abstract T delete(int key);

    public abstract void print();

    public abstract void printDepthNNumber();
}


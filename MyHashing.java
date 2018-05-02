package hashingClasses;

public abstract class MyHashing<K, V> {

	abstract V put(K key, V value);
	
	abstract V get(K key);
	
	abstract V remove(K key);
}

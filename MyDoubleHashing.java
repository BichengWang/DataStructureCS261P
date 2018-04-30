package hashingClasses;

/**
 * double hashing class, support put, get, remove
 * @version 1.0
 * @author wangbicheng
 *
 * @param <K>
 * @param <V>
 */
public class MyDoubleHashing<K, V> {

	private int k = 5;
	private double factor = 0.1;
	
	private int times = 4;

	private int hashMod = 31;
	private int capacity = 10;

	private MyHashPair[] elements;
	private int size = 0;

	public MyDoubleHashing() {
		this.elements = new MyHashPair[capacity];
	}
	
	public MyDoubleHashing(double factor, int k) {
		this.elements = new MyHashPair[capacity];
		this.factor = factor;
		this.k = k;
	}

	public int hashCode(K key) {
		return (key.hashCode() % capacity + capacity) % capacity;
	}
	
	public int hashCode2(K key) {
		return hashMod;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}
	
	public int capacity() {
		return capacity;
	}

	/**
	 * check if it needs rehashing, rehashing.
	 * check if there is empty, place element in it.
	 * else check the next place which position is hash1() + (n - 1) hash2()
	 * if not empty, place it. 
	 * else rehashing, and put element again.
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public V put(K key, V value) {
		if(size > capacity * factor) {
			rehashing();
		}
		int code = hashCode(key);
		// linear probing
		int next = code;
		for(int i = 0; i < times; i++) {
			// try to insert
			if (this.elements[next] == null) {
				MyHashPair mhp = new MyHashPair(key, value);
				this.elements[next] = mhp;
				size++;
				return null;
			}
			// key exist
			if (this.elements[next].getKey().equals(key)) {
				V oldV = (V) this.elements[next].getValue();
				this.elements[next].setValue(value);
				return oldV;
			}
			next = (next + hashCode2(key)) % capacity;
		}
		rehashing();
		return put(key, value);
	}

	/**
	 * get element
	 * check if there exists the element, return element
	 * else check the next element shift by hashCode2(key)
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public V get(K key) {
		if(key == null)
			return null;
		int code = hashCode(key);
		int next = code;
		// linear probing
		for(int i = 0; i < times; i++) {
			// key exist
			if (this.elements[next] != null && this.elements[next].getKey().equals(key)) {
				return (V) this.elements[next].getValue();
			}
			next = (next + hashCode2(key)) % capacity;
		}
		return null;
	}

	/**
	 * remove element
	 * check if there exists the element, remove element
	 * else check the next element shift by hashCode2(key), remove element
	 * @param key
	 * @return oldValue
	 */
	@SuppressWarnings("unchecked")
	public V remove(K key) {
		if(key == null)
			return null;
		int code = hashCode(key);
		int next = code;
		// linear probing
		for(int i = 0; i < times; i++) {
			// key exist
			if (this.elements[next] != null && this.elements[next].getKey().equals(key)) {
				V oldV = (V) this.elements[next].getValue();
				this.elements[next] = null;
				size--;
				return oldV;
			}
			next = (next + hashCode2(key)) % capacity;
		}
		return null;
	}
	
	/**
	 * rehashing from old elements put into new elements
	 */
	@SuppressWarnings("unchecked")
	private void rehashing() {
		MyHashPair[] oldElements = this.elements;
		this.capacity *= this.k;
		this.elements = new MyHashPair[this.capacity];
		this.size = 0;
		for(int i = 0; i < oldElements.length; i++) {
			if(oldElements[i] == null)
				continue;
			MyHashPair cur = oldElements[i];
			oldElements[i] = null;
			put((K)cur.getKey(), (V)cur.getValue());
		}
		return;
	}
}

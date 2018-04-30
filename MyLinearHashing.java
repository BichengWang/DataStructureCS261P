package hashingClasses;

/**
 * Linear hashing class, support put, get, remove
 * @version 1.0
 * @author wangbicheng
 *
 * @param <K> key
 * @param <V> value
 */
public class MyLinearHashing<K, V> {
	private int k = 5;
	private double factor = 0.1;

	private int hashMod = 31;
	private int capacity = 10;

	private MyHashPair[] elements;
	private int size = 0;

	public MyLinearHashing() {
		this.elements = new MyHashPair[capacity];
	}
	
	public MyLinearHashing(double factor, int k) {
		this.elements = new MyHashPair[capacity];
		this.factor = factor;
		this.k = k;
	}

	public int hashCode(K key) {
		return (key.hashCode() % capacity + capacity) % capacity;
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
	 * check if it needs rehashing.
	 * check if it possibly put element into bucket. If not, find the next empty bucket to put into.
	 * @param key
	 * @param value
	 * @return oldValue
	 */
	@SuppressWarnings("unchecked")
	public V put(K key, V value) {
		if(size > capacity * factor)
			rehashing();
		int code = hashCode(key);
		// linear probing
		int next = code;
		do {
			// try to insert
			if (this.elements[next] == null) {
				MyHashPair mhp = new MyHashPair(key, value);
				this.elements[next] = mhp;
				size++;
				return null;
			}

			// key exist
			if (key.equals(this.elements[next].getKey())) {
				V oldV = (V) this.elements[next].getValue();
				this.elements[next].setValue(value);
				return oldV;
			}
			next = (next + 1) % capacity;
		} while (next != code);
		return null;
	}

	/**
	 * get key, from hash position, if no element, check the next until one total loop
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public V get(K key) {
		int code = hashCode(key);
		int next = code;
		// linear probing
		do {
			// key exist
			if (this.elements[next] != null && this.elements[next].getKey().equals(key)) {
				return (V) this.elements[next].getValue();
			}
			next = (next + 1) % capacity;
		} while (next != code);
		return null;
	}

	/**
	 * remove an element, from hash position, if not exist, check the next until one total loop.
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public V remove(K key) {
		int code = hashCode(key);
		int next = code;
		do {
			if (this.elements[next] != null && key.equals(this.elements[next].getKey())) {
				V oldV = (V) this.elements[next].getValue();
				this.elements[next] = null;
				size--;
				return oldV;
			}
			next = (next + 1) % capacity;
		} while (next != code);
		return null;
	}
	
	/**
	 * rehashing the total table, time by this.k and reinsert elements one by one
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

class MyHashPair {
	protected Object key;
	protected Object value;

	public MyHashPair(Object key, Object value) {
		this.key = key;
		this.value = value;
	}

	public Object getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}

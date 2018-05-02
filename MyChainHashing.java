package hashingClasses;

/**
 * chain hash class, support chain hash structure, put, get, remove
 * @version 1.0
 * @author wangbicheng
 *
 * @param <K>
 * @param <V>
 */
public class MyChainHashing<K, V> extends MyHashing<K, V>{
	private int k = 5;
	private double factor = 0.1;

	private int hashMod = 31;
	private int capacity = 10;

	private MyChainHashPair[] elements;
	private int size = 0;
	
	public MyChainHashing(){
		this.elements = new MyChainHashPair[capacity];
	}
	public MyChainHashing(double factor, int k){
		this.elements = new MyChainHashPair[capacity];
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
	 * put element into hash code position, if exist other element, place it to the next
	 * @param key
	 * @param value
	 * @return oldValue
	 */
	@SuppressWarnings("unchecked")
	public V put(K key, V value) {
		if(this.size > capacity * factor)
			rehashing();
		int code = hashCode(key);
		if(this.elements[code] == null) {
			MyChainHashPair pair = new MyChainHashPair(key, value, null);
			this.elements[code] = pair;
			size++;
			return null;
		}
		MyChainHashPair pair = this.elements[code];
		MyChainHashPair prev = pair;
		pair = pair.getNext();
		while(pair != null) {
			if(pair.getKey().equals(key)) {
				V oldV = (V) pair.getValue();
				pair.setValue(value);
				return oldV;
			}
			prev = pair;
			pair = pair.getNext();
		}
		pair = new MyChainHashPair(key, value, null);
		prev.setNext(pair);
		size++;
		return null;
	}
	
	/**
	 * check the bucket if it exist the key value pair, if not, check the next, until the tail
	 * @param key
	 * @return value
	 */
	@SuppressWarnings("unchecked")
	public V get(K key) {
		int code = hashCode(key);
		MyChainHashPair pair = this.elements[code];
		while(pair != null) {
			if(pair.getKey().equals(key)) {
				return (V) pair.getValue();
			}
			pair = pair.getNext();
		}
		return null;
	}
	
	/**
	 * remove element, if not at the bucket, check the next element, until the tail
	 * @param key
	 * @return value
	 */
	@SuppressWarnings("unchecked")
	public V remove(K key) {
		int code = hashCode(key);
		MyChainHashPair pair = this.elements[code];
		MyChainHashPair prev = null;
		while(pair != null) {
			if(pair.getKey().equals(key)) {
				V oldV = (V) pair.getValue();
				if(prev == null) {
					this.elements[code] = pair.getNext();
				} else {
					prev.setNext(pair.getNext());
				}
				pair.setNext(null);
				size--;
				return oldV;
			}
			prev = pair;
			pair = pair.getNext();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void rehashing() {
		MyChainHashPair[] oldElements = this.elements;
		this.capacity *= this.k;
		this.elements = new MyChainHashPair[this.capacity];
		this.size = 0;
		for(int i = 0; i < oldElements.length; i++) {
			if(oldElements[i] == null)
				continue;
			MyChainHashPair cur = oldElements[i];
			oldElements[i] = null;
			while(cur != null) {
				MyChainHashPair next = cur.getNext();
				cur.setNext(null);
				put((K)cur.getKey(), (V)cur.getValue());
				cur = next;
			}
		}
		return;
	}
}

class MyChainHashPair extends MyHashPair{
	private MyChainHashPair next;
	public MyChainHashPair(Object key, Object value, MyChainHashPair next) {
		super(key, value);
		this.next = next;
	}
	public MyChainHashPair getNext() {
		return next;
	}
	public void setNext(MyChainHashPair next) {
		this.next = next;
	}
}

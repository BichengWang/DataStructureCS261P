package hashingClasses;

/**
 * cuckoo hashing class, support cuckoo hashing put, get, remove
 * @version 1.0
 * @author wangbicheng
 *
 * @param <K>
 * @param <V>
 */
public class MyCuckooHashing<K, V> extends MyHashing<K, V> {

	private double factor = 0.1;
	private int hashMod = 31;
	private int capacity1 = 5;
	private int capacity2 = 5;
	private int shift = 31;
	private int k = 5;
	
	private int size = 0;
	
	private int times = 10;
	
	private MyHashPair[] elements1;
	private MyHashPair[] elements2;
	
	public MyCuckooHashing(){
		this.elements1 = new MyHashPair[capacity1];
		this.elements2 = new MyHashPair[capacity2];
	}
	public MyCuckooHashing(double factor, int k){
		this.elements1 = new MyHashPair[capacity1];
		this.elements2 = new MyHashPair[capacity2];
		this.factor = factor;
		this.k = k;
	}
	public MyCuckooHashing(double factor, int k, int times){
		this.elements1 = new MyHashPair[capacity1];
		this.elements2 = new MyHashPair[capacity2];
		this.factor = factor;
		this.k = k;
		this.times = times;
	}
	
	public int hashCode1(K key) {
		return (key.hashCode() % capacity1 + capacity1) % capacity1;
	}
	
	public int hashCode2(K key) {
		return ((key.hashCode() / capacity2) % capacity2 + capacity2) % capacity2;
	}
	
	public int size() {
		return size;
	}
	
	public int capacity() {
		return capacity1 + capacity2;
	}
	
	/**
	 * check if it needs rehashing,
	 * check hash table1 or table2 exist the same key, if exist, replace;
	 * check if table1 or table2 is empty, if empty, place element
	 * check if table1's exist element can move forward and table2's exist element can move forward, 
	 * if yes, place the element into table, and move forward the other elements one by one.
	 * @param key
	 * @param value
	 * @return oldValue
	 */
	@SuppressWarnings("unchecked")
	public V put(K key, V value) {
		if(size > (capacity1 + capacity2) * factor) {
			rehashing();
		}
		int code1 = hashCode1(key);
		int code2 = hashCode2(key);
		if(this.elements1[code1] != null && this.elements1[code1].equals(key)) {
			V v = (V) this.elements1[code1].getValue();
			this.elements1[code1].setValue(value);
			return v;
		}else if(this.elements2[code2] != null && this.elements2[code2].equals(key)) {
			V v = (V) this.elements2[code2].getValue();
			this.elements2[code2].setValue(value);
			return v;
		}
		
		if(this.elements1[code1] == null) {
			size++;
			this.elements1[code1] = new MyHashPair(key, value);
			return null;
		}
		if(this.elements2[code2] == null) {
			size++;
			this.elements2[code2] = new MyHashPair(key, value);
			return null;
		}
		if(probe1(code1)) {
			return insert1(key, value);
		}
		if(probe2(code2)) {
			return insert2(key, value);
		}
		// probe
		rehashing();
		return put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	private boolean probe1(int code1) {
		MyHashPair cur = this.elements1[code1];
		for(int i = 0; i < times; i++) {
			int code2 = hashCode2((K) cur.getKey());
			cur = this.elements2[code2];
			if(cur == null) {
				return true;
			}
			code1 = hashCode1((K) cur.getKey());
			cur = this.elements1[code1];
			if(cur == null) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean probe2(int code2) {
		MyHashPair cur = this.elements2[code2];
		for(int i = 0; i < times; i++) {
			int code1 = hashCode1((K) cur.getKey());
			cur = this.elements1[code1];
			if(cur == null) {
				return true;
			}
			code2 = hashCode2((K) cur.getKey());
			cur = this.elements2[code2];
			if(cur == null) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private V insert1(K key, V value) {
		V res = null;
		int code1 = hashCode1(key);
		MyHashPair cur = this.elements1[code1];
		size++;
		this.elements1[code1] = new MyHashPair(key, value);
		
		while(cur != null) {
			int code2 = hashCode2((K) cur.getKey());
			MyHashPair next = this.elements2[code2];
			this.elements2[code2] = cur;
			cur = next;
			if(cur == null) {
				break;
			}
			
			code1 = hashCode1((K) cur.getKey());
			next = this.elements1[code1];
			this.elements1[code1] = cur;
			cur = next;
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	private V insert2(K key, V value) {
		V res = null;
		int code2 = hashCode2(key);
		MyHashPair cur = this.elements2[code2];
		size++;
		this.elements2[code2] = new MyHashPair(key, value);
		while(cur != null) {
			int code1 = hashCode1((K) cur.getKey());
			MyHashPair next = this.elements1[code1];
			this.elements1[code1] = cur;
			cur = next;
			if(cur == null) {
				break;
			}
			
			code2 = hashCode2((K) cur.getKey());
			next = this.elements2[code2];
			this.elements2[code2] = cur;
			cur = next;
			if(cur == null) {
				break;
			}
		}
		return res;
	}
	
	/**
	 * check if table1 or table2 exists the element
	 * @param key
	 * @return value
	 */
	@SuppressWarnings("unchecked")
	public V get(K key) {
		int code1 = hashCode1(key);
		int code2 = hashCode2(key);
		if(this.elements1[code1] != null && this.elements1[code1].getKey().equals(key)) {
			return (V) this.elements1[code1].getValue();
		}
		if(this.elements2[code2] != null && this.elements2[code2].getKey().equals(key)) {
			return (V) this.elements2[code2].getValue();
		}
		return null;
	}
	
	/**
	 * check if table1 or table2 exist element, if yes, remove it
	 * @param key
	 * @return oldValue
	 */
	@SuppressWarnings("unchecked")
	public V remove(K key) {
		
		int code1 = hashCode1(key);
		int code2 = hashCode2(key);
		if(this.elements1[code1] != null && this.elements1[code1].getKey().equals(key)) {
			MyHashPair oldPair = this.elements1[code1];
			this.elements1[code1] = null;
			size--;
			return (V) oldPair.getValue();
		}
		if(this.elements2[code2] != null && this.elements2[code2].getKey().equals(key)) {
			MyHashPair oldPair = this.elements2[code2];
			this.elements2[code2] = null;
			size--;
			return (V) oldPair.getValue();
		}
		return null;
	}
	
	/**
	 * rehashing the table1 and table2 into new table1 and table2
	 */
	@SuppressWarnings("unchecked")
	private void rehashing() {
		MyHashPair[] oldElements1 = this.elements1;
		MyHashPair[] oldElements2 = this.elements2;
		this.capacity1 *= this.k;
		this.capacity2 *= this.k;
		this.elements1 = new MyHashPair[this.capacity1];
		this.elements2 = new MyHashPair[this.capacity2];
		for(int i = 0; i < oldElements1.length; i++) {
			if(oldElements1[i] == null)
				continue;
			MyHashPair cur = oldElements1[i];
			oldElements1[i] = null;
			int code1 = hashCode1((K) cur.getKey());
			put((K)cur.getKey(), (V)cur.getValue());
		}
		
		for(int i = 0; i < oldElements2.length; i++) {
			if(oldElements2[i] == null)
				continue;
			MyHashPair cur = oldElements2[i];
			oldElements2[i] = null;
			int code2 = hashCode2((K) cur.getKey());
			put((K)cur.getKey(), (V)cur.getValue());
		}
		
		return;
	}
}

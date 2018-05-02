package hashingClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestHashing {

	// total insert elements
	static int N = 50000;
	
	// increasing factor
	static double factor = 0.75;
	
	// compare map
	static Map<Double, Integer> map;
	
	// rehashing times
	static int k = 5;

	public static void main(String[] args) {
		init();
		long[][] res = new long[10][4];
		
		N = 50000;
		k = 5;
		factor = 0.75;
		for (int i = 1; i < 10; i++) {
			factor = 0.1 * i;
			res[i][0] = testMyHashing(new MyLinearHashing<>(factor, k));
			res[i][1] = testMyHashing(new MyChainHashing<>(factor, k));
			res[i][2] = testMyHashing(new MyCuckooHashing<>(factor, k));
			res[i][3] = testMyHashing(new MyDoubleHashing<>(factor, k));
			System.out.println("factor: 0." + i + "\t" + res[i][0] + "\t" + res[i][1] + "\t" + res[i][2] + "\t" + res[i][3]);
		}
		
		N = 50000;
		k = 5;
		factor = 0.75;
		for (int i = 1; i < 10; i++) {
			k = i + 1;
			res[i][0] = testMyHashing(new MyLinearHashing<>(factor, k));
			res[i][1] = testMyHashing(new MyChainHashing<>(factor, k));
			res[i][2] = testMyHashing(new MyCuckooHashing<>(factor, k));
			res[i][3] = testMyHashing(new MyDoubleHashing<>(factor, k));
			System.out.println("k: " + k + "\t" + res[i][0] + "\t" + res[i][1] + "\t" + res[i][2] + "\t" + res[i][3]);
		}
	}
	
	private static void init() {
		Random r = new Random();
		map = new HashMap<Double, Integer>();
		for (int i = 0; i < N; i++) {
			map.put(r.nextDouble(), r.nextInt());
		}
	}

	private static long testMyHashing(MyHashing<Double, Integer> mh) {
		long res = 0;
		for (int i = 0; i < 10; i++) {
			long start = System.currentTimeMillis();
			for (Map.Entry<Double, Integer> en : map.entrySet()) {
				mh.put(en.getKey(), en.getValue());
			}
			for (Map.Entry<Double, Integer> en : map.entrySet()) {
				if (!en.getValue().equals(mh.get(en.getKey()))) {
					System.out.println("Value Exception");
				}
			}
			for (Map.Entry<Double, Integer> en : map.entrySet()) {
				if (!en.getValue().equals(mh.remove(en.getKey()))) {
					System.out.println("Remove Exception");
				}
			}
			long end = System.currentTimeMillis();
			res += end - start;
		}
		return res / 10;
	}

}

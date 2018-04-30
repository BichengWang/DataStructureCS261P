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
		// init()
		Random r = new Random();
		map = new HashMap<Double, Integer>();
		for (int i = 0; i < N; i++) {
			map.put(r.nextDouble(), r.nextInt());
		}
		long[][] res = new long[10][4];
		for (int i = 1; i < 10; i++) {
			factor = 0.1 * i;
			res[i][0] = testMyLinearHashing();
			res[i][1] = testMyChainHashing();
			res[i][2] = testMyCuckooHashing();
			res[i][3] = testMyDoubleHashing();
			System.out.println("factor: " + factor + res[i][0] + "\t" + res[i][1] + "\t" + res[i][2] + "\t" + res[i][3]);
		}
		for (int i = 1; i < 10; i++) {
			k = i + 1;
			res[i][0] = testMyLinearHashing();
			res[i][1] = testMyChainHashing();
			res[i][2] = testMyCuckooHashing();
			res[i][3] = testMyDoubleHashing();
			System.out.println("k: " + k + res[i][0] + "\t" + res[i][1] + "\t" + res[i][2] + "\t" + res[i][3]);
		}
	}

	private static long testMyLinearHashing() {
		long res = 0;
		for (int i = 0; i < 10; i++) {
			long start = System.currentTimeMillis();
			MyLinearHashing<Double, Integer> mh = new MyLinearHashing<>(factor, k);
			for (Map.Entry<Double, Integer> en : map.entrySet()) {
				mh.put(en.getKey(), en.getValue());
			}

			for (Map.Entry<Double, Integer> en : map.entrySet()) {
				if (!en.getValue().equals(mh.get(en.getKey()))) {
					System.out.println(mh.get(en.getKey()));
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

	private static long testMyChainHashing() {
		long res = 0;
		for (int i = 0; i < 10; i++) {
			long start = System.currentTimeMillis();
			MyChainHashing<Double, Integer> mh = new MyChainHashing<>(factor, k);
			for (Map.Entry<Double, Integer> en : map.entrySet()) {
				mh.put(en.getKey(), en.getValue());
			}
			for (Map.Entry<Double, Integer> en : map.entrySet()) {
				if (!en.getValue().equals(mh.get(en.getKey()))) {
					System.out.println(mh.get(en.getKey()));
					System.out.println(en.getValue());
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

	private static long testMyCuckooHashing() {
		long res = 0;
		for (int i = 0; i < 10; i++) {
			long start = System.currentTimeMillis();
			MyCuckooHashing<Double, Integer> mh = new MyCuckooHashing<>(factor, k);
			for (Map.Entry<Double, Integer> en : map.entrySet()) {
				mh.put(en.getKey(), en.getValue());
			}
			for (Map.Entry<Double, Integer> en : map.entrySet()) {
				if (!en.getValue().equals(mh.get(en.getKey()))) {
					System.out.println(mh.get(en.getKey()));
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

	private static long testMyDoubleHashing() {
		long res = 0;
		for (int i = 0; i < 10; i++) {
			long start = System.currentTimeMillis();
			MyDoubleHashing<Double, Integer> mh = new MyDoubleHashing<>(factor, k);
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

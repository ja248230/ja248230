import java.util.*;
public class QuadTest
{
	public static void main(String[] args) {
		Random gen = new Random();
		Runtime runtime = Runtime.getRuntime();
		//Runtime mem = new Runtime();
		int iter = 1000;
		int mink = gen.nextInt(100);
		int maxk = mink + iter;
		QuadHash<Integer> map = new QuadHash<>(iter);
	
		int muayThaiMaster = 0;
        runtime.gc();
		long before = runtime.totalMemory() - runtime.freeMemory();
        double start = System.nanoTime();
		while(muayThaiMaster < iter)
		{
			int rand = gen.nextInt((maxk - mink) + 1) + mink;
			//   System.out.println("key: " + rand);
			int num = gen.nextInt();
			if(map.insert(rand, num) != null)
				muayThaiMaster++;
		}
			double end = System.nanoTime();
             runtime.gc();
		long after = runtime.totalMemory() - runtime.freeMemory();
		long usedBytes = after - before;
		System.out.println("Insert Memory: " + usedBytes + " bytes");
			 System.out.println("Insert time: " + (end-start)/1000000 + "ms");
              runtime.gc();
		before = runtime.totalMemory() - runtime.freeMemory();
			start = System.nanoTime();
			for(int i = 0; i < iter; i++)
			map.search(gen.nextInt((maxk - mink) + 1 ) + mink);
			end = System.nanoTime();
            runtime.gc();
		 after = runtime.totalMemory() - runtime.freeMemory();
		 usedBytes = after - before;
		System.out.println("Search Memory: " + usedBytes + " bytes");
			System.out.println("Search time: " + (end-start)/1000000 + "ms");
            runtime.gc();
		before = runtime.totalMemory() - runtime.freeMemory();
			start = System.nanoTime();
			for(int i = 0; i < iter; i++){
			    int rand = gen.nextInt((maxk - mink) + 1) + mink;
			    map.delete(rand);
			}
			end = System.nanoTime();
            runtime.gc();
		 after = runtime.totalMemory() - runtime.freeMemory();
		 usedBytes = after - before;
		System.out.println("Delete Memory: " + usedBytes + " bytes");
		    System.out.println("Deletion time: " + (end-start)/1000000 + "ms");
		   // map.print();
	}
}
class Node<E> {
	private int key;
	public Node<E> next;
	public Node<E> prev;
	public E value;
	Node() {};
	Node(int key, E value) {
		this.key = key;
		this.value = value;
	}
	int getKey() {
		return key;
	}
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
class QuadHash<E extends Comparable<E>> {
	Node<E> arr[];
	Random generator;
    @SuppressWarnings("unchecked")
	QuadHash(int size) {
		generator = new Random();
		size++;
		while(!isPrime(size))
			size++;
		arr = (Node<E>[])new Node[size];
	}
	void print() {
		for(Node<E> cur: arr)
			System.out.println(cur);
	}
	public Node<E> insert(int key, E value) {
		for(int i = 0; i < arr.length; i++)
		{
			int index = hash(key, i);
			if(arr[index] == null) {
				arr[index] = new Node<>(key, value);
				return arr[index];
			}
			if(arr[index].getKey() == key)
				break;
		}
		// System.out.println("key has already been in use");
		return null;
	}
	public void delete(int key) {
		for(int i = 0; i < arr.length; i++)
		{
			int index = hash(key, i);
			if( arr[index] == null)
				break;
			if(arr[index].getKey() == key)
			{
				//   System.out.println("Deleted... " + arr[index]);
				arr[index] = null;
				return;
			}
		}
		//  System.out.println("Key hasn't been entered yet");
	}
	public Node<E> search(int key) {
		for(int i = 0; i < arr.length; i++)
		{
			int index = hash(key, i);
			if( arr[index] == null)
				break;

			if(arr[index].getKey() == key)
			{
				//   System.out.println("Found: " + arr[index]);
				return arr[index];
			}
		}
		// System.out.println("Key hasn't been entered yet");
		return null;
	}
	private int hash(int key, int index) {
		return ((key%arr.length) + index + (index*index))%arr.length;
	}
	boolean isPrime(int n) {
		for(int i=3; i*i<=n; i+=2) {
			if(n%i==0)
				return false;
		}
		return n%2==0;

	}

}

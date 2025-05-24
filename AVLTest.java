
import java.util.*;

public class AVLTest
{
	public static void main(String[] args) {
		Random generator = new Random();
		AVL<Integer> tree = new AVL<>();
		Runtime runtime = Runtime.getRuntime();
		runtime.gc();
		long before = runtime.totalMemory() - runtime.freeMemory();
        double start = System.nanoTime();
		int j= 0;
		// tree.root = tree.insert(generator.nextInt(20000), tree.root);
		final int iter = 100000;
		while(j < iter)
		{
			if(tree.insert(generator.nextInt(iter + (iter/2))) != null)
				j++;
		}
        double end = System.nanoTime();
		runtime.gc();
		long after = runtime.totalMemory() - runtime.freeMemory();
		long usedBytes = after - before;
		System.out.println("Insert Memory: " + usedBytes + " bytes");
		System.out.println("Insert time: " + (end-start)/1000000 + " ms");

		runtime.gc();
		before = runtime.totalMemory() - runtime.freeMemory();
    	start = System.currentTimeMillis();
		for(int i = 0; i < iter; i++)
			tree.search(generator.nextInt(iter + (iter/2)));
		end = System.currentTimeMillis();
		runtime.gc();
		after = runtime.totalMemory() - runtime.freeMemory();
		usedBytes = after - before;
		System.out.println("Search Memory: " + usedBytes + " bytes");
		System.out.println("Search time: " + (end-start)  + "ms");
		// tree.print();
		runtime.gc();
		before = runtime.totalMemory() - runtime.freeMemory();
		start = System.currentTimeMillis();
		for(int i = 0; i < iter; i++)
			tree.delete(generator.nextInt(iter + (iter/2)));
		end = System.currentTimeMillis();
		runtime.gc();
		after = runtime.totalMemory() - runtime.freeMemory();
		usedBytes = after - before;
	    System.out.println("Delete Memory: " + usedBytes + " bytes");
		System.out.println("Deletion time: " + (end-start)  + "ms");
		// tree.print();
		// tree.inOrder(tree.root);
		//System.out.println(tree.root);
	}
}
class Node<E> {
	Node<E> left;
	Node<E> right;
	int height;
	E value;
	Node() {
	}
	Node(E val) {
		value = val;
		height = 0;
	}
	//i need help to ensure variable E overrides toString
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
class AVL<E extends Comparable<E>> {
	Node<E> root;
	StringBuilder searchval = new StringBuilder();
	AVL() {
		root = null;
	}
	/*  void inOrder(Node<E> head){
	      //System.out.println(head.height);
	      inOrderWrapper(head);
	  }*/
	void inOrder(Node<E> head) {
		if(head == null)
			return;
		inOrder(head.left);
		System.out.println("Value: " + head);
		inOrder(head.right);
	}
	Node<E> insert(E element) {
		Node<E> temp = insert(element, root);
		if(temp == null)
			return temp;
		root = temp;
		return root;
	}
	private Node<E> insert(E element, Node<E> t) {
		if(t == null) {
			return new Node<>(element);
		}
		if(element.compareTo(t.value) < 0)
			t.left = insert(element, t.left);
		else if(element.compareTo(t.value) > 0)
			t.right = insert(element, t.right);
		else if(element.compareTo(t.value) == 0)
			return null;
		//  System.out.println("Cannot have the same values");
		return checkBalance(t);
	}
	void delete(E target) {
		root = delete(target, root);
	}
	Node<E> delete(E target, Node<E> t) {
		if(t == null)
			return t;
		int compareRes = target.compareTo(t.value);

		if(compareRes < 0)
			t.left = delete(target, t.left);
		else if(compareRes > 0)
			t.right = delete(target, t.right);
		else if(t.left != null && t.right != null)
		{
			t.value = findMax(t.left).value;
			t.left = delete(t.value, t.left);
		}
		else
			t = t.left != null ? t.left : t.right;
		return checkBalance(t);
	}
	public Node<E> search(E target) {
		if(root == null)
		{
			return null;
		}
		Node<E> parent = null;
		Node<E> current = root;

		while(current != null) {
			int compareVal = target.compareTo(current.value);
			parent = current;
			if(compareVal < 0)
				current = current.left;
			else if(compareVal > 0)
				current = current.right;
			else {
				// System.out.println("Found: " + current);
				return current;
			}
		}
		// System.out.println("Not found: " + target);
	
		return null;
	}
	public void print() {
		System.out.println(searchval.toString());
	}
	private Node<E> checkBalance(Node<E> t) {
		if(t == null)
			return t;

		if((height(t.left) - height(t.right)) > 1) {
			if(height(t.left.left) >= height(t.left.right))
				t = rotateWithLeftChild(t);
			else
				t = doubleWithLeftChild(t);
		}
		else if((height(t.right) - height(t.left)) > 1)
			if(height(t.right.right) >= height(t.right.left))
				t = rotateWithRightChild(t);
			else
				t = doubleWithRightchild(t);
		t.height = Math.max(height(t.left), height(t.right)) + 1;
		//   System.out.println(t);
		return t;

	}
	protected int height(Node<E> curNode) {
		if(curNode == null)
			return -1;
		return curNode.height;
	}
	protected Node<E> findMin(Node<E> t) {
		Node<E> parent = null;
		Node<E> curNode = t;
		while(curNode != null) {
			parent = curNode;
			curNode = curNode.left;
		}
		return parent;
	}
	protected Node<E> findMax(Node<E> t) {
		Node<E> parent = null;
		Node<E> curNode = t;
		while(curNode != null) {
			parent = curNode;
			curNode = curNode.right;
		}
		return parent;
	}
	protected Node<E> rotateWithLeftChild(Node<E> node1) {
		Node<E> node2 = node1.left;
		node1.left = node2.right;
		node2.right = node1;
		node1.height = Math.max(height(node1.left), height(node1.right)) + 1;
		node2.height = Math.max(height(node2.left), node1.height) + 1;
		//  if(root == node1)
		//    root = node2;
		return node2;
	}
	protected Node<E> rotateWithRightChild(Node<E> node1) {
		Node<E> node2 = node1.right;
		node1.right = node2.left;
		node2.left = node1;
		node1.height = Math.max(height(node1.right), height(node1.left)) + 1;
		node2.height = Math.max(height(node2.right), node1.height) + 1;
		//   if(node1 == root)
		//      root = node2;
		return node2;
	}
	protected Node<E> doubleWithLeftChild(Node<E> x) {
		x.left =  rotateWithRightChild(x.left);
		return  rotateWithLeftChild(x);
	}
	protected Node<E> doubleWithRightchild(Node<E> x) {
		x.right = rotateWithLeftChild(x.right);
		return  rotateWithRightChild(x);
	}
}

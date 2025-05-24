import java.util.*;
public class SplayTest
{
	public static void main(String[] args) {
		Random gen = new Random();
		Splay<Integer> tree = new Splay<>();
		Runtime runtime = Runtime.getRuntime();
        runtime.gc();
		long before = runtime.totalMemory() - runtime.freeMemory();
		int j= 0;
		double start = System.nanoTime();
		final int iter = 100000;
		while(j < iter)
		{
			if(tree.insert(gen.nextInt(iter + (iter/2))) != null)
				j++;
		}
			double end = System.nanoTime();
				      	       //tree.inOrder(tree.root);
        runtime.gc();
		long after = runtime.totalMemory() - runtime.freeMemory();
		long usedBytes = after - before;
		System.out.println("Insert Memory: " + usedBytes + " bytes");
        System.out.println("Insert time: " + (end-start)/1000000 + " ms");
		//	tree.inOrder(tree.root);
        runtime.gc();
		before = runtime.totalMemory() - runtime.freeMemory();
			start = System.nanoTime();
			for(int i = 0; i < iter; i++){
			   int rand = gen.nextInt(iter + (iter/2));
			    tree.search(rand);
		  
		}
		        end = System.nanoTime();
                runtime.gc();
		        after = runtime.totalMemory() - runtime.freeMemory();
                usedBytes = after - before;
		        System.out.println("Search Memory: " + usedBytes + " bytes");
		        System.out.println("Search time: "+ (end-start)/1000000 + " ms");

                 runtime.gc();
		        before = runtime.totalMemory() - runtime.freeMemory();
		         start = System.nanoTime();
		        for(int i = 0; i < iter; i++){
		            int rand = gen.nextInt(iter + (iter/2));
		          //  System.out.println(i);
			        tree.delete(rand);
		        }
		        end = System.nanoTime();
                runtime.gc();
		        after = runtime.totalMemory() - runtime.freeMemory();
                usedBytes = after - before;
	             System.out.println("Delete Memory: " + usedBytes + " bytes");
		        System.out.println("Deletion time: "+ (end-start)/1000000 + " ms");
		        // tree.inOrder(tree.root);
	}
}
class Node<E> {
	Node<E> left;
	Node<E> right;
	E value;
	Node() {
	}
	Node(E val) {
		value = val;
	}
	//i need help to ensure variable E overrides toString
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}

class Splay<E extends Comparable<E>> {
	Node<E> root;
	Splay() {
		root = null;
	}
	Node<E> insert(E value) {
		if(root == null)
		{
			root = new Node<>(value);
			return root;
		}
		Node<E> parent = null;
		Node<E> x = root;
		while(x != null) {
			int compareVal = value.compareTo(x.value);
			parent = x;
			if(compareVal < 0)
				x = x.left;
			else if(compareVal > 0)
				x = x.right;
			else {
				// System.out.println("Value already exists");
				return root;
			}
		}
		int compareVal = value.compareTo(parent.value);
		if (compareVal < 0)
			parent.left = new Node<E>(value);
		else
			parent.right = new Node<E>(value);


		return root;
	}
	void inOrder(Node<E> head) {
		if(head == null)
			return;
		inOrder(head.left);
		System.out.println("Value: " + head);
		inOrder(head.right);
	}
	// Node<E> delete()
	//tried to do iterative delete really hard
	Node<E> delete(E value) {
		Node<E> parent = null;
		Node<E> x = root;
		while(x != null) {
			int compareVal = value.compareTo(x.value);
			if(compareVal < 0) {
				parent = x;
				x = x.left;
			}
			else if(compareVal > 0) {
				parent = x;
				x = x.right;
			}
			else if(compareVal == 0) {
				//System.out.println("Deleting ..." + x);
				Node<E> temp = findMin(x.right);
				if(temp == null) {
					if((x.value).compareTo(parent.value) < 0)
						parent.left = null;
					else
						parent.right = null;
					return parent;
				}
				x.value = temp.left.value;
				temp.left = temp.left.right;
				return x;
			}
		}
		// System.out.println("This value does not exist");
		return null;
	}

	protected Node<E> findMin(Node<E> t) {
		if(t == null || t.left == null)
			return null;

		Node<E> parent = null;
		Node<E> curNode = t;
		while(curNode.left != null) {
			parent = curNode;
			curNode = curNode.left;
		}
		return parent;
	}
	protected Node<E> findMax(Node<E> t) {
		if(t == null)
			return t;
		Node<E> parent = null;
		Node<E> curNode = t;
		while(curNode.left != null) {
			parent = curNode;
			curNode = curNode.right;
		}
		return parent;
	}
	Node<E> search(E target) {
		root = splay(root, target);
		return root;
	}
	private Node<E> splay(Node<E> t, E target) {
		if(t == null) {
			return null;
		}
		int compareVal = target.compareTo(t.value);
		if(compareVal == 0) {
			return t;
		}
		else if(compareVal < 0) {
			if(t.left == null)
				return t;
			int comp2 = target.compareTo(t.left.value);
			if(comp2 < 0) {
				t.left.left = splay(t.left.left, target);
				t = rotateRight(t);
			}
			else if(comp2 > 0) {
				t.left.right = splay(t.left.right, target);
				if (t.left.right != null)
					t.left = rotateLeft(t.left);
			}

			if (t.left == null)
				return t;
			else
				return rotateRight(t);

		}
		else {
			if(t.right == null)
				return t;
			int comp2 = target.compareTo(t.right.value);
			if(comp2 < 0) {
				t.right.left = splay(t.right.left, target);
				if(t.right.left != null)
					t.right = rotateRight(t.right);
			}
			else if(comp2 > 0) {
				t.right.right = splay(t.right.right, target);
				t = rotateLeft(t);
			}

			if (t.right == null)
				return t;
			else
				return rotateLeft(t);
		}

	}
	private Node<E> rotateRight(Node<E> t) {
		Node<E> k2 = t.left;
		t.left = k2.right;
		k2.right = t;
		return k2;
	}
	private Node<E> rotateLeft(Node<E> t) {
		Node<E> k2 = t.right;
		t.right = k2.left;
		k2.left = t;
		return k2;
	}

}


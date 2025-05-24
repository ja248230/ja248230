
import java.time.LocalDateTime;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;
public class BrowserDriver
{
	public static void main(String[] args) {
		BrowserNavigation newBrowser = new BrowserNavigation();
		System.out.println("\t\tWelcome to Bessie Safari Browser!");
		Scanner input = new Scanner(System.in);
		int choice;
		do {
			System.out.println("1. Visit a website");
			System.out.println("2. Go Back");
			System.out.println("3. Go Forward");
			System.out.println("4. Show History");
			System.out.println("5. Clear History");
			System.out.println("6. Close Browser");
			System.out.print("\nEnter a choice? ");
			choice = input.nextInt();
			input.nextLine();
			switch(choice) {
			case 1:
				System.out.print("Enter website name: ");
				String in = input.nextLine();
				newBrowser.visitWebsite(in);
				break;
			case 2:
				newBrowser.goback();
				break;
			case 3:
				newBrowser.goforward();
				break;
			case 4:
				newBrowser.showHistory();
				break;
			case 5:
				newBrowser.clearHistory();
				break;
			case 6:
				newBrowser.closeBrowser();
			default:
				break;
			}
		} while (choice != 6);
	}
}
class Website {
	public String url;
	public int index;
	public LocalDateTime time;
	public Website next = null;
	public Website prev = null;

	public Website() {}

	public Website(String url) {
		time = LocalDateTime.now();
		this.url = url;
	}
	//override toString so it prints the name of the url and the time when it was created would differ betwen
	//the queue and stack because the queue can have multiple of the same name/url but the times would be different
	@Override
	public String toString() {
		return url + " " + time;
	}
}
class BrowserLinkedList <E extends Iterable<E>> {
	public Website head;
	public Website tail;

	public BrowserLinkedList() {
		head = null;
		tail = null;
	}
	public BrowserLinkedList(String value) {
		head = tail = new Website(value);
	}
	protected void insert(Website newNode) {
		if (head == null) {
			head = tail = newNode;
		} else {
			newNode.next = head;
			head.prev = newNode;
			head = newNode;
		}
	}

	protected Website delete(String target) {
		Website curNode = search(target);
		if(curNode == null) {
			System.out.println("You have not browsed this URL");
			return null;
		}
		if(curNode == head)
			head = curNode.next;

		if (curNode.prev != null)
			curNode.prev.next = curNode.next;

		if (curNode.next != null)
			curNode.next.prev = curNode.prev;


		System.out.println("Deleted: " + target);
		return curNode;
	}
	protected Website search(String target) {
		Website cur = head;
		while(cur != null) {
			if(cur.url.equals(target))
				return cur;
			cur = cur.next;
		}
		return null;
	}

}
class BrowserStack extends BrowserLinkedList implements Iterable<Website> {
	public BrowserLinkedList list;
	public BrowserStack() {
		list = new BrowserLinkedList();
	}
	public BrowserStack(String url) {
		list = new BrowserLinkedList(url);
	}
	public Website pop() {
		if (list.head == null) {
			System.out.println("No pages to go back to.");
			return null;
		}
		return list.delete(list.head.url);
	}
	public void push(Website url) {
		list.insert(url);
	}
	@Override
	public Iterator<Website> iterator() {
		return new StackIterator();
	}
	private class StackIterator implements Iterator<Website> {
		private Website adt = list.head;
		@Override
		public boolean hasNext() {
			return (null != adt);
		}
		@Override
		public Website next() {
		    if (!hasNext())
				throw new NoSuchElementException();
			Website curNode = adt;
			adt = adt.next;
			return curNode;
		}
	}
}

class BrowserArrayList<E extends Iterable<E>> {
	protected Object[] list;
	public int cellsFilled, front, back, size;
	private static final int DEFAULT_SIZE = 10;

	public BrowserArrayList() {
		size = DEFAULT_SIZE;
		list =  new Object[DEFAULT_SIZE];
		front = cellsFilled = 0;
		back = -1;
	}

	public BrowserArrayList(int size) {
		this.size = size;
		//type casts into a website array 
		list = (Website[])new Object[size];
		front = cellsFilled = 0;
		back = -1;
	}

	/*
		public Website get(int index) {
			if (index >= size || index < 0)
				throw new IndexOutOfBoundsException("out of bounds");
			return (Website) list[index];
		}
	*/
	//doubles the size then reallocates the new list into the old one makes the old list the new one
	protected void resize() {
		int newSize = size * 2;
		Object newList[] = (Website[])new Object[newSize];
		for (int i = 0; i < cellsFilled; i++)
			newList[i] = list[(front + i) % size];
		list = newList;
		size = newSize;
		front = 0;
		back = cellsFilled - 1;
	}
}

class BrowserQueue extends BrowserArrayList implements Iterable<Website> {

	public BrowserQueue() {
		super();
	}

	public BrowserQueue(Website... pages) {
		super(pages.length);
		for (Website object : pages)
			enqueue(object);
	}

	public BrowserQueue(int num) {
		super(num);
	}
//checks if the amount of array cells filled are equal to the actual size so we end up resizing else just add
	public void enqueue(Website element) {
		if (size == cellsFilled)
			resize();
		back = (back + 1) % size;
		list[back] = element;
		cellsFilled++;
	}

	public Website dequeue() {
		if (cellsFilled == 0)
			throw new IllegalStateException("no urls");
		Website cur = (Website)list[front];
		list[front] = null;
		front = (front + 1) % size;
		cellsFilled--;
		return cur;
	}
//private class for the iterator 
	@Override
	public Iterator<Website> iterator() {
		return new QIterator();
	}
	protected class QIterator implements Iterator<Website> {
		protected int count;
		protected int i;
		public QIterator() {
			count = 0;
		}
		@Override
		public boolean hasNext() {
			return count < cellsFilled;
		}
		@Override
		public Website next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Website node = (Website)list[i];
			i = (i + 1) % size;
			count++;
			return node;
		}
	}
}
class BrowserNavigation {
	BrowserStack frontStack;
	BrowserStack backStack;
	BrowserQueue q;

	public BrowserNavigation() {
		frontStack = new BrowserStack();
		backStack = new BrowserStack();
		q = new BrowserQueue();
		restoreLastSession();
	}
	public void visitWebsite(String url) {
		Website page = new Website(url);
		while(frontStack.list.head != null)
			frontStack.pop();
		backStack.push(page);
		q.enqueue(page);
		System.out.println(page);
	}
	//bothgo back and go forward would create a new object into the queue just for history checking
	public void goback() {
		if(backStack.list.head == null) {
			throw new EmptyStackException();
		}
		if(backStack.list.head.next == null) {
			System.out.println("Cannot go back");
			return;
		}

		Website node = backStack.pop();

		frontStack.push(node);
		q.enqueue(node);
		System.out.println("You are now on " + backStack.list.head.url);
	}
    //pop from the frontStack check if there isnt anything left in the stack then push onto back stack if frontStack
    //stack is empty then you cannot go forward
	public void goforward() {
	   if (frontStack.list.head == null) {
			System.out.println("on the most forward page");
			return;
		}
		Website node = frontStack.pop();
		if(node == null) {
			throw new EmptyStackException();
		}
		backStack.push(node);

		q.enqueue(node);
		System.out.println("You are now on: " + frontStack.list.head.url);
	}
	public void showHistory() {
		for(Website web: q) {
			System.out.println(web);
		}
	}
	public void closeBrowser() {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter("session_data.txt"))) {
			writer.write("Front Stack\n");
			for(Website obj: frontStack) {
				writer.write(obj.url + "\n");
			}
			writer.write("Back Stack\n");
			for(Website obj: backStack) {
				writer.write(obj.url + "\n");
			}

			writer.write("Queue\n");
			for(Website obj: q) {
				writer.write(obj.url + "\n");
			}
			System.out.println("Good Bye...");
		}
		catch (IOException e) {
			e.printStackTrace();
		}


	}
	//open buffer reader and read into respective stack based on identifier from the txt file
	public void restoreLastSession() {
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader("session_data.txt"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		String line;
		if (fileReader == null) {
			System.out.println("Error: file must be opened first!");

		} else {

			try {
				while((line = fileReader.readLine()) != null) {
					BrowserStack curStack = null;
					BrowserQueue curQ = null;
					while((line = fileReader.readLine()) != null) {
						switch(line) {
						case "Front Stack":
							curStack = frontStack;
							curQ = null;
							break;
						case "Back Stack":
							curStack = backStack;
							curQ = null;
							break;
						case "Queue":
							curStack = null;
							curQ = q;
							break;
						default:
							if(curStack != null) {
								Website web = new Website(line);
								curStack.push(web);
							}
							else if(curQ != null) {
								Website web = new Website(line);
								curQ.enqueue(web);
							}
							break;
						}
					}

					System.out.println("\t\tSession restored!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
    //create a new browser queue
	public void clearHistory() {
		q = new BrowserQueue();
	}

}

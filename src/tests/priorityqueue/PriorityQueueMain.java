package tests.priorityqueue;

// Java program to demonstrate the
// working of PriorityQueue
// https://www.geeksforgeeks.org/priority-queue-class-in-java-2/
import java.util.*;

class PriorityQueueMain {

	// Main Method
	public static void main(String args[])
	{
		// Creating empty priority queue
		PriorityQueue<Integer> pQueue = new PriorityQueue<Integer>();

		// Adding items to the pQueue using add()
		pQueue.add(10);
		pQueue.add(20);
		pQueue.add(15);

		// Printing the top element of PriorityQueue
		System.out.println(pQueue.peek());

		// Printing the top element and removing it
		// from the PriorityQueue container
		System.out.println(pQueue.poll());

		// Printing the top element again
		System.out.println(pQueue.peek());
	}
}

/* 
Output:

10
10
15
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/******************************************************************************
 *  Name:    
 *  NetID:   
 *  Precept: 
 *
 *  Partner Name:    
 *  Partner NetID:   
 *  Partner Precept: 
 * 
 *  Description:  
 *
 * This is a template file for RingBuffer.java. It lists the constructors and
 * methods you need, along with descriptions of what they're supposed to do.
 *  
 * Note: it won't compile until you fill in the constructors and methods
 *       (or at least commment out the ones whose return type is non-void).
 *
 ******************************************************************************/

public class RingBuffer {
    // YOUR INSTANCE VARIABLES HERE
	private int capacity, first, last;
	private double[] arr;

    // creates an empty ring buffer with the specified capacity
    public RingBuffer(int capacity) {
        // YOUR CODE HERE
    	arr = new double[capacity];
    	Arrays.fill(arr, -1e8);
    	this.capacity = capacity;
    	first = 0;
    	last = 0;
    }

    // return the capacity of this ring buffer
    public int capacity() {
        // YOUR CODE HERE
    	return capacity;
    }

    // return number of items currently in this ring buffer
    public int size() {
        // YOUR CODE HERE
    	int cnt = 0;
    	for(double item : arr) {
    		if(item != -1e8) cnt++;
    	}
    	return cnt;
    }

    // is this ring buffer empty (size equals zero)?
    public boolean isEmpty() {
        // YOUR CODE HERE
    	return size() == 0;
    }

    // is this ring buffer full (size equals capacity)?
    public boolean isFull() {
        // YOUR CODE HERE
    	return size() == capacity;
    }

    // adds item x to the end of this ring buffer
    public synchronized void enqueue(double x) {
        // YOUR CODE HERE
    	if(isFull()) throw new IllegalStateException();
    	arr[last] = x;
    	last = last+1 >= capacity ? 0 : last+1;
    	
    }

    // deletes and returns the item at the front of this ring buffer
    public synchronized double dequeue() {
        // YOUR CODE HERE
    	if(isEmpty()) throw new NoSuchElementException();
    	double ret = arr[first];
    	arr[first] = -1e8;
    	first = first+1 >= capacity ? 0 : first+1;
    	return ret;
    }

    // returns the item at the front of this ring buffer
    public double peek() {
        // YOUR CODE HERE
    	if(isEmpty()) throw new NoSuchElementException();
    	return arr[first];
    }
    
    // returns a string interpretation of the ring buffer
    public String toString() {
    	List<Double> out = new ArrayList<>();
    	//System.out.println(first + " " + last + " " + Arrays.toString(arr));
    	int i = first;
    	if(first == last && arr[0] != -1e8) {
    		while(out.size() != capacity) {
    			out.add(arr[i]);
        		i = i+1 >= capacity ? 0 : i+1;
    		}
    	}
    	else {
        	while(i != last) {
        		out.add(arr[i]);
        		i = i+1 >= capacity ? 0 : i+1;
        	}
    	}
    	return out.toString();
    }

    // tests and calls every instance method in this class
    public static void main(String[] args) {
        // YOUR CODE HERE
        int N = (int)(Math.ceil(44100/2000));
    	RingBuffer rb = new RingBuffer(N);
    	System.out.println(rb.isEmpty());
    	System.out.println(N + " " + rb.size() + " " + rb.capacity());
    	for(int i = 0; i < N; i++) {
    		rb.enqueue(0);
    	}
    	System.out.println(rb.toString());
    	System.out.println(rb.isFull());
    	N = rb.capacity();
    	for(int i = 0; i < N; i++) {
    		rb.dequeue();
    		rb.enqueue(Math.random() - 0.5);
    	}
    	System.out.println(rb.toString());
    	System.out.println(rb.peek());
    	rb.dequeue();
    	System.out.println(rb.toString());
    	System.out.println(rb.peek());
    	System.out.println(rb.size());
    }

}

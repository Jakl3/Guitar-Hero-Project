/******************************************************************************
 *  Name:    
 *  NetID:   
 *  Precept: 
 *
 *  Partner Name:    
 *  Partner NetID:   
 *  Partner Precept: 
 * 
 * Description: 
 *  
 * This is a template file for GuitarString.java. It lists the constructors
 * and methods you need, along with descriptions of what they're supposed
 * to do.
 *  
 * Note: it won't compile until you fill in the constructors and methods
 *       (or at least commment out the ones whose return type is non-void).
 *
 ******************************************************************************/

public class GuitarString {
    // YOUR INSTANCE VARIABLES HERE
	private volatile RingBuffer rb;
	private int cnt;
	
    // creates a guitar string of the specified frequency,
    // using sampling rate of 44,100
    public GuitarString(double frequency) {
        // YOUR CODE HERE
    	cnt = 0;
    	int N = (int)(Math.ceil(44100.0/frequency));
    	rb = new RingBuffer(N);
    	for(int i = 0; i < N; i++) {
    		rb.enqueue(0);
    	}
    }

    // creates a guitar string whose size and initial values are given by
    // the specified array
    public GuitarString(double[] init) {
        // YOUR CODE HERE
    	cnt = 0;
    	rb = new RingBuffer(init.length);
    	for(double item : init) {
    		rb.enqueue(item);
    	}
    }

    // returns the number of samples in the ring buffer
    public int length() {
        // YOUR CODE HERE
    	return rb.size();
    }

    // plucks the guitar string (by replacing the buffer with white noise)
    public void pluck() {
        // YOUR CODE HERE
    	int N = rb.capacity();
    	for(int i = 0; i < N; i++) {
    		rb.dequeue();
    		rb.enqueue(Math.random() - 0.5);
    	}
    }

    // advances the Karplus-Strong simulation one time step
    public void tic() {
        // YOUR CODE HERE
    	double a = rb.dequeue();
    	double b = rb.peek();
    	double res = 0.994 * 0.5 * (a + b);
    	rb.enqueue(res);
    	cnt++;
    }

    // returns the current sample
    public double sample() {
        // YOUR CODE HERE
    	return rb.peek();
    }
    
    // returns the total number of times tic() was called
    public int time() {
    	return cnt;
    }

    // tests and calls every constructor and instance method in this class
    public static void main(String[] args) {
        // YOUR CODE HERE
    }

}

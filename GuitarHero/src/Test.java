import java.util.*;
import java.io.*;
import java.text.*;
import java.math.*;
import java.lang.Math.*;

public class Test {
	public void run() throws Exception {
		//Scanner file = new Scanner("Test.");
		Scanner file = new Scanner(System.in);
		StdDraw.enableDoubleBuffering();
		StdDraw.line(0, 0, 1, 1);
		StdDraw.show();
		file.close();
	}

	public static void main(String[] args) throws Exception {
		new Test().run();
	}

}
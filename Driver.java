package polynomial.java;

import java.io.*;

public class Driver {

	public static void main(String[] args) throws IOException {
        File file = new File("polynomial.txt");
        Polynomial p = new Polynomial(file);
	System.out.println(p.evaluate(3));
	double [] c1 = {6, 5};
	int [] e1 = {0, 7};
	Polynomial p1 = new Polynomial(c1, e1);
	double [] c2 = {-2, -9};
	int [] e2 = {1, 3};
	Polynomial p2 = new Polynomial(c2, e2);
	Polynomial s = p1.add(p2);
	System.out.println("s(0.1) = " + s.evaluate(0.1));
	if(s.hasRoot(1))
		System.out.println("1 is a root of s");
	else
		System.out.println("1 is not a root of s");
	}

}
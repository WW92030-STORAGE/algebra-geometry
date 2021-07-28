import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeSet;

public class convexhullstandalone { // convex hull algorithm. input and output format is given by https://cses.fi/problemset/task/2195/
	static class pair implements Comparable<pair>{
		private int x;
		private int y;
		public pair(int a, int b) {
			x = a;
			y = b;
		}
		
		public void set(int a, int b) {
			x = a;
			y = b;
		}
		
		public void set(pair p) {
			x = p.x;
			y = p.y;
		}
		
		public pair clone() {
			return new pair(this.x, this.y);
		}
		
		public pair invert() {
			return new pair(this.y, this.x);
		}
		
		public int compareTo(pair other) {
			Integer px = this.x;
			Integer py = this.y;
			Integer ox = other.x;
			Integer oy = other.y;
			if (!px.equals(ox)) return px.compareTo(ox);
			if (!py.equals(oy)) return py.compareTo(oy);
			return 0;
		}
		
		public boolean equals(pair other) {
			return this.compareTo(other) == 0;
		}
		
		public pair min(pair q) {
			if (this.compareTo(q) > 0) return q.clone();
			return this.clone();
		}
		
		public String toString() {
			return "[" + x + " " + y + "]";
		}
		
		public static int orientation(pair a, pair b, pair c) {
			int tester = (b.y - a.y) * (c.x - b.x) - (c.y - b.y) * (b.x - a.x);
			if (tester < 0) return 1;
			else if (tester > 0) return -1;
			return 0;
		}
	}
	
	public static ArrayList<pair> solve(ArrayList<pair> list) {
		Collections.sort(list);
	//	System.out.println(list);
		ArrayList<pair> upper = new ArrayList<pair>();
		upper.add(list.get(0).clone());
		upper.add(list.get(1).clone());
		for (int i = 2; i < list.size(); i++) {
			pair p = list.get(i).clone();
		//	System.out.println(upper + " + " + p);
			while (upper.size() >= 2 && pair.orientation(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p) > 0) upper.remove(upper.size() - 1);
			upper.add(p);
		}
	//	System.out.println("UPPER " + upper);
		
		ArrayList<pair> lower = new ArrayList<pair>();
		lower.add(list.get(0).clone());
		lower.add(list.get(1).clone());
		for (int i = 2; i < list.size(); i++) {
			pair p = list.get(i).clone();
		//	System.out.println(lower + " + " + p);
			while (lower.size() >= 2 && pair.orientation(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p) < 0) lower.remove(lower.size() - 1);
			lower.add(p);
		}
		
	//	System.out.println("UPPER " + upper + "\nLOWER " + lower);
		ArrayList<pair> res = new ArrayList<pair>();
		TreeSet<pair> set = new TreeSet<pair>();
		for (pair p : lower) {
			if (!set.contains(p)) {
				set.add(p.clone());
				res.add(p.clone());
			}
		}
		for (pair p : upper) {
			if (!set.contains(p)) {
				set.add(p.clone());
				res.add(p.clone());
			}
		}
		return res;
	}
	
	public static void main(String[] args) throws IOException {
		ArrayList<pair> test = new ArrayList<pair>();
		/*
		test.add(new pair(0, 0));
		test.add(new pair(0, 2));
		test.add(new pair(2, 0));
		test.add(new pair(2, 2));
		test.add(new pair(1, 1));
		System.out.println(solve(test));
		*/
		
		Scanner x = new Scanner(System.in);
		int n = x.nextInt();
		for (int i = 0; i < n; i++) {
			int a = x.nextInt();
			int b = x.nextInt();
			test.add(new pair(a, b));
		}
		ArrayList<pair> res = solve(test);
		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out));
		output.write(res.size() + "\n");
		for (pair p : res) output.write((int)p.x + " " + (int)p.y + "\n");
		output.flush();
	}
}

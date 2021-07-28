import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class convexhull3d {
	static class pair implements Comparable<pair>{
		private int x;
		private int y;
		public pair(int a, int b) {
			x = a;
			y = b;
		}
		
		public void set(pair p) {
			x = p.x;
			y = p.y;
		}
		
		public pair clone() {
			return new pair(x + 0, y + 0);
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
		
		public String toString() {
			return "[" + x + " " + y + "]";
		}
	}
	
	static class triple implements Comparable<triple>{
		private double x;
		private double y;
		private double z;
		public triple(double a, double b, double c) {
			x = a;
			y = b;
			z = c;
		}
		
		public void set(double a, double b, double c) {
			x = a;
			y = b;
			z = c;
		}
		
		public triple clone() {
			return new triple(x + 0, y + 0, z + 0);
		}
		
		public int compareTo(triple other) {
			Double px = this.x;
			Double py = this.y;
			Double pz = this.z;
			Double ox = other.x;
			Double oy = other.y;
			Double oz = other.z;
			if (px != ox) return px.compareTo(ox);
			if (py != oy) return py.compareTo(oy);
			if (pz != oz) return pz.compareTo(oz);
			return 0;
		}
		
		public boolean equals(triple other) {
			return this.compareTo(other) == 0;
		}
		
		public String toString() {
			return "[" + x + " " + y + " " + z + "]";
		}
		
		public triple add(triple b) {
			return new triple(x + b.x, y + b.y, z + b.z);
		}
		
		public triple subtract(triple b) {
			return new triple(x - b.x, y - b.y, z - b.z);
		}
		
		public double dot(triple b) {
			return x * b.x + y * b.y + z * b.z;
		}
		
		public triple cross(triple b) {
			return new triple(y * b.z - z * b.y, z * b.x - b.z * x, x * b.y - b.x * y);
		}
	}
	
	// A point is an ordered triple of integer coordinates
	// A face is a triple of integers (point indices) mapped to another ordered triple (normal vector)
	
	static class face {
		private int a, b, c;
		private triple p;
		private triple n;
		public face(triple p, triple x) {
			a = (int)p.x;
			b = (int)p.y;
			c = (int)p.z;
			n = x.clone();
			this.p = new triple(a, b, c);
		}
		
		public face(int aa, int bb, int cc, triple d) {
			a = aa;
			b = bb;
			c = cc;
			n = d.clone();
			p = new triple(a, b, c);
		}
		
		public face clone() {
			return new face(p, n);
		}
		
		public int compareTo(face other) {
			if (!p.equals(other.p)) return p.compareTo(other.p);
			return n.compareTo(other.n);
		}
		
		public boolean equals(face other) {
			return (p.equals(other.p)) && (n.equals(other.n));
		}
		
		public String toString() {
			return "|" + p.toString() + " | " + n.toString() + "|";
		}
	}
	
	static final double E = Math.pow(10, -12);
	
	static boolean[][] dead;
	static face lol(ArrayList<triple> p, int a, int b, int c) {
		dead[a][b] = dead[b][c] = dead[c][a] = false;
		return new face(a, b, c, (p.get(b).subtract(p.get(a))).cross(p.get(c).subtract(p.get(a))));
	}
	
	public static ArrayList<face> solve(ArrayList<triple> points) {
		Collections.sort(points);
		int n = points.size();
		ArrayList<face> res = new ArrayList<face>();
		if (points.size() < 3) return res;
		dead = new boolean[n][n];
		for (int i = 0; i < n; i++) Arrays.fill(dead[i], true);
		res.add(lol(points, 0, 1, 2));
		res.add(lol(points, 0, 2, 1));
		for (int i = 3; i < n; i++) {
			ArrayList<face> invis = new ArrayList<face>();
			for (face x : res) {
				if ((points.get(i).subtract(points.get(x.a)).dot(x.n)) > E) {
					dead[x.a][x.b] = dead[x.b][x.c] = dead[x.c][x.a] = true;
				}
				else invis.add(x.clone());
			}
			
			res.clear();
			for (face x : invis) {
				int[] ps = {x.a, x.b, x.c};
				for (int ind = 0; ind < 3; ind++) {
					int cur = ps[ind];
					int next = ps[(ind + 10) % 3];
					if (dead[next][cur]) res.add(lol(points, next, cur, i));
				}
			}
			for (face x : invis) res.add(x);
		}
		return res;
	}
	
	static triple x(int a, int b, int c) {
		return new triple(a, b, c);
	}
	
	public static void main(String[] args) {
		ArrayList<triple> points = new ArrayList<triple>();
		points.add(x(0, 0, 0));
		points.add(x(8, 0, 0));
		points.add(x(0, 8, 0));
		points.add(x(0, 0, 8));
	//	points.add(x(-8, 0, 0));
	//	points.add(x(0, -8, 0));
	//	points.add(x(0, 0, -8));
		points.add(x(0, 8, 8));
		points.add(x(8, 8, 0));
		points.add(x(8, 0, 8));
		points.add(x(8, 8, 8));
		ArrayList<face> res = solve(points);
		System.out.println(res);
		System.out.println(res.size());
	}
}

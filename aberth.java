import java.util.Arrays;

public class aberth { // here we go into the death zone
	static class pair implements Comparable<pair>{
		private double x;
		private double y;
		public pair(double a, double b) {
			x = a;
			y = b;
		}
		
		public void set(double a, double b) {
			x = a;
			y = b;
		}
		
		public void set(pair p) {
			x = p.x;
			y = p.y;
		}
		
		public double getX() {
			return x + 0.0;
		}
		
		public double getY() {
			return y + 0.0;
		}
		
		public pair clone() {
			return new pair(this.getX(), this.getY());
		}
		
		public int compareTo(pair other) {
			double epsilon = Math.pow(10, -12);
			Double px = this.getX();
			Double py = this.getY();
			Double ox = other.getX();
			Double oy = other.getY();
			if (!px.equals(ox)) {
				if (Math.abs(px - ox) < epsilon) return 0;
				else return px.compareTo(ox);
			}
			if (!py.equals(oy)) {
				if (Math.abs(py - oy) < epsilon) return 0;
				else return py.compareTo(oy);
			}
			return 0;
		}
		
		public boolean equals(pair other) {
			return this.compareTo(other) == 0;
		}
		
		public String toString() {
			return "[" + x + " " + y + "]";
		}
	}
	
	static class comp extends pair {
		public comp(double a, double b) {
			super(a, b);
		}
		
		public comp clone() {
			return new comp(this.getX(), this.getY());
		}
		
		public comp add(comp other) {
			return new comp(this.getX() + other.getX(), this.getY() + other.getY());
		}
		
		public comp subtract(comp other) {
			return new comp(this.getX() - other.getX(), this.getY() - other.getY());
		}
		
		public comp multiply(comp other) {
			return new comp(this.getX() * other.getX() - this.getY() * other.getY(), this.getX() * other.getY() + this.getY() * other.getX());
		}
		
		public comp inv() {
			double rsq = this.getX() * this.getX() + this.getY() * this.getY();
			if (rsq == 0) return new comp(0, 0);
			return new comp(this.getX() / rsq, -1.0 * this.getY() / rsq);
		}
		
		public comp divide(comp other) {
			return this.multiply(other.inv());
		}
		
		public String toString() {
			if (this.getY() > 0) return this.getX() + " + " + this.getY() + "i";
			if (this.getY() < 0) return this.getX() + " - " + Math.abs(this.getY()) + "i";
			return "" + this.getX();
		}
	}
	
	static comp eval(comp[] p, comp x) {
		comp res = new comp(0, 0);
		int n = p.length;
		for (int i = 0; i < n; i++) {
			res = res.multiply(x);
			res = res.add(p[i]);
		}
		return res;
	}
	
	static comp deriv(comp[] p, comp x) {
		double e = Math.pow(10, -12);
		comp epsilon = new comp(e, e);
		comp dy = (eval(p, x.add(epsilon)).subtract(eval(p, x)));
		return dy.divide(epsilon);
	}
	
	static comp[] convert(double[] p) {
		comp[] res = new comp[p.length];
		for (int i = 0; i < p.length; i++) res[i] = new comp(p[i], 0);
		return res;
	}
	
	static comp[] solve(comp[] p) {
		int n = p.length - 1;
		comp[] res = new comp[n];
		comp[] offset = new comp[n];
		comp one = new comp(1, 0);
		double x = -1.0 * p[1].getX() / n;
		double y = -1.0 * p[1].getY() / n;
		for (int i = 0; i < n; i++) res[i] = new comp(Math.random() * 2 + x - 1.0, Math.random() * 2 + y - 1.0);
		for (int sksk = 0; sksk < 100000; sksk++) {
			for (int i = 0; i < n; i++) {
				comp sum = new comp(0, 0);
				for (int j = 0; j < n; j++) {
					if (j == i) continue;
					sum = sum.add((res[i].subtract(res[j])).inv());
				}
				comp val = eval(p, res[i]);
				comp dev = deriv(p, res[i]);
				comp quo = val.divide(dev);
				comp denom = one.subtract(quo.multiply(sum));
				offset[i] = quo.divide(denom);
			}
			for (int i = 0; i < n; i++) res[i] = res[i].subtract(offset[i]);
		}
		Arrays.sort(res);
		return res;
	}
	
	public static void main(String[] args) {
		double[] poly = {1, -3, 7, 75};
		comp[] p = convert(poly);
		System.out.println(Arrays.toString(solve(p)));
	}
}

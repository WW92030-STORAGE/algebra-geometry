import java.util.Arrays;

public class powerrule {
	static double[] d(double[] p) {
		int n = p.length;
		double[] res = new double[n - 1];
		for (int i = 0; i < n - 1; i++) {
			res[i] = p[i] * (n - i - 1);
		}
		return res;
	}
	
	public static void main(String[] args) {
		double[] p = {1, 1, 1, 1, 1};
		System.out.println(Arrays.toString(d(p)));
	}
}

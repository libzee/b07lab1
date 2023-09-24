public class Polynomial {
	private double[] coefficient;
	
	public Polynomial() {
		this.coefficient = new double[] {0};
	}
	
	public Polynomial(double... coefficient) {
		this.coefficient = coefficient;
	}
	
	public Polynomial add(Polynomial other) {
		int maxlength = Math.max(coefficient.length, other.coefficient.length);
		double[] resultcoefficient = new double[maxlength];
		
		for (int i = 0; i < coefficient.length; i++) {
			resultcoefficient[i] += coefficient[i];
		}
		
		for (int i = 0; i < other.coefficient.length; i++) {
			resultcoefficient[i] += other.coefficient[i];
		}
		
		return new Polynomial(resultcoefficient);
	}
	
	public double evaluate(double x) {
		double result = 0;
		
		for (int i = 0; i < coefficient.length; i++) {
			result += coefficient[i] * Math.pow(x, i);
		}
		
		return result;
	}
	
	public boolean hasRoot(double x) {
		return evaluate(x) == 0;
	}
	
}
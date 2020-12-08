package algorithms;

public class Multplication extends Expression {

	public Multplication(double left, double right) {
		super(left, right);
	}

	@Override
	public double evaluate() {
		return left * right;
	}

}

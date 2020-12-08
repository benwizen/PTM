package algorithms;

public class Addition extends Expression {

	public Addition(double left, double right) {
		super(left, right);
	}

	@Override
	public double evaluate() {
		return left + right;
	}

}

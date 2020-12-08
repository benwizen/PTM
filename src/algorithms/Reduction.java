package algorithms;

public class Reduction extends Expression {

	public Reduction(double left, double right) {
		super(left, right);
	}

	@Override
	public double evaluate() {
		return left-right;
	}

}

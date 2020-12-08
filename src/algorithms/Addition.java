package algorithms;
import algorithms.Converter;

public class Addition extends Expression {

	public Addition(double left, double right) {
		super(left, right);
	}

	@Override
	public double evaluate() {
		return left + right;
	}

}

package algorithms;

public class Division extends Expression {

	public Division(double left, double right) {
		super(left, right);
	}

	@Override
	public double evaluate() {
		return left/right;
	}

}

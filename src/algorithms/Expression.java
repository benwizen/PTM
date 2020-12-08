package algorithms;

public abstract class Expression {
	double left,right;
	public Expression(double left,double right){
		this.left = left;
		this.right = right;
	}
	public abstract double evaluate();
}

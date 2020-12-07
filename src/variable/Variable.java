package variable;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


@SuppressWarnings("deprecation")
public class Variable extends Observable implements Observer {
	private String name;
	private String value;
	
	public Variable(String name, String value) {
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(String value) throws IOException {
		this.value = value;
		setChanged();
		notifyObservers(this.value);
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
			Variable otherVariable = (Variable) o;
			if (this.value != otherVariable.getValue()) {
				try {
					this.setValue(otherVariable.getValue());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}

}

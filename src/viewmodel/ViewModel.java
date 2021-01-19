package viewmodel;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.Model;
import view.MainWindowController;

public class ViewModel extends Observable implements Observer {

	Model m;
	MainWindowController wc;
	private DoubleProperty joystickDragX;
	private DoubleProperty joystickDragY;
	private DoubleProperty rudderSliderValue;
	private DoubleProperty throttleSliderValue;
	private StringProperty sliderType;
	private StringProperty ipTxt;
	private StringProperty portTxt;
	private StringProperty autopilotScript;
	public ViewModel(Model m, MainWindowController windowController) {
		this.m = m;
		this.wc = windowController;
		joystickDragX = new SimpleDoubleProperty();
		joystickDragY = new SimpleDoubleProperty();
		rudderSliderValue = new SimpleDoubleProperty();
		throttleSliderValue = new SimpleDoubleProperty();
		sliderType = new SimpleStringProperty();
		autopilotScript = new SimpleStringProperty();
		
		joystickDragX.bind(wc.getJoystickDragX());
		joystickDragY.bind(wc.getJoystickDragY());
		rudderSliderValue.bind(wc.getRudderSlider().valueProperty());
		throttleSliderValue.bind(wc.getThrottleSlider().valueProperty());
		sliderType.bind(wc.getSliderType());
		autopilotScript.bind(wc.getFileTxt().textProperty());
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(((String) arg).equals("connectionStatus")) {
			wc.setConnectionTxt("Connected", Color.GREEN);
		}
		if(((String) arg).equals("coordinates")) {
			Model model = (Model) o;
			wc.redrawAirplane(model.normalizedCanvasX, model.normalizedCanvasY);
		}
		if(((String) arg).equals("matrix_solution")) {
			Model model = (Model) o;
			wc.drawPath(model.pathDirections.split(","));
		}
	}
	
	public void dragJoystick() throws IOException {
		Point2D point = new Point2D(joystickDragX.getValue(), joystickDragY.getValue());
		m.dragJoystick(point);
	}
	public void sliderDrag() throws IOException {
		if(sliderType.getValue().equals("throttle"))
			m.sliderDrag(throttleSliderValue.getValue(),sliderType.getValue());
		else
			m.sliderDrag(rudderSliderValue.getValue(),sliderType.getValue());
		
	}
	public void connect(String ipTxt, String portTxt) throws IOException {
		m.connect(ipTxt, portTxt);
	}
	public void interpretAutoPilot() throws IOException {
		m.interpretAutoPilot(autopilotScript.getValue());
	}
	public boolean checkConnection() throws IOException {
		return m.checkConnection();
	}
	public void mapLoaded(double startPlaneLocX, double startPlaneLocY, double cubicSize, double cellHeight, double cellWidth) {
		m.mapLoaded(startPlaneLocX, startPlaneLocY, cubicSize, cellHeight, cellWidth);
	}
	public void calculatePathBtn(String calcIp, String calcPort,double lastClickedX, double lastClickedY, int[][] mapHeightsArr) throws UnknownHostException, IOException, InterruptedException {
		m.calculatePathBtn(calcIp, calcPort, lastClickedX, lastClickedY, mapHeightsArr);
		
	}

}

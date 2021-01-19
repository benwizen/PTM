package viewmodel;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import model.Model;
import view.MainWindowController;

public class ViewModel extends Observable implements Observer {

	Model m;
	MainWindowController windowController;
	public ViewModel(Model m, MainWindowController windowController) {
		this.m = m;
		this.windowController = windowController;
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(((String) arg).equals("connectionStatus")) {
			windowController.setConnectionTxt("Connected", Color.GREEN);
		}
		if(((String) arg).equals("coordinates")) {
			Model model = (Model) o;
			MainWindowController.redrawAirplane(model.normalizedCanvasX, model.normalizedCanvasY);
		}
		if(((String) arg).equals("matrix_solution")) {
			Model model = (Model) o;
			windowController.drawPath(model.pathDirections.split(","));
		}
	}
	
	public void dragJoystick(Point2D point) throws IOException {
		m.dragJoystick(point);
	}
	public void sliderDrag(double value, String slider) throws IOException {
		m.sliderDrag(value,slider);
	}
	public void connect(String ipTxt, String portTxt) throws IOException {
		m.connect(ipTxt, portTxt);
	}
	public void interpretAutoPilot(String script) throws IOException {
		m.interpretAutoPilot(script);
	}
	public boolean checkConnection() throws IOException {
		return m.checkConnection();
	}
	public void mapLoaded(double startPlaneLocX, double startPlaneLocY, double cubicSize, double cellHeight, double cellWidth) {
		m.mapLoaded(startPlaneLocX, startPlaneLocY, cubicSize, cellHeight, cellWidth);
	}
	public void calculatePathBtn(double lastClickedX, double lastClickedY, int[][] mapHeightsArr) throws UnknownHostException, IOException, InterruptedException {
		m.calculatePathBtn(lastClickedX, lastClickedY, mapHeightsArr);
		
	}

}

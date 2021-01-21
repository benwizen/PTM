package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.dgc.VMID;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.text.*;



public class MainWindowController extends Observable{
	
	@FXML
	private Circle innerCircle;
	@FXML
	private Circle outerCircle;
	@FXML
	private Slider throttleSlider;
	@FXML
	private Slider rudderSlider;
	@FXML
	private Text connectionStatusTxt;
	@FXML
	private Button loadFileBtn;
	@FXML
	private TextArea fileTxt;
	@FXML
	private RadioButton manualRb;
	@FXML
	private Button calcBtn;
	@FXML
	private RadioButton autopilotRb;
	@FXML
	private Canvas mapCanvas;
	@FXML
	private Canvas selectedLocationCanvas;
	@FXML
	private Canvas airplaneCanvas;
	private static int[][] mapHeightsArr;
	private DoubleProperty lastClickedX;
	private DoubleProperty lastClickedY;
	private DoubleProperty joystickDragX;
	private DoubleProperty joystickDragY;
	private StringProperty sliderType;
	private static double startPlaneLocX, startPlaneLocY;
	private double cubicSize;
	private double boundsX = 0, boundsY = 0;
	private static double cellWidth = 0;
	private static double cellHeight = 0;
	private static double lastPlaneX = 0;
	private static double lastPlaneY = 0;
	private static String calcIp;
	private static String calcPort;
	private boolean mapDrawn = false;
	@FXML
	private FlightMap flightMapInstance;
    private GraphicsContext locationContext;
	public void setConnectionTxt(String txt, Color color) {
		connectionStatusTxt.setText(txt);
		connectionStatusTxt.setFill(color);
	}

	@FXML
	private void initialize() {
		lastClickedX = new SimpleDoubleProperty();
		lastClickedY = new SimpleDoubleProperty();
		joystickDragX = new SimpleDoubleProperty();
		joystickDragY = new SimpleDoubleProperty();
		sliderType = new SimpleStringProperty();
		Image innerImg = new Image(getClass().getResourceAsStream("joystick-blue.png"));
		Image outerImg = new Image(getClass().getResourceAsStream("joystick-base.png"));
		innerCircle.setFill(new ImagePattern(innerImg, 0, 0, 1, 1, true));
		outerCircle.setFill(new ImagePattern(outerImg, 0, 0, 1, 1, true));
	}
	
	public TextArea getFileTxt() {
		return fileTxt;
	}

	public static int[][] getMapHeightsArr() {
		return mapHeightsArr;
	}

	public static void setMapHeightsArr(int[][] mapHeightsArr) {
		MainWindowController.mapHeightsArr = mapHeightsArr;
	}

	public Slider getThrottleSlider() {
		return throttleSlider;
	}

	public Slider getRudderSlider() {
		return rudderSlider;
	}

	public DoubleProperty getJoystickDragX() {
		return joystickDragX;
	}

	public DoubleProperty getJoystickDragY() {
		return joystickDragY;
	}
	

	public StringProperty getSliderType() {
		return sliderType;
	}

	private boolean checkConnection() throws IOException {
    	if(!Main.vm.checkConnection()) {
    		Alert errorAlert = new Alert(AlertType.ERROR);
    		errorAlert.setHeaderText("Connection Error");
    		errorAlert.setContentText("Not connected to server");
    		errorAlert.showAndWait();
    		return false;
    	}
    	return true;
	}
	private boolean checkManual() throws IOException {
    	if(!manualRb.isSelected()) {
    		Alert errorAlert = new Alert(AlertType.ERROR);
    		errorAlert.setHeaderText("Manual mode not selected");
    		errorAlert.setContentText("Toggle Manual Mode");
    		errorAlert.showAndWait();
    		return false;
    	}
    	return true;
	}
    public static String readFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }


	@FXML
	private void loadFileClicked() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(loadFileBtn.getScene().getWindow());
        if(file != null) {
        	fileTxt.setText(readFile(file.getAbsolutePath()));
        	autopilotRb.setDisable(false);
        }
 
	}
    @FXML
    private void autopilotSelected() throws IOException {
    	Main.vm.interpretAutoPilot();
    }
    
    public void redrawAirplane(double x, double y) {
    	if(this.mapDrawn) {
    		flightMapInstance.redrawAirplane(x,y);
    	}
    	
    }
    @FXML
    private void loadDataClicked() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(loadFileBtn.getScene().getWindow());
        if(file != null) {
        	calcBtn.setDisable(false);
        }
        mapDrawn = true;
        flightMapInstance.drawMap(file);
        lastClickedX.bind(flightMapInstance.getLastClickedX());
        lastClickedY.bind(flightMapInstance.getLastClickedY());
        Main.vm.mapLoaded(flightMapInstance.getStartPlaneLocX().getValue(),
        				  flightMapInstance.getStartPlaneLocY().getValue(), 
        				  flightMapInstance.getCubicSize().getValue(), 
        				  flightMapInstance.getCellHeight().getValue(),
        				  flightMapInstance.getCellWidth().getValue());
        
        
    }
    public double getStartX() {
    	return startPlaneLocX;
    }
    public double getStartY() {
    	return startPlaneLocY;
    }
    public double getcubicSize() {
    	return cubicSize;
    }
    public double getCellHeight() {
    	return cellHeight;
    }
    public static double getCellWidth() {
    	return cellWidth;
    }
    public Canvas getMapCanvas() {
    	return this.mapCanvas;
    }
	public static void setCalcIp(String calcIp) {
		MainWindowController.calcIp = calcIp;
	}

	public static void setCalcPort(String calcPort) {
		MainWindowController.calcPort = calcPort;
	}
	
    public static String getCalcIp() {
		return calcIp;
	}

	public static String getCalcPort() {
		return calcPort;
	}

	@FXML
    private void calculatePathBtn() throws UnknownHostException, IOException, InterruptedException {
        Stage popupSave = new Stage();
        popupSave.setResizable(false);
        popupSave.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ConnectSolverPopup.fxml"));
        Parent root = loader.load();
        ConnectSolverPopup controller = loader.getController();
        Scene scene = new Scene(root);
        popupSave.setScene(scene);
        popupSave.showAndWait();
    	Main.vm.calculatePathBtn(calcIp, calcPort, lastClickedX.getValue(), lastClickedY.getValue(), mapHeightsArr);
    }
    @FXML
    private void dragged(MouseEvent me) throws IOException {
    	if(!checkManual()) {
    		return;
    	}
    	if(!checkConnection()) {
    		return;
    	}
    	double mouseX = me.getSceneX() - innerCircle.getLayoutX();
    	double mouseY = me.getSceneY() - innerCircle.getLayoutY();
    	if(mouseX > outerCircle.getRadius()/1.5|| mouseX < -outerCircle.getRadius()/1.5 || mouseY > outerCircle.getRadius()/1.5|| mouseY <-outerCircle.getRadius()/1.5) {
    		innerCircle.setCenterX(boundsX);
    		innerCircle.setCenterY(boundsY);
    	}
    	else {
    		boundsX = mouseX;
    		boundsY = mouseY;
    		innerCircle.setCenterX(mouseX);
    		innerCircle.setCenterY(mouseY);
    		joystickDragX.setValue(mouseX / (outerCircle.getRadius()));
    		joystickDragY.setValue(mouseY / (outerCircle.getRadius()));
    		Main.vm.dragJoystick();
    	}
    	
    }
    @FXML
    private void released(MouseEvent me) throws IOException{
		innerCircle.setCenterX(0);
		innerCircle.setCenterY(0);
		joystickDragX.setValue(0);
		joystickDragY.setValue(0);
		Main.vm.dragJoystick();
    	
    }
    @FXML
    private void throttleDrag() throws IOException {
    	if(!checkManual()) {
    		return;
    	}
    	if(!checkConnection()) {
    		return;
    	}
    	sliderType.setValue("throttle");
    	Main.vm.sliderDrag();
    }
    @FXML
    private void rudderDrag() throws IOException {
    	if(!checkManual()) {
    		return;
    	}
    	if(!checkConnection()) {
    		return;
    	}
    	sliderType.setValue("rudder");
    	Main.vm.sliderDrag();
    }
    public void drawPath(String[] path) {
    	if(flightMapInstance != null)
    		flightMapInstance.drawPath(path);
    }
    @FXML
    private void connectClicked() throws IOException {
        Stage popupSave = new Stage();
        popupSave.setResizable(false);
        popupSave.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ConnectPopup.fxml"));
        Parent root = loader.load();
        ConnectPopup controller = loader.getController();
        Scene scene = new Scene(root);
        popupSave.setScene(scene);
        popupSave.showAndWait();
    }
}

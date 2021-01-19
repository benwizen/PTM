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
	private int[][] mapHeightsArr;
	private double lastClickedX = 0, lastClickedY = 0;
	private static double startPlaneLocX, startPlaneLocY;
	private double cubicSize;
	private double boundsX = 0, boundsY = 0;
	private static double cellWidth = 0;
	private static double cellHeight = 0;
	private static double lastPlaneX = 0;
	private static double lastPlaneY = 0;
    private static Image airplaneImage;
    private Image selectedLocationImage = new Image("file:resources/selectedLocation.png");
    private static GraphicsContext airplaneGc;
    private GraphicsContext locationContext;
	public void setConnectionTxt(String txt, Color color) {
		connectionStatusTxt.setText(txt);
		connectionStatusTxt.setFill(color);
	}
	private ArrayList<String[]> parseCsv(File csvFile) throws IOException{
		List<String> lines = Files.readAllLines(csvFile.toPath());
		ArrayList<String[]> csvFileSplitted = new ArrayList<String[]>();
		for(String line : lines) {
			csvFileSplitted.add(line.split(","));
		}
		return csvFileSplitted;
	}
	@FXML
	private void initialize() {
		airplaneImage = new Image("file:resources/airplane.png");
		Image innerImg = new Image("file:resources/joystick-blue.png");
		Image outerImg = new Image("file:resources/joystick-base.png");
		innerCircle.setFill(new ImagePattern(innerImg, 0, 0, 1, 1, true));
		outerCircle.setFill(new ImagePattern(outerImg, 0, 0, 1, 1, true));
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
    	Main.vm.interpretAutoPilot(fileTxt.getText());
    }
    @FXML
    private void loadDataClicked() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(loadFileBtn.getScene().getWindow());
        if(file != null) {
        	calcBtn.setDisable(false);
        }
        ArrayList<String[]> parsedCsv = parseCsv(file);
        startPlaneLocX = Double.valueOf(parsedCsv.get(0)[0]);
        startPlaneLocY = Double.valueOf(parsedCsv.get(0)[1]);
        cubicSize = Double.valueOf(parsedCsv.get(1)[0]);
        
        int size = parsedCsv.get(2).length;
        
        int k = 0;
        int[][] mapHeights = new int[parsedCsv.size()][parsedCsv.get(2).length];
        for(int i = 2; i < (parsedCsv.size()); i++, k++) {
        	for(int j = 0; j < parsedCsv.get(2).length; j++) {
        		mapHeights[k][j] = Integer.parseInt(parsedCsv.get(i)[j]);
        	}
        }
        
        mapHeightsArr = mapHeights;
        
        double CanvasHeight = mapCanvas.getHeight();
        double CanvasWidth = mapCanvas.getWidth();
        cellHeight = CanvasHeight / mapHeights.length;
        cellWidth = CanvasWidth / mapHeights[0].length;
        
        GraphicsContext mapGc = mapCanvas.getGraphicsContext2D();
        airplaneGc = airplaneCanvas.getGraphicsContext2D();
        
        int minHeight = Integer.MAX_VALUE;
        int maxHeight = 0;
        
        for(int i = 0 ; i < mapHeights.length;i++) {
        	for(int j = 0 ; j < mapHeights[0].length; j++) {
        		if(mapHeights[i][j] > maxHeight)
        			maxHeight = mapHeights[i][j];
        		if(mapHeights[i][j] < minHeight)
        			minHeight = mapHeights[i][j];
        	}
        }
        
        
        
        for (int i = 0; i < mapHeights.length; i++) {
            for (int j = 0; j < mapHeights[0].length; j++) {
                int height = mapHeights[i][j];
                double normalizedHeight = 255*(((double)height - (double)minHeight)/((double)maxHeight - (double)minHeight));
                int redColor = (255 - (int)normalizedHeight);
                int greenColor = (int)normalizedHeight;
                mapGc.setFill(Color.rgb(redColor, greenColor, 0));
                mapGc.fillRect((j * cellWidth), (i * cellHeight), cellWidth, cellHeight);
            }
        }
        Main.vm.mapLoaded(startPlaneLocX, startPlaneLocY, cubicSize, cellHeight, cellWidth);
        
        
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
    @FXML
    private void locationClicked(MouseEvent me) {
    	lastClickedX = me.getX() - 10;
    	lastClickedY = me.getY() - 10;
    	locationContext = selectedLocationCanvas.getGraphicsContext2D();
    	locationContext.clearRect(0, 0, locationContext.getCanvas().getWidth(), locationContext.getCanvas().getHeight());
    	locationContext.drawImage(selectedLocationImage, lastClickedX, lastClickedY, 30, 30);
    }
    @FXML
    private void calculatePathBtn() throws UnknownHostException, IOException, InterruptedException {
    	Main.vm.calculatePathBtn(lastClickedX, lastClickedY, mapHeightsArr);
    }
    public static void redrawAirplane(double x, double y) {
    	lastPlaneX = x;
    	lastPlaneY = y;
    	airplaneGc.clearRect(0, 0, airplaneGc.getCanvas().getWidth(), airplaneGc.getCanvas().getHeight());
    	airplaneGc.drawImage(airplaneImage, x / cellWidth, y / cellHeight,30 ,30);
    }
    @FXML
    private void dragged(MouseEvent me) throws IOException {
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
    		Point2D draggedPoint = new Point2D(mouseX / (outerCircle.getRadius()),mouseY / (outerCircle.getRadius()));
    		Main.vm.dragJoystick(draggedPoint);
    	}
    	
    }
    @FXML
    private void released(MouseEvent me) throws IOException{
		innerCircle.setCenterX(0);
		innerCircle.setCenterY(0);
		Main.vm.dragJoystick(new Point2D(0,0));
    	
    }
    @FXML
    private void throttleDrag() throws IOException {
    	if(!checkConnection()) {
    		return;
    	}
    	Main.vm.sliderDrag(throttleSlider.getValue(), "throttle");
    }
    @FXML
    private void rudderDrag() throws IOException {
    	if(!checkConnection()) {
    		return;
    	}
    	Main.vm.sliderDrag(rudderSlider.getValue(), "rudder");
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
	public void drawPath(String[] path) {
		locationContext.clearRect(0, 0,locationContext.getCanvas().getWidth(), locationContext.getCanvas().getHeight());
		double xPlane =  (lastPlaneX / cellWidth) + (8*cellWidth);
		double yPlane = (lastPlaneY / cellHeight) + (15*cellHeight);
		locationContext.beginPath();
		locationContext.moveTo(xPlane, yPlane);
		
		for(String direction : path) {
			if(direction.equals("Up")) {
				yPlane = yPlane - cellHeight;
			}
			else if(direction.equals("Down")) {
				yPlane = yPlane + cellHeight;
			}
			else if(direction.equals("Left")) {
				xPlane = xPlane + cellWidth;
			}
			else if(direction.equals("Right")) {
				xPlane = xPlane + cellWidth;
			}
			locationContext.lineTo(xPlane, yPlane);
		}
		locationContext.stroke();
		locationContext.closePath();
		locationContext.drawImage(selectedLocationImage, lastClickedX, lastClickedY, 30, 30);
		
	}
}

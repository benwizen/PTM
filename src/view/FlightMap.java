package view;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class FlightMap extends StackPane{

	private double diff = 0.016;
	private final Image airplaneImage = new Image(getClass().getResourceAsStream("airplane.png"));
    private final Image selectedLocationImage = new Image(getClass().getResourceAsStream("selectedLocation.png"));
	private Canvas mapCanvas;
	private Canvas airplaneCanvas;
	private Canvas selectedLocationCanvas;
	private GraphicsContext airplaneGc;
	private GraphicsContext locationGc;
	private DoubleProperty cellWidth;
	private DoubleProperty cellHeight;
	private DoubleProperty lastPlaneX;
	private DoubleProperty lastPlaneY;
	private DoubleProperty startPlaneLocX;
	private DoubleProperty startPlaneLocY;
	private DoubleProperty lastClickedX;
	private DoubleProperty lastClickedY;
	private DoubleProperty cubicSize;
	private BooleanProperty isCalculated;
	private boolean mapDrawn = false;
	private int[][] mapHeightsArr;
	
	
	public DoubleProperty getCubicSize() {
		return cubicSize;
	}
	public void setCubicSize(DoubleProperty cubicSize) {
		this.cubicSize = cubicSize;
	}
	public DoubleProperty getCellWidth() {
		return cellWidth;
	}
	public void setCellWidth(DoubleProperty cellWidth) {
		this.cellWidth = cellWidth;
	}
	public DoubleProperty getCellHeight() {
		return cellHeight;
	}
	public void setCellHeight(DoubleProperty cellHeight) {
		this.cellHeight = cellHeight;
	}
	public DoubleProperty getLastPlaneX() {
		return lastPlaneX;
	}
	public void setLastPlaneX(DoubleProperty lastPlaneX) {
		this.lastPlaneX = lastPlaneX;
	}
	public DoubleProperty getLastPlaneY() {
		return lastPlaneY;
	}
	public void setLastPlaneY(DoubleProperty lastPlaneY) {
		this.lastPlaneY = lastPlaneY;
	}
	public DoubleProperty getStartPlaneLocX() {
		return startPlaneLocX;
	}
	public void setStartPlaneLocX(DoubleProperty startPlaneLocX) {
		this.startPlaneLocX = startPlaneLocX;
	}
	public DoubleProperty getStartPlaneLocY() {
		return startPlaneLocY;
	}
	public void setStartPlaneLocY(DoubleProperty startPlaneLocY) {
		this.startPlaneLocY = startPlaneLocY;
	}
	public DoubleProperty getLastClickedX() {
		return lastClickedX;
	}
	public void setLastClickedX(DoubleProperty lastClickedX) {
		this.lastClickedX = lastClickedX;
	}
	public DoubleProperty getLastClickedY() {
		return lastClickedY;
	}
	public void setLastClickedY(DoubleProperty lastClickedY) {
		this.lastClickedY = lastClickedY;
	}
	
	private void initMap() {
		cellWidth = new SimpleDoubleProperty();
		cellHeight = new SimpleDoubleProperty();
		lastPlaneX = new SimpleDoubleProperty();
		lastPlaneY = new SimpleDoubleProperty();
		startPlaneLocX = new SimpleDoubleProperty();
		startPlaneLocY = new SimpleDoubleProperty();
		lastClickedX = new SimpleDoubleProperty();
		lastClickedY = new SimpleDoubleProperty();
		cubicSize = new SimpleDoubleProperty();
		isCalculated = new SimpleBooleanProperty(false);
	}
	
	private ArrayList<String[]> parseCsv(File csvFile) throws IOException{
		List<String> lines = Files.readAllLines(csvFile.toPath());
		ArrayList<String[]> csvFileSplitted = new ArrayList<String[]>();
		for(String line : lines) {
			csvFileSplitted.add(line.split(","));
		}
		return csvFileSplitted;
	}
    public void redrawAirplane(double x, double y) {
    	if(airplaneGc != null) {
        	lastPlaneX.setValue(x);
        	lastPlaneY.setValue(y);
        	airplaneGc.clearRect(0, 0, airplaneGc.getCanvas().getWidth(), airplaneGc.getCanvas().getHeight());
        	airplaneGc.drawImage(airplaneImage, lastPlaneX.getValue(), lastPlaneY.getValue(),30 ,30);
    	}

    }
    private void locationClicked(MouseEvent me) throws UnknownHostException, IOException, InterruptedException {
    	if(!mapDrawn)
    		return;
    	lastClickedX.setValue(me.getX() - 10);
    	lastClickedY.setValue(me.getY() - 10);
    	if(!isCalculated.getValue()) {
    		isCalculated.setValue(true);
        	locationGc = selectedLocationCanvas.getGraphicsContext2D();
        	locationGc.clearRect(0, 0, locationGc.getCanvas().getWidth(), locationGc.getCanvas().getHeight());
        	locationGc.drawImage(selectedLocationImage, lastClickedX.getValue(), lastClickedY.getValue(), 30, 30);
    	}
    	else {
    		Main.vm.calculatePathBtn(MainWindowController.getCalcIp(),MainWindowController.getCalcPort() ,lastClickedX.getValue(), lastClickedY.getValue(), mapHeightsArr);
    	}
    }
	public void drawPath(String[] path) {
		locationGc.clearRect(0, 0,locationGc.getCanvas().getWidth(), locationGc.getCanvas().getHeight());
		double xPlane =  (lastPlaneX.getValue() / cellWidth.getValue()) + (10*cellWidth.getValue());
		double yPlane = (lastPlaneY.getValue() / cellHeight.getValue()) + (15*cellHeight.getValue());
		locationGc.beginPath();
		locationGc.moveTo(xPlane, yPlane);
		
		for(String direction : path) {
			if(direction.equals("Up")) {
				yPlane = yPlane - cellHeight.getValue();
			}
			else if(direction.equals("Down")) {
				yPlane = yPlane + cellHeight.getValue();
			}
			else if(direction.equals("Left")) {
				xPlane = xPlane + cellWidth.getValue();
			}
			else if(direction.equals("Right")) {
				xPlane = xPlane + cellWidth.getValue();
			}
			locationGc.lineTo(xPlane, yPlane);
		}
		locationGc.stroke();
		locationGc.closePath();
		locationGc.drawImage(selectedLocationImage, lastClickedX.getValue(), lastClickedY.getValue() - 10, 30, 30);
		
	}
	public void drawMap(File mapFile) throws IOException {
		this.initMap();
		mapCanvas = new Canvas(this.getPrefWidth(),this.getPrefHeight());
		airplaneCanvas = new Canvas(this.getPrefWidth(),this.getPrefHeight());
		selectedLocationCanvas = new Canvas(this.getPrefWidth(),this.getPrefHeight());
		mapDrawn = true;
		
		this.getChildren().add(mapCanvas);
		this.getChildren().add(airplaneCanvas);
		this.getChildren().add(selectedLocationCanvas);
		
		selectedLocationCanvas.toFront();
		
		selectedLocationCanvas.setOnMouseClicked((event) -> {
			try {
				locationClicked(event);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		});		
		
		
        ArrayList<String[]> parsedCsv = parseCsv(mapFile);
        startPlaneLocX.setValue(Double.valueOf(parsedCsv.get(0)[0]));
        startPlaneLocY.setValue(Double.valueOf(parsedCsv.get(0)[1]));
        cubicSize.setValue(Double.valueOf(parsedCsv.get(1)[0]));
        
        int size = parsedCsv.get(2).length;
        
        int k = 0;
        int[][] mapHeights = new int[parsedCsv.size()][parsedCsv.get(2).length];
        for(int i = 2; i < (parsedCsv.size()); i++, k++) {
        	for(int j = 0; j < parsedCsv.get(2).length; j++) {
        		mapHeights[k][j] = Integer.parseInt(parsedCsv.get(i)[j]);
        	}
        }
        
        mapHeightsArr = mapHeights;
        MainWindowController.setMapHeightsArr(mapHeightsArr);
        
        double CanvasHeight = mapCanvas.getHeight();
        double CanvasWidth = mapCanvas.getWidth();
        cellHeight.setValue(CanvasHeight / mapHeights.length);
        cellWidth.setValue(CanvasWidth / mapHeights[0].length);
        
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
                mapGc.fillRect((j * cellWidth.getValue()), (i * cellHeight.getValue()), cellWidth.getValue(), cellHeight.getValue());
            }
        }
	}
}

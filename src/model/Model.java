package model;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Observable;
import java.util.stream.Collectors;

import clientConnect.ClientSocket;
import javafx.geometry.Point2D;

import command.OpenDataServerCommand;
import javafx.beans.property.StringProperty;
import interperter.Interperter;

public class Model extends Observable{
	
		public boolean stop = false;
		public double normalizedCanvasX = 0, normalizedCanvasY= 0;
		private double startPlaneLocX = 0;
		private double startPlaneLocY = 0;
		private double cubicSize = 0;
		private double cellHeight = 0;
		private double planeX =0 ;
		private double planeY = 0;
		private double cellWidth = 0;
		public String pathDirections = "";
		public Model() {

		}
	
		public void connect(String ip, String port) throws IOException {
			Interperter.Commands.get("openDataServer").doCommand(new String[] {"openDataServer", "5400", "10"});
			while(!((OpenDataServerCommand)Interperter.Commands.get("openDataServer")).connected) {
				
			}
			Interperter.Commands.get("connect").doCommand(new String[] {"connect", ip, port});
			setChanged();
			notifyObservers("connectionStatus");
		}
		public void dragJoystick(Point2D point) throws IOException {
			Interperter.SimVariables.get("/controls/flight/aileron").setValue(String.valueOf(point.getX()),false);
			Interperter.SimVariables.get("/controls/flight/elevator").setValue(String.valueOf(-point.getY()),false);
		}
		public void sliderDrag(double value, String slider) throws IOException {
			if(slider.equals("throttle"))
				Interperter.SimVariables.get("/controls/engines/current-engine/throttle").setValue(String.valueOf(value),false);
			else
				Interperter.SimVariables.get("/controls/flight/rudder").setValue(String.valueOf(value),false);
		}
		public void interpretAutoPilot(String script) throws IOException {
			new Thread(()->{
					try {
						String[] lines = script.split(System.getProperty("line.separator"));
						Interperter.parser(lines);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}).start();

		}
		public boolean checkConnection() {
			return (ClientSocket.s != null && !ClientSocket.s.isClosed());
		}
		public void mapLoaded(double startPlaneLocX, double startPlaneLocY, double cubicSize, double cellHeight, double cellWidth) {
			this.cellHeight = cellHeight;
			this.cubicSize = cubicSize;
			this.cellWidth = cellWidth;
			this.startPlaneLocX = startPlaneLocX;
			this.startPlaneLocY = startPlaneLocY;
			new Thread(()->{
				while(!this.stop){
					try {
						
						planeX = Double.valueOf(Interperter.SimVariables.get("/position/longitude-deg/").getValue());
						planeY = Double.valueOf(Interperter.SimVariables.get("/position/latitude-deg/").getValue());
						normalizedCanvasX = (planeX - startPlaneLocX) * cubicSize / cellWidth;
						normalizedCanvasY = -((planeY - startPlaneLocY) * cubicSize / cellHeight);
						setChanged();
						notifyObservers("coordinates");
						Thread.sleep(250);
					} catch (InterruptedException e) {e.printStackTrace();}
				}

			}).start();
		}

		public void calculatePathBtn(double lastClickedX, double lastClickedY, int[][] mapHeightsMat) throws UnknownHostException, IOException, InterruptedException {
			Socket clientSocket = new Socket("127.0.0.1", 10000);
			PrintWriter clientPrinter = new PrintWriter(clientSocket.getOutputStream());
			for(int i = 0; i<mapHeightsMat.length; i++) {
				String row = Arrays.stream(mapHeightsMat[i]).mapToObj(String::valueOf).collect(Collectors.joining(","));
				clientPrinter.print(row + Interperter.lineSeperator);
				clientPrinter.flush();
			}
			clientPrinter.print("end" + Interperter.lineSeperator);
			clientPrinter.flush();
			clientPrinter.print((int) (normalizedCanvasY) + "," + (int) (normalizedCanvasX) + Interperter.lineSeperator);
			clientPrinter.flush();
			clientPrinter.print((int)(lastClickedY/cellHeight) + "," + (int)(lastClickedX/cellWidth) + Interperter.lineSeperator);
			clientPrinter.flush();
			BufferedReader in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while((pathDirections = in.readLine()) == null) {
				Thread.sleep(250);
			}
			clientSocket.close();
			setChanged();
			notifyObservers("matrix_solution");
			
		}
}

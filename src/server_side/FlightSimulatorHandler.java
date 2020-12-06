package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import interperter.Interperter;

public class FlightSimulatorHandler {
	private int wait;
	
	public FlightSimulatorHandler(int wait) {
		this.wait = wait;
	}
	
	public void handleClient(InputStream in, OutputStream out) throws IOException {
		BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(in));
        String client_input;
        try {
            while (!(client_input = buffered_reader.readLine()).equals("bye")) {
                Interperter.parser(Interperter.lexer(client_input));
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("FlightSimulatorHandler has stopped");
        }
        catch (NullPointerException e) {}
	}
}

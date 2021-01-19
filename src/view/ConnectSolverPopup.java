package view;

import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ConnectSolverPopup {
	@FXML
	private TextField portTxt;
	@FXML
	private TextField ipTxt;
	@FXML
	private Button connectBtn;
	
	@FXML
	private void connect() throws IOException {
		MainWindowController.setCalcIp(ipTxt.getText());
		MainWindowController.setCalcPort(portTxt.getText());
		Stage stage = ((Stage) connectBtn.getScene().getWindow());
		stage.close();
	}

}

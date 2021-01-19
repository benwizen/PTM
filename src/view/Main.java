package view;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;
import model.Model;
import viewmodel.ViewModel;

public class Main extends Application {
	public static ViewModel vm;
	public static Model m;
	@SuppressWarnings("deprecation")
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
			//GridPane root = (GridPane)FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
			AnchorPane root = (AnchorPane)fxmlLoader.load();
			MainWindowController windowController = fxmlLoader.getController();
			Scene scene = new Scene(root,1000,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			m = new Model();
			
			vm = new ViewModel(m, windowController);
			m.addObserver(vm);
			vm.addObserver(vm);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

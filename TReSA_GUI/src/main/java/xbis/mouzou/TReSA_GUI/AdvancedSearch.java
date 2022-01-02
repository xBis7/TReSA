package xbis.mouzou.TReSA_GUI;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdvancedSearch {

	public static void advSearchWindow(Stage stage) {
		
		Stage advSearchWin = new Stage();
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane, 1080, 820);
		
		stage.setMinHeight(600);
        stage.setMinWidth(700);     
        
        
        advSearchWin.setTitle("Advanced Search");
        advSearchWin.setScene(scene);
        advSearchWin.show();
		
	}

	
}

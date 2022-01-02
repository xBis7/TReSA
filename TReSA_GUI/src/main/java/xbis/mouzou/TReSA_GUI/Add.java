package xbis.mouzou.TReSA_GUI;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Add {

	public static void addWindow(Stage stage) {
		
		Stage addWin = new Stage();
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane, 1080, 820);
		
		stage.setMinHeight(600);
        stage.setMinWidth(700);     
        
        addWin.setTitle("Add new article");
        addWin.setScene(scene);
        addWin.show();
		
	}

	
}

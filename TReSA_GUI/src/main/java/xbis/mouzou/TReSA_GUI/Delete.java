package xbis.mouzou.TReSA_GUI;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Delete {

	public static void deleteWindow(Stage stage) {
		
		Stage deleteWin = new Stage();
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane, 1080, 820);
		
		stage.setMinHeight(600);
        stage.setMinWidth(700);     
        
        
        deleteWin.setTitle("Delete article");
        deleteWin.setScene(scene);
        deleteWin.show();
		
	}

	
}

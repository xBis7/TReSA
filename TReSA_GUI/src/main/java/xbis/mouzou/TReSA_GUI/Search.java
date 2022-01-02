package xbis.mouzou.TReSA_GUI;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import xbis.mouzou.TReSA_Lucene.LuceneTester;

public class Search {
	
	public static void searchWindow(Stage stage, String query) {
		
		Stage searchWin = new Stage();
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane, 1080, 820);
		
		stage.setMinHeight(600);
        stage.setMinWidth(700);     
        
        LuceneTester search = new LuceneTester();
        search.tester(query);
        
        searchWin.setTitle("Search Results");
        searchWin.setScene(scene);
        searchWin.show();
		
	}

}

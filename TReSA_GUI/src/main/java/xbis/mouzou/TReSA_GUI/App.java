package xbis.mouzou.TReSA_GUI;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import xbis.mouzou.TReSA_Lucene.LuceneTester;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
    	LuceneTester tester = new LuceneTester();
    	//create index file
    	try {
			tester.createIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Search.newSearchWindow(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }

}
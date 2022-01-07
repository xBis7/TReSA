package xbis.mouzou.TReSA_GUI;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
    	Search.newSearchWindow(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }

}
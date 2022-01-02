package xbis.mouzou.TReSA_GUI;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import xbis.mouzou.TReSA_Lucene.*;

public class App extends Application {
	
	public static String searchQuery;
	
	TextField searchField = new TextField();

    @Override
    public void start(Stage stage) {

        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane, 1080, 860);

        stage.setMinHeight(600);
        stage.setMinWidth(700);        

        Label tresaLabel = new Label("The Reuters Search Assistant");

        

        searchField.setPromptText("Enter query");

        VBox vbox = new VBox();

        HBox hbox = new HBox();

        Button searchButton = new Button("Search");
        searchButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        Button addButton = new Button("Add");
        addButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        Button deleteButton = new Button("Delete");
        deleteButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        stackPane.getChildren().addAll(vbox);

        vbox.getChildren().addAll(tresaLabel, searchField, hbox);

        hbox.getChildren().addAll(searchButton, addButton, deleteButton);

        tresaLabel.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 28));

        searchField.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        tresaLabel.setTextAlignment(TextAlignment.CENTER);

        vbox.setAlignment(Pos.CENTER);

        hbox.setAlignment(Pos.CENTER);

        stackPane.setPadding(new Insets(10, 10, 10, 10));

        vbox.setSpacing(30);

        hbox.setSpacing(20);

        searchButton.setPrefSize(100.0, 50.0);

        addButton.setPrefSize(100.0, 50.0);

        deleteButton.setPrefSize(100.0, 50.0);

        searchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				searchQuery = searchField.getText();
				
				if(!searchQuery.isEmpty()) {
					LuceneTester search = new LuceneTester();

					new Thread(){
	                    public void run(){
	                    	search.tester(searchQuery);
	                    }
	                }.start();
					
				}
			}
        	
        });
        
        stage.setTitle("TReSA");
        stage.setScene(scene);
        stage.show();
        
        
    }

    public static void main(String[] args) {
        launch();
    }

}
package xbis.mouzou.TReSA_GUI;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class App extends Application {
	
	public static String searchQuery;
	
	TextField searchField = new TextField();

    @Override
    public void start(Stage stage) {

        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane, 1080, 820);

        stage.setMinHeight(600);
        stage.setMinWidth(700);        

        Label tresaLabel = new Label("The Reuters Search Assistant");

        searchField.setPromptText("Enter query");

        VBox vbox = new VBox();

        HBox hbox = new HBox();

        Button searchButton = new Button("Search");
        searchButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        Button advSearchButton = new Button("Advanced \nSearch");
        searchButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        
        Button addButton = new Button("Add");
        addButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        Button deleteButton = new Button("Delete");
        deleteButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        stackPane.getChildren().addAll(vbox);

        vbox.getChildren().addAll(tresaLabel, searchField, hbox);

        hbox.getChildren().addAll(searchButton, advSearchButton, addButton, deleteButton);

        tresaLabel.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 28));

        searchField.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        tresaLabel.setTextAlignment(TextAlignment.CENTER);

        vbox.setAlignment(Pos.CENTER);

        hbox.setAlignment(Pos.CENTER);

        stackPane.setPadding(new Insets(10, 10, 10, 10));

        vbox.setSpacing(30);

        hbox.setSpacing(20);

        searchButton.setPrefSize(120.0, 60.0);
        
        advSearchButton.setPrefSize(120.0, 60.0);
        advSearchButton.setTextAlignment(TextAlignment.CENTER);

        addButton.setPrefSize(120.0, 60.0);

        deleteButton.setPrefSize(120.0, 60.0);
        
        stage.setTitle("TReSA");
        stage.setScene(scene);
        stage.show();
        
        //search button click listener
        searchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				searchQuery = searchField.getText();
				
				if(!searchQuery.isEmpty()) {	
					Search.searchWindow(stage, searchQuery);
					stage.close();
				}
				else {
					Alert alert = new Alert(AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(stage);
                    alert.setTitle("Search Error");
                    alert.setHeaderText("Empty Search Bar");
                    alert.setContentText("Make a query to the search engine");
                    alert.showAndWait();
				}
			}
        });
        
        //advanced search button click listener
        advSearchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				AdvancedSearch.advSearchWindow(stage);
				stage.close();
			}
        });
        
        //add button click listener
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Add.addWindow(stage);
				stage.close();
			}
        });
        
        //delete button click listener
        deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Delete.deleteWindow(stage);
				stage.close();
			}
        });
        
    }

    public static void main(String[] args) {
        launch();
    }

}
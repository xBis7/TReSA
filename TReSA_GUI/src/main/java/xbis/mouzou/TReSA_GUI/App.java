package xbis.mouzou.TReSA_GUI;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import xbis.mouzou.TReSA_Lucene.LuceneTester;

public class App extends Application {
	
	public static String searchQuery;
	
	TextField searchField = new TextField();

    @Override
    public void start(Stage stage) {

        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane, 1080, 820);

        stage.setMinHeight(700);
        stage.setMinWidth(900);        
        
        ComboBox<String> resultNumCombo = new ComboBox<String>();
        
        resultNumCombo.getItems().addAll("10", "20", "30", "50", "70", "100", "120");
        
        resultNumCombo.getSelectionModel().select(2);
        
        Label tresaLabel = new Label("The Reuters Search Assistant");

        searchField.setPromptText("Enter query");

        VBox vbox = new VBox();

        HBox searchHBox = new HBox();
        
        HBox buttonHBox = new HBox();

        Button searchButton = new Button("Search");
        searchButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        Button advSearchButton = new Button("Advanced \nSearch");
        searchButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        
        Button addButton = new Button("Add");
        addButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        Button deleteButton = new Button("Delete");
        deleteButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        stackPane.getChildren().addAll(vbox);

        vbox.getChildren().addAll(tresaLabel, searchHBox, buttonHBox);

        searchHBox.getChildren().addAll(searchField, resultNumCombo);
        
        buttonHBox.getChildren().addAll(searchButton, advSearchButton, addButton, deleteButton);

        tresaLabel.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 28));

        searchField.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));

        resultNumCombo.setStyle("-fx-font-size: 16");
        
        searchHBox.setHgrow(searchField, Priority.ALWAYS);
        searchHBox.setHgrow(resultNumCombo, Priority.NEVER);
        
        tresaLabel.setTextAlignment(TextAlignment.CENTER);

        vbox.setAlignment(Pos.CENTER);
        
        searchHBox.setAlignment(Pos.CENTER);

        buttonHBox.setAlignment(Pos.CENTER);

        stackPane.setPadding(new Insets(10, 10, 10, 10));

        vbox.setSpacing(30);
        
        searchHBox.setSpacing(20);

        buttonHBox.setSpacing(20);

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
	        	String result = resultNumCombo.getValue();

	        	int num = Integer.parseInt(result);
				
				if(!searchQuery.isBlank()) {
					List<Boolean> checkBoxes = new ArrayList<>();
					checkBoxes.add(false);
					checkBoxes.add(false);
					checkBoxes.add(false);
					checkBoxes.add(false);
					
					SearchResults.resultsWindow(stage, searchQuery, num, false, checkBoxes);
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
package xbis.mouzou.TReSA_GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import xbis.mouzou.TReSA_Lucene.LuceneTester;

public class AdvancedSearch {
	
	public static List <Boolean> checkBoxes = new ArrayList<>();

	public static void advSearchWindow(Stage stage) {
		
		Stage advSearchWin = new Stage();
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane, 1080, 820);
		
		advSearchWin.setMinHeight(700);
		advSearchWin.setMinWidth(900);     
        
        VBox mainVBox = new VBox();
        
        VBox placesVBox = new VBox();
        
        VBox peopleVBox = new VBox();

        VBox titleVBox = new VBox();

        VBox bodyVBox = new VBox();
        
        HBox headerHBox = new HBox();
        
        HBox placesHBox = new HBox();
        
        HBox peopleHBox = new HBox();
        
        HBox titleHBox = new HBox();
        
        HBox bodyHBox = new HBox();
        
        HBox buttonHBox = new HBox();
        
        ComboBox<String> resultNumCombo = new ComboBox<String>();
        resultNumCombo.getItems().addAll("10", "20", "30", "50", "70", "100", "120");
        resultNumCombo.getSelectionModel().select(2);
        resultNumCombo.setStyle("-fx-font-size: 16");
        
        Label advSearchLabel = new Label("Advanced Search");
        advSearchLabel.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 28));

        //places
        Label placesLabel = new Label("Search in places");
        placesLabel.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        TextField placesField = new TextField();
        placesField.setPromptText("Query places");
        placesField.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        
        CheckBox placesCB = new CheckBox("Not Exist");
        placesCB.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        placesCB.setSelected(false);
        
        //people
        Label peopleLabel = new Label("Search in people");
        peopleLabel.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        TextField peopleField = new TextField();
        peopleField.setPromptText("Query people");
        peopleField.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        
        CheckBox peopleCB = new CheckBox("Not Exist");
        peopleCB.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        peopleCB.setSelected(false);
        
        //title
        Label titleLabel = new Label("Search in title");
        titleLabel.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        TextField titleField = new TextField();
        titleField.setPromptText("Query title");
        titleField.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        
        CheckBox titleCB = new CheckBox("Not Exist");
        titleCB.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        titleCB.setSelected(false);
        
        //body
        Label bodyLabel = new Label("Search in body");
        bodyLabel.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        TextField bodyField = new TextField();
        bodyField.setPromptText("Query body");
        bodyField.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        
        CheckBox bodyCB = new CheckBox("Not Exist");
        bodyCB.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        bodyCB.setSelected(false);
        
        //buttons
        Button findButton = new Button("Find");
        findButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        findButton.setPrefSize(120.0, 60.0);
        
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        backButton.setPrefSize(120.0, 60.0);
        
        stackPane.getChildren().addAll(mainVBox);
        placesVBox.getChildren().addAll(placesHBox, placesField);
        peopleVBox.getChildren().addAll(peopleHBox, peopleField);
        titleVBox.getChildren().addAll(titleHBox, titleField);
        bodyVBox.getChildren().addAll(bodyHBox, bodyField);
        headerHBox.getChildren().addAll(advSearchLabel, resultNumCombo);
        placesHBox.getChildren().addAll(placesLabel, placesCB);
        peopleHBox.getChildren().addAll(peopleLabel, peopleCB);
        titleHBox.getChildren().addAll(titleLabel, titleCB);
        bodyHBox.getChildren().addAll(bodyLabel, bodyCB);
        buttonHBox.getChildren().addAll(findButton, backButton);
        mainVBox.getChildren().addAll(headerHBox, placesVBox, peopleVBox, titleVBox, bodyVBox, buttonHBox);
        
        mainVBox.setSpacing(30);
        placesVBox.setSpacing(10);
        peopleVBox.setSpacing(10);
        titleVBox.setSpacing(10);
        bodyVBox.setSpacing(10);
        headerHBox.setSpacing(20);
        placesHBox.setSpacing(20);
        peopleHBox.setSpacing(20);
        titleHBox.setSpacing(20);
        bodyHBox.setSpacing(20);
        buttonHBox.setSpacing(10);
        
        mainVBox.setAlignment(Pos.CENTER);
        headerHBox.setAlignment(Pos.CENTER);
        buttonHBox.setAlignment(Pos.CENTER);
        
        StackPane.setMargin(mainVBox, new Insets(20, 20, 20, 20));
        
        advSearchWin.setTitle("Advanced Search");
        advSearchWin.setScene(scene);
        advSearchWin.show();
        
        findButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
	        	
				String result = resultNumCombo.getValue();
	        	int num = Integer.parseInt(result);
	        	
	        	String places = placesField.getText();
	            String people = peopleField.getText();
	            String title = titleField.getText();
	            String body = bodyField.getText(); 
	            
	            if(places.isBlank() && people.isBlank() && title.isBlank() && body.isBlank()) {
	            	Alert alert = new Alert(AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(stage);
                    alert.setTitle("Search Error");
                    alert.setHeaderText("All fields are empty");
                    alert.setContentText("Fill at least one field to proceed with the query");
                    alert.showAndWait();
	            }
	            else {
	            	String query = places + "&" + people + "&" + title + "&" + body;
	            	
	            	if(placesCB.isSelected()) {
	            		checkBoxes.add(true);
	            	}
	            	else {
	            		checkBoxes.add(false);
	            	}
	            	
	            	if(peopleCB.isSelected()) {
	            		checkBoxes.add(true);
	            	}
	            	else {
	            		checkBoxes.add(false);
	            	}

					if(titleCB.isSelected()) {
						checkBoxes.add(true);
					}
					else {
						checkBoxes.add(false);
					}
					
					if(bodyCB.isSelected()) {
						checkBoxes.add(true);
					}
					else {
						checkBoxes.add(false);
					}
	            	
	            	SearchResults.resultsWindow(stage, query, num, true, checkBoxes);
	            	checkBoxes.clear();
	            	advSearchWin.close();
	            }
			}
        });
        
        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				App mainWin = new App();
				mainWin.start(stage);
				advSearchWin.close();	
			}
        });
		
	}

	
}

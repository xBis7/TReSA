package xbis.mouzou.TReSA_GUI;

import java.util.Optional;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import xbis.mouzou.TReSA_Lucene.LuceneTester;

public class Add {
	
	public static final String REGEX_PATTERN = "^[A-za-z0-9_]{1,255}$";

	public static void addWindow(Stage stage) {
		
		Stage addWin = new Stage();
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane, 1080, 820);
		
		addWin.setMinHeight(700);
		addWin.setMinWidth(900); 
		
		LuceneTester tester = new LuceneTester();
		
		VBox mainVBox = new VBox();
		VBox placesVBox = new VBox();
		VBox peopleVBox = new VBox();
		VBox titleVBox = new VBox();
		VBox bodyVBox = new VBox();
		HBox buttonHBox = new HBox();
		
		Label addLabel = new Label("Add new article");
		addLabel.setFont(Font.font("Courier New", FontWeight.BOLD, FontPosture.REGULAR, 28));
		
		//places
		Label placesLabel = new Label("Article section Places");
        placesLabel.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        TextField placesField = new TextField();
        placesField.setPromptText("Enter places");
        placesField.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        
        //people
        Label peopleLabel = new Label("Article section People");
        peopleLabel.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        TextField peopleField = new TextField();
        peopleField.setPromptText("Enter people");
        peopleField.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        
        //title
        Label titleLabel = new Label("Article section Title");
        titleLabel.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        TextField titleField = new TextField();
        titleField.setPromptText("Enter title");
        titleField.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        
        //body
        Label bodyLabel = new Label("Article section Body");
        bodyLabel.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Enter body");
        bodyArea.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        
        //buttons
        Button addButton = new Button("Add");
        addButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        addButton.setPrefSize(120.0, 60.0);
        
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("verdaba", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        backButton.setPrefSize(120.0, 60.0);
        
        stackPane.getChildren().addAll(mainVBox);
        placesVBox.getChildren().addAll(placesLabel, placesField);
        peopleVBox.getChildren().addAll(peopleLabel, peopleField);
        titleVBox.getChildren().addAll(titleLabel, titleField);
        bodyVBox.getChildren().addAll(bodyLabel, bodyArea);
        buttonHBox.getChildren().addAll(addButton, backButton);
        mainVBox.getChildren().addAll(addLabel, placesVBox, peopleVBox, titleVBox, bodyVBox, buttonHBox);
		
        mainVBox.setSpacing(30);
        placesVBox.setSpacing(10);
        peopleVBox.setSpacing(10);
        titleVBox.setSpacing(10);
        bodyVBox.setSpacing(10);
        buttonHBox.setSpacing(10);
        
        mainVBox.setAlignment(Pos.CENTER);
        buttonHBox.setAlignment(Pos.CENTER);
        
        StackPane.setMargin(mainVBox, new Insets(20, 20, 20, 20));
        
        addWin.setTitle("Add new article");
        addWin.setScene(scene);
        addWin.show();
        
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				String places = placesField.getText();
	            String people = peopleField.getText();
	            String title = titleField.getText();
	            String body = bodyArea.getText(); 
	            
	            if(places.isBlank() && people.isBlank() && title.isBlank() && body.isBlank()) {
	            	Alert alert = new Alert(AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.initOwner(addWin);
                    alert.setTitle("Error");
                    alert.setHeaderText("All fields are empty");
                    alert.setContentText("Fill at least one field to proceed with the entry");
                    alert.showAndWait();
	            }
	            else {
	            	TextInputDialog dialog = new TextInputDialog();
					dialog.initModality(Modality.APPLICATION_MODAL);
	                dialog.initOwner(addWin);
					dialog.setTitle("Save as");
					dialog.setHeaderText("Don't provide a file extension");
					dialog.setContentText("Enter file name: ");
					
					Optional<String> result = dialog.showAndWait();
					String fileName = "";
					if(result.isPresent()) {
						if(result.get().matches(REGEX_PATTERN)) {
							fileName = result.get();
						}
						else {
							Alert alert = new Alert(AlertType.ERROR);
		                    alert.initModality(Modality.APPLICATION_MODAL);
		                    alert.initOwner(addWin);
		                    alert.setTitle("Error");
		                    alert.setHeaderText("Invalid file name");
		                    alert.setContentText("File name can only contain letters, numbers or underscores \nFile name cannot be empty");
		                    alert.showAndWait();
						}
					}
					
					if(!fileName.isBlank()) {
						tester.addFile(places, people, title, body, fileName);
						Alert alert = new Alert(AlertType.INFORMATION);
	                    alert.initModality(Modality.APPLICATION_MODAL);
	                    alert.initOwner(addWin);
	                    alert.setTitle("Confirmation");
	                    alert.setHeaderText("Save Success");
	                    alert.setContentText("File '" + fileName + ".txt' saved successfully");
	                    alert.showAndWait();
	                    
	                    placesField.clear();
	                    peopleField.clear();
	                    titleField.clear();
	                    bodyArea.clear();
					}	
	            }
			}
        });
        
        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Search.newSearchWindow(stage);
				addWin.close();	
			}
        });
		
	}

	
}
